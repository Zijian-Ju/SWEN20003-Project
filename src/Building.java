
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;

/**
 * Parent class for all buildings.
 * Handles train units and other function in GameObject class like rending and update.
 */
public class Building extends GameObject implements Selectable {
	/**The radius surround a unit to consider whether the building is selected*/
	public static final int BUILDING_AREA_RADIUS = 32; 
	
	private static final String HIGHLIGHT_IMAGE_PATH = "assets/highlight_large.png";
	private static Image highlightImage = null;
	//The time and metal cost for creating building, depending on the building type. Initially considering it as -1.
	private static int metalCost = -1;
	private static int timeCost = -1;
	private static final String CLASS_NAME_SCOUT = "Scout";
    private static final String CLASS_NAME_BUILDER = "Builder";
    private static final String CLASS_NAME_ENGINEER = "Engineer";
    private static final String CLASS_NAME_TRUCK = "Truck";
	private static final int MENU_NO_CHOICE = 0;
    private int menuInput;
    //Timer for training units.
	private Timer trainingTimer;
	private String trainingUnitType;
	private boolean selected = false;
	
	/**
	 * Constructor is similar to GameObject parent class.
	 * @param world
	 * @param mapPosition
	 * @param imagePath
	 */
	public Building(World world, Position mapPosition, String imagePath) {
		super(world, mapPosition, imagePath);
		try {
			if (highlightImage == null) highlightImage = new Image(HIGHLIGHT_IMAGE_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		trainingTimer = new Timer(Unit.TIME_COST);
		menuInput = MENU_NO_CHOICE;
	}
	/**
	 * Get the trainingTimer of the building.
	 * @return trainingTimer The timer to train new unit,
	 */
	public Timer getTrainingTimer() {
		return trainingTimer;
	}
	/**
	 * Get the input of the menu list.
	 * @return menuInput  The input on the menu list.
	 */
	public int getMenuInput() {
		return menuInput;
	}	
	/**
	 * Get the metalCost for the building.
	 * @return metalCost The metal cost to create this building.
	 */
	public static int getMetalCost() {
		return metalCost; 
	}
	/**
	 * Get the timeCost for the building.
	 * @return timeCost The cost of time to create this building.
	 */
	public static int getTimeCost() {
		return timeCost; 
	}
	/**
	 * Check whether the building is selected.
	 *@return selected The status of whether the building is selected.
	 */
	@Override
	public boolean isSelected() {
		return selected;
	}
	/**
	 *Let the building be selected.
	 */
	@Override
	public void select() {
		selected = true;
		getCamera().setFollowingObject(this);
		getWorld().setSelectedObject(this);
	}
	/**
	 *Let the building be deselected. 
	 */
	@Override
	public void deselect() {
		selected = false;
	}
	/**
	 * Update the building for a frame.
	 * @param input The input of the player
	 * @param delta Time passed since last frame (milliseconds).
	 * @param mouseButton Which mouse button did the player click 
	 * @param clickPos The position of the click.
	 */
	@Override
	public void update(Input input, int delta, int mouseButton, Position clickPos) {
		if (mouseButton == Input.MOUSE_LEFT_BUTTON) {
			if (selected) {
				deselect();
			} else {
				//If there is a unit around mouse click position, select the unit first.
				if (getWorld().unitAt(clickPos) == null && clickPos.distance(getPos()) <= BUILDING_AREA_RADIUS) {
					select();
				}
			}
		}		
		if (trainingTimer.isRunning()) {
			trainingTimer.update(delta);
			if (trainingTimer.timeout()) {
				addNewUnitToWorld();
				trainingTimer.stop();
				trainingTimer.reset();
				menuInput = MENU_NO_CHOICE;
			}
		} else if (isSelected()) {
			//When a building is selected, convert the input to corresponding number as the menuInput.
			if (input.isKeyPressed(Input.KEY_1)) {
				menuInput = 1;
            } else if (input.isKeyPressed(Input.KEY_2)) {
            	menuInput = 2;
       	    } else if (input.isKeyPressed(Input.KEY_3)) {
       	    	menuInput = 3;
        	} else {
        		menuInput = MENU_NO_CHOICE;
        	}
			//Convert the menuInput to the matching training unit.
			trainingUnitType = getTrainingUnitType(menuInput);
			startTraining();
		}
	}
	/**
	 * Get the training time of the unit.
	 * @return The time cost to train the unit.
	 */
	public int getTrainingTime() {
		return Unit.TIME_COST;
	}	
	/**
	 * Get the training unit type corresponding the menu input. Overrided in the child class.
	 * @param menuInput Menu input converted from player input.
	 * @return The training unit type corresponding the menu input.
	 */
	public String getTrainingUnitType(int menuInput) {
		return "";
	}
	/**
	 * Let the building start training unit.
	 */
	public void startTraining() {
		//If the training unit type is empty then create nothing.
		if ("".equals(trainingUnitType))
			return;
		int unitMetalCost = getUnitMetalCost(trainingUnitType);
		//When the metal amount is larger the metal cost, start training unit,
		if (getWorld().getMetalAmount() >= unitMetalCost) {
			getWorld().consumeMetal(unitMetalCost);
			trainingTimer.start();
		} else {
			menuInput = MENU_NO_CHOICE;
		}
	}
	
	/**
	 * Get the metal cost of units.
	 * @param buildingClassName The class name of the expected unit.
	 * @return The metal cost for creating expected unit.
	 */
	public int getUnitMetalCost(String buildingClassName) {
		if (CLASS_NAME_SCOUT.equals(buildingClassName)) {
			return Scout.METAL_COST;
		} else if (CLASS_NAME_BUILDER.equals(buildingClassName)) {
			return Builder.METAL_COST;
		} else if (CLASS_NAME_ENGINEER.equals(buildingClassName)) {
			return Engineer.METAL_COST;
		} else if (CLASS_NAME_TRUCK.equals(buildingClassName)) {
			return Truck.METAL_COST;
		} else {
			return -1;
		}
	}
	/**
	 * Add units to world when receiving orders.
	 */
	public void addNewUnitToWorld() {
		Unit newUnit = null;
		if (CLASS_NAME_SCOUT.equals(trainingUnitType)) {
			newUnit = new Scout(getWorld(), getPos());
		} else if (CLASS_NAME_BUILDER.equals(trainingUnitType)) {
			newUnit = new Builder(getWorld(), getPos());
		} else if (CLASS_NAME_ENGINEER.equals(trainingUnitType)) {
			newUnit = new Engineer(getWorld(), getPos());
		} else if (CLASS_NAME_TRUCK.equals(trainingUnitType)) {
			newUnit = new Truck(getWorld(), getPos());
		}
		getWorld().addNewUnit(newUnit);
	}
	
	/**
	 * Render method see details in parent class.
	 */
	@Override
	public void render() {
		if(selected) {
		    highlightImage.drawCentered((float)getScreenPos().getX(), (float)getScreenPos().getY());
		}
		super.render();
	}
	
	/**
	 * Render function menu when being selected.
	 */
	@Override
	public void renderMenu(Graphics g, String[] menuItems) {
		if (selected && menuItems != null) {
			super.renderMenu(g, menuItems);
		}
	}
}

	

