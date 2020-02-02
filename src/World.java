import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * World class to contain the map and all game objects(buildings, units, resources).
 * Handles update, render and identify the property of the map. 
 */
public class World {
	private static final String MAP_PATH = "assets/main.tmx";
	private static final int TILEDMAP_LAYER_INDEX=0;
	private static final String INITIALISATION_FILE_PATH = "assets/objects.csv";
	private static final int OBJECT_NAME_INDEX=0;
	private static final int X_POSITION_INDEX =1;
	private static final int Y_POSITION_INDEX =2;
	//Name strings of each elements in the world.
	private static final String COMMAND_CENTRE_NAME = "command_centre";
	private static final String FACTORY_NAME = "factory";
	private static final String METAL_NAME = "metal_mine" ;
	private static final String UNOBTAINIUM_NAME = "unobtainium_mine" ;
	private static final String PYLON_NAME = "pylon";
	private static final String ENGINEER_NAME = "engineer";
	private static final String SCOUT_NAME = "scout";
	private static final String BUILDER_NAME = "builder";
	private static final String TRUCK_NAME = "truck";
	//Map property attributes.
	private static final String SOILD_TILE_PROPERTY_NAME = "solid";
	private static final String SOLID_TILE_PROPERTY_VALUE_TRUE = "true";
	private static final String OCCUPIED_TILE_PROPERTY_NAME = "occupied";
	private static final String OCCUPIED_TILE_PROPERTY_VALUE_TRUE = "true";
	//Constant value to indicate no mouse input currently.
	private static final int NO_MOUSE_BUTTON = -1;
	private static final Position AMOUNT_SATATEMENT_LOCATION = new Position(32,32);

	private TiledMap map;
	private Camera camera;

	private List<Unit> units = new ArrayList<Unit>();
	private List<Engineer> engineers = new ArrayList<Engineer>();
	private List<Building> buildings = new ArrayList<Building>();
	private List<CommandCentre>  commandCentres = new ArrayList<CommandCentre>();
	private List<Resource> resources = new ArrayList<Resource>();
    //Initially both resources amount is zero
    private int unobtainiumAmount = 0;
	private int metalAmount = 0;
	private String resourceAmount = "Metal: %d\nUnobtainium: %d" ;
	private GameObject selectedObject = null;
	
	/**
	 * Initialize World class
	 * @throws SlickException
	 */
	public World() throws SlickException {
		map = new TiledMap(MAP_PATH);
		camera = new Camera(map);
		initializeMap(INITIALISATION_FILE_PATH);
	}
	//Read a given file and allocate text of each row into proper GameObject and their position
	private void initializeMap(String fileName) throws SlickException {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {			
			String text;	
			while ((text = br.readLine()) != null) {
				//the format of the file:gameObject name, xPos, yPos.
				String[] column = text.split(",");
				String objectType = column[OBJECT_NAME_INDEX];
				Position pos = new Position(Double.parseDouble(column[X_POSITION_INDEX]), Double.parseDouble(column[Y_POSITION_INDEX]));
				if (objectType.equals(COMMAND_CENTRE_NAME))
					addNewBuilding(new CommandCentre(this, pos));
				else if (objectType.equals(FACTORY_NAME))
					addNewBuilding(new Factory(this, pos));
				else if (objectType.equals(METAL_NAME))
					addNewResource(new Metal(this, pos));
				else if (objectType.equals(UNOBTAINIUM_NAME))
					addNewResource(new Unobtainium(this, pos));
				else if (objectType.equals(PYLON_NAME))
					addNewBuilding(new Pylon(this, pos));
				else if (objectType.equals(ENGINEER_NAME))
					addNewUnit(new Engineer(this, pos));
				else if (objectType.equals(SCOUT_NAME))
					addNewUnit(new Scout(this, pos));
				else if (objectType.equals(BUILDER_NAME))
					addNewUnit(new Builder(this, pos));
				else if (objectType.equals(TRUCK_NAME))
					addNewUnit(new Truck(this, pos));		
			}			
		} catch (FileNotFoundException e) {
			System.out.println("File " + fileName + " not found:");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error reading " + fileName + ":");
			e.printStackTrace();
		}
	}
	
	/**
	 * Add new unit to the units list.
	 * @param newUnit Different type of Unit
	 */
	public void addNewUnit(Unit newUnit) {
		if (newUnit != null) units.add(newUnit);
	}
	/**
	 * Add new building to the buildings list.
	 * @param newBuilding Different type of Building
	 */
	public void addNewBuilding(Building newBuilding) {
		if (newBuilding != null) buildings.add(newBuilding);
		if (newBuilding instanceof CommandCentre) commandCentres.add((CommandCentre)newBuilding);
	}
	/**
	 * Add new resource to the resources list.
	 * @param newRes
	 */
	public void addNewResource(Resource newRes) {
		if (newRes != null) resources.add(newRes);
	}
	/**
	 * Add given amount to the unobtainium 
	 * @param unobtainiumAmount The expected additional amount of the unobtainium
	 */
	public void addUnobtainiumAmount(int unobtainiumAmount) {
		this.unobtainiumAmount += unobtainiumAmount;
	}
	/**
	 * Add given amount to the metal
	 * @param metalAmount The expected additional amount of the metal
	 */
	public void addMetalAmount(int metalAmount) {
		this.metalAmount += metalAmount;
	}
	/**
	 * Consume metal while creating units/buildings
	 * @param metalAmount
	 */
	public void consumeMetal(int metalAmount) {
		this.metalAmount -= metalAmount;
	}
	/**
	 * @return metalAmount The current holding metal amount.
	 */
	public int getMetalAmount() {
		return metalAmount;
	}
	/**
	 * Set the metalAmount to given amount
	 * @param metalAmount
	 */
	public void setMetalAmount(int metalAmount) {
		this.metalAmount = metalAmount;
	}
	/** 
	 * @return camera The camera of the world
	 */
	public Camera getCamera() {
		return camera;
	}
	/**
	 * Get the currently selected object
	 * @return selectedObject The GameObject which is currently selected
	 */
	public GameObject getSelectedObject() {
		return selectedObject;
	}
	/**
	 * Set the selecting object to the given object
	 * @param obj The expected being selected object
	 */
	public void setSelectedObject(GameObject obj) {
		selectedObject = obj;
	}
	/**
	 *  Update the world  for a frame.
	 * @param input The input by the player
	 * @param delta Time passed since last frame (milliseconds).
	 * @throws SlickException
	 */
	public void update(Input input, int delta) throws SlickException{
		int mouseButton = NO_MOUSE_BUTTON;
		Position clickPos = null;
		// If the mouse button is being clicked, record which button is clicked and its position
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			mouseButton = Input.MOUSE_LEFT_BUTTON;
			clickPos = camera.screenPosToMapPos(new Position(input.getMouseX(), input.getMouseY()));
		} else if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			mouseButton = Input.MOUSE_RIGHT_BUTTON;
			clickPos = camera.screenPosToMapPos(new Position(input.getMouseX(), input.getMouseY()));
		}	
		updateAllResources(input, delta, mouseButton, clickPos);
		updateAllBuildings(input, delta, mouseButton, clickPos);
		updateAllUnits(input, delta, mouseButton, clickPos);
		camera.update(input, delta);
		/**Reset the selectedObject if the selected object been deselected*/
		verifySelectedObject();
	}
	//Update all the resources in the map and remove the resources which are destroyed
	private void updateAllResources(Input input, int delta, int mouseButton, Position clickPos) {
		List<Resource> destroyedResources = new ArrayList<Resource>();
		for (Resource res: resources) {
			res.update(input, delta, mouseButton, clickPos);
			if (res.isDestroyed()) {
				destroyedResources.add(res);
			}
		}
		resources.removeAll(destroyedResources);
	}
	//Update all the buildings on the map
	private void updateAllBuildings(Input input, int delta, int mouseButton, Position clickPos) {
		for (Building building: buildings) {
			building.update(input, delta, mouseButton, clickPos);
		}
	}
	//Update all the units on the map
	private void updateAllUnits(Input input, int delta, int mouseButton, Position clickPos) {
		List<Unit> destroyedTrucks = new ArrayList<Unit>();
		for (Unit unit: units) {
			unit.update(input, delta, mouseButton, clickPos);
			if (unit instanceof Truck && ((Truck)unit).isDestroyed()) {
				destroyedTrucks.add(unit);
			}
		}
		units.removeAll(destroyedTrucks);
	}
	/**
	 * Draw the menu list on the screen.
	 * @param g The Slick graphics object, used for drawing.
	 */
	public void renderMenu(Graphics g) {
		if (selectedObject != null)
			selectedObject.renderMenu(g);
	}
	
	/**
	 * Draw all the objects and map on the screen
	 * @param g The Slick graphics object, used for drawing.
	 * @throws SlickException
	 */
	public void render(Graphics g) throws SlickException{
		map.render(getMapRenderX(), getMapRenderY());
		for (Resource res: resources) {
			res.render();
		}
		for (Building building: buildings) {
			building.render();
		}
		for (Unit unit: units) {
			unit.render();
		}
		renderMenu(g);
		renderAvailableRes(g);
	}
	

	/**
	 * Draw the current resource amount on the screen
	 * @param g The Slick graphics object, used for drawing.
	 */
	public void renderAvailableRes(Graphics g) {
		g.drawString(String.format(resourceAmount, metalAmount, unobtainiumAmount), (float)AMOUNT_SATATEMENT_LOCATION.getX(), (float)AMOUNT_SATATEMENT_LOCATION.getY());
	}
	/**
	 * Identify whether a given position tile is solid
	 * @param pos The position which is going to be checked on the map 
	 * @return The result whether the tile is solid
	 */
	public boolean isSolidTile(Position pos) {
		int tileWIndex = (int)(pos.getX()/map.getTileWidth());
		int tileHIndex = (int)(pos.getY()/map.getTileHeight());
		int tileId = map.getTileId(tileWIndex, tileHIndex, TILEDMAP_LAYER_INDEX);
		return SOLID_TILE_PROPERTY_VALUE_TRUE.equals(map.getTileProperty(tileId, SOILD_TILE_PROPERTY_NAME,""));
	}
	/**
	 * Identify whether a given position tile is occupied
	 * @param pos The position which is going to be checked on the map 
	 * @return The result whether the tile is occupied
	 */
	public boolean isOccupiedTile(Position pos) {
		int tileWIndex = (int)(pos.getX()/map.getTileWidth());
		int tileHIndex = (int)(pos.getY()/map.getTileHeight());
		int tileId = map.getTileId(tileWIndex, tileHIndex, TILEDMAP_LAYER_INDEX);
		return OCCUPIED_TILE_PROPERTY_VALUE_TRUE.equals(map.getTileProperty(tileId, OCCUPIED_TILE_PROPERTY_NAME,""));
	}
	//Convert the camera X-location on map to the render location of map.
	private int getMapRenderX() {
		int mapLeft = - (int)camera.getLeft();
		return mapLeft;
	}
	//Convert the camera Y-location on map to the render location of map.
	private int getMapRenderY() {
		int mapTop = - (int)camera.getTop();
		return mapTop;
	}
	/**
	 * Get the map of the world
	 * @return map The map of the world.
	 */
	public TiledMap getMap() {
		return map;
	}
	/**
	 * Get the engineer list of the world
	 * @return engineers The engineer list of the world.
	 */
	public List<Engineer> getEngineers() {
		return engineers;
	}
	/**
	 * Get the commandCentre list of the world
	 * @return commandCentres The commandCentre list of the world.
	 */
	public List<CommandCentre> getCommandCentres() {
		return commandCentres;
	}
	/**
	 * Get the unit list of the world
	 * @return units The unit list of the world.
	 */
	public List<Unit> getUnits() {
		return units;
	}
	/**
	 * Get the building list of the world
	 * @return building The building list of the world.
	 */
	public List<Building> getBuildings() {
		return buildings;
	}	
	/**
	 * Get the resource list of the world
	 * @return resources The resource list of the world.
	 */
	public List<Resource> getResources() {
		return resources;
	}
	
	/**
	 * Identify the resource on a given position
	 * @param pos The given position 
	 * @return res The resource on the position
	 */
	public Resource resourceAt(Position pos) {
		if (pos == null)
			return null;	
		for (Resource res:resources) {
			if (pos.distance(res.getPos()) <= Resource.RESOURCE_AREA_RADIUS) 
				return res;
		}
		return null;
	}
	/**
	 * Identify the building on a given position
	 * @param pos The given position 
	 * @return res The building on the position
	 */
	public Building buildingAt(Position pos) {
		if (pos == null) 
			return null;	
		for (Building building:buildings) {
			if (pos.distance(building.getPos()) <= Building.BUILDING_AREA_RADIUS) 
				return building;
		}
		return null;
	}
	/**
	 * Identify the unit on a given position
	 * @param pos The given position 
	 * @return res The unit on the position
	 */
	public Unit unitAt(Position pos) {
		if (pos == null)
			return null;
		for (Unit unit:units) {
			if (pos.distance(unit.getPos()) <= Unit.SELECTION_AREA_RADIUS) 
				return unit;
		}
		return null;
	}
	/**Reset the selectedObject if it has been deselected*/
	public void verifySelectedObject() {
		if (selectedObject instanceof Building && !((Building)selectedObject).isSelected())
			selectedObject = null;
		else if (selectedObject instanceof Unit && !((Unit)selectedObject).isSelected())
			selectedObject = null;
	}
}
