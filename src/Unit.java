import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;
/**
 * Parent class for Truck, Scout, Builder, Engineer.
 * Handles movement, select, create buildings, update and rendering.
 */
public class Unit extends GameObject implements Movable , Selectable {
	/**The menu input when key 1 pressed */
	public static final int KEY_1_INDEX = 1;
    /**The radius surround a unit to consider whether the unit is selected*/
    public static final int SELECTION_AREA_RADIUS = 32;
    /**Time cost to create a unit in millisecond*/
    public static final int TIME_COST = 5000;
	
    private static final String HIGHLIGHT_IMAGE_PATH = "assets/highlight.png";
    private static final String CLASS_NAME_COMMAND_CENTRE = "CommandCentre";
    private static final String CLASS_NAME_FACTORY = "Factory";
    //The object list of the building which the unit can create
    private static String[] menuItems;
    //Indicate no item in the item menu is chosen to be created
    private static final int MENU_NO_CHOICE = 0;
    private int delta;
    //Initially no objects should be created
    private int menuInput = MENU_NO_CHOICE;

	private static Image highlightImage = null;
	//Status which presents whether the unit can create new buildings.
	private boolean canBuild;
	//The time when the building starts to be built
	private int buildingMillis = 0;
	private Timer buildingTimer;
	private String creatingBuildingType;

	private Position targetPosition;
	
	private boolean selected = false;
	private double speed;
	
	/**
	 * Initialize the unit with world, position, image and speed
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 * @param imagePath The image path which store the image of the object.
	 * @param speed The moving speed of the unit
	 */
	public Unit(World world, Position mapPosition, String imagePath,  double speed) {
		super(world, mapPosition, imagePath);
		this.speed = speed;
		targetPosition = getPos();
		try {
			if (highlightImage == null) highlightImage = new Image(HIGHLIGHT_IMAGE_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		buildingTimer = null;
		creatingBuildingType = "";
	}
	/**
	 * Set the canBuild status.
	 * @param canBuild The expecting status of the unit for canBuild
	 */
	public void setCanBuild(boolean canBuild) {
		this.canBuild = canBuild;
	}
	/**
	 * Get whether this unit can create buildings
	 * @return canBuild Whether this unit can create buildings.
	 */
	public boolean getCanBuild() {
		return canBuild;
	}
	/**
	 * Set the expecting timer for the unit
	 * @param timer The expecting timer for the unit
	 */
	public void setBuildingTimer(Timer timer) {
		buildingTimer = timer;
	}
	/**
	 * Check status for whether the unit is creating building
	 * @return The status for whether the unit is creating building
	 */
	public boolean isBuilding() {
		return buildingTimer != null && buildingTimer.isRunning();
	}
	/**
	 * Get the time when the unit start creating the building.
	 * @return buildingMillis The time when start to create building.
	 */
	public int getBuildingMillis() {
		return buildingMillis;
	}
	/**
	 * Set the start creating time to the given input.
	 * @param buildingMillis The time when start to create building.
	 */
	public void setBuildingMillis(int buildingMillis) {
		this.buildingMillis = buildingMillis;
	}
	/**
	 * Get the input of the menu list.
	 * @return menuInput  The input on the menu list.
	 */
	public int getMenuInput() {
		return menuInput;
	}
	/**
	 * Set the meunInput to the given number.
	 * @param menuInput The input on the menu list.
	 */
	public void setMenuInput(int menuInput) {
		this.menuInput = menuInput;
	}
	/**
	 * Get the target position for movement on the map.
	 * @return targetPosition The target position for movement on the map.
	 */
	public Position getTargetPosition() {
		return targetPosition;
	}
	/**
	 * Set target position of the unit.
	 * @param targetPosition Target position on map.
	 */
	public void setTargetPosition(Position targetPosition) {
		this.targetPosition = targetPosition;
	}
	
	/**Update method, look up details in parent class.*/
	@Override
	public void update(Input input, int delta, int mouseButton, Position clickPos) {
		if (canBuild) {
			//Update method for unit which can create building.
			buildingUnitUpdate(input, delta);
		}
		//Update method for all the units.
		generalUnitUpdate(input, delta, mouseButton, clickPos);
	}	
	/**
	 * Update method for unit which can create building.
	 * @param input The input of the player
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void buildingUnitUpdate(Input input, int delta) {
		if (isBuilding()) {
			buildingTimer.update(delta);
			if (buildingTimer.timeout()) {
				addNewBuildingToWorld();
				afterBuilding();
			}
		} else {
			//If unit is selected and its location is not occupied, it can create building with input KEY_1.
			if (isSelected() && input.isKeyPressed(Input.KEY_1) && !getWorld().isOccupiedTile(getPos())) {
				menuInput = KEY_1_INDEX;
			} else {
				menuInput = MENU_NO_CHOICE;
			}			
			if (menuInput != MENU_NO_CHOICE) {
				targetPosition = getPos();
				creatingBuildingType = getBuildingType(menuInput);
				startBuilding();
			}
		}
	}
	/**
	 * Update method for all the units.
	 * @param input The input of the player
	 * @param delta Time passed since last frame (milliseconds).
	 * @param mouseButton Which mouse button did the player click 
	 * @param clickPos The position of the click.
	 */
	public void generalUnitUpdate(Input input, int delta, int mouseButton, Position clickPos) {
		this.delta = delta;
		if (mouseButton == Input.MOUSE_LEFT_BUTTON) {
			if (selected) {
				deselect();
			} else {
				if (clickPos.distance(getPos()) <= SELECTION_AREA_RADIUS) {
					select();
				}
			}
		} else if (mouseButton == Input.MOUSE_RIGHT_BUTTON) {
			if (selected && !isBuilding()) {
				targetPosition = clickPos;
			}
		}
		move(targetPosition);
	}
	/**
	 * Get the building type with given menuInput. Overrided in child class.
	 * @param menuInput The input number convered from input.
	 * @return null
	 */
	public String getBuildingType(int menuInput) {
		return "";
	}
	/**
	 * Start create building.
	 */
	public void startBuilding() {
		//Build nothing if creatingBuildingType is null.
		if ("".equals(creatingBuildingType)) 
			return;
		int buildingMetalCost = getBuildingMetalCost(creatingBuildingType);
		if (getWorld().getMetalAmount() >= buildingMetalCost) {
			getWorld().consumeMetal(buildingMetalCost);
			buildingTimer = new Timer(getBuildingTimeCost(creatingBuildingType));
			buildingTimer.start();
		}
	}
	/**
	 * Get the metal cost for creating new buildings with given building class name.
	 * @param buildingClassName The class name of the building.
	 * @return The metal cost for the building.
	 */
	public int getBuildingMetalCost(String buildingClassName) {
		if (CLASS_NAME_COMMAND_CENTRE.equals(buildingClassName)) {
			return CommandCentre.METAL_COST;
		} else if (CLASS_NAME_FACTORY.equals(buildingClassName)) {
			return Factory.METAL_COST;
		} else {
			return -1;
		}
	}	
	/**
	 * Get the time cost for creating new buildings with given building class name.
	 * @param buildingClassName The class name of the building.
	 * @return The time cost to create the building.
	 */
	public int getBuildingTimeCost(String buildingClassName) {
		if (CLASS_NAME_COMMAND_CENTRE.equals(buildingClassName)) {
			return CommandCentre.TIME_COST;
		} else if (CLASS_NAME_FACTORY.equals(buildingClassName)) {
			return Factory.TIME_COST;
		} else {
			return -1;
		}
	}
	/**Add the building to the matching building list in the world.*/
	public void addNewBuildingToWorld() {
		Building newBuilding = null;
		if (CLASS_NAME_COMMAND_CENTRE.equals(creatingBuildingType)) {
			newBuilding = new CommandCentre(getWorld(), getPos());
		} else if (CLASS_NAME_FACTORY.equals(creatingBuildingType)) {
			newBuilding = new Factory(getWorld(), getPos());
		}
		getWorld().addNewBuilding(newBuilding);
	}
	/**Reset the timer and menuInput after creating a building.*/
	public void afterBuilding() {
		buildingTimer = null;
		menuInput = MENU_NO_CHOICE;
	}
	/**
	 * Let the unit move to given position.
	 *@param targePos The target position for the movement.
	 */
	@Override
	public void move(Position targetPos) {
		double distanceToTarget = getPos().distance(targetPos);
		if (distanceToTarget >= Position.DISTANCE_TOLERANCE) {
			double distanceToMove = Math.min(distanceToTarget, speed * delta);
			double radian = getPos().radian(targetPos);
			double dx = distanceToMove*Math.cos(radian);
			double dy = distanceToMove*Math.sin(radian);
			//Identify movement in each delta
			Position movingTo = new Position(getPos().getX()+dx, getPos().getY()+dy);
			if (!getWorld().isSolidTile(movingTo)) {
				setPos(movingTo);
			} else {
				targetPosition = getPos();
			}
		}
	}
	
	/**
	 * Check whether the unit is moving.
	 *@return The status whether the unit is moving.
	 */
	@Override
	public boolean isMoving() {
		return !getPos().equals(targetPosition);
	}
	/**Make the unit be selected and let the camera follow it*/
	@Override
	public void select() {
		selected = true;
		getCamera().setFollowingObject(this);
		GameObject prevSelectedObj = getWorld().getSelectedObject();
		//Only select one unit for each time.
		if (prevSelectedObj instanceof Unit)
			((Unit)prevSelectedObj).deselect();
		//When selecting a building and a unit at the same time, select the unit only.
		else if (prevSelectedObj instanceof Building)
			((Building)prevSelectedObj).deselect();
		getWorld().setSelectedObject(this);
	}
	/**Set selected status to false.*/
	@Override
	public void deselect() {
		selected = false;
	}
	/**Check whether the unit is selected.*/
	@Override
	public boolean isSelected() {
		return selected;
	}
	/**Render the unit on screen, when unit is selected, render highlight image at the same position.*/
	@Override
	public void render() {
		if(selected) {
		    highlightImage.drawCentered((float)getScreenPos().getX(), (float)getScreenPos().getY());
		}
		super.render();
	}
	/**Render the menu when is selected, other information is in parent class.*/
	@Override
	public void renderMenu(Graphics g, String[] menuItems) {
		if (selected && menuItems != null) {
			super.renderMenu(g, menuItems);
		}
	}
}

	

