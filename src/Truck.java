import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Child Class of Unit.
 * Handles creating CommandCentre and destroyed after creation.
 */
public class Truck extends Unit implements Destructible {
	/**Cost to train a truck. */
	public static final int METAL_COST = 150;
	
	private static final String IMAGE_PATH = "assets/units/truck.png";
	private static final double TRUCK_SPEED = 0.25;
	//To make the menuItem index be corresponding to the menu input index.
	private static String[] menuItems = new String[]{"", "CommandCentre"};
	private static final int CREATE_COMMANDCENTRE_INDEX = 1;
	private boolean isDestroyed;

	/**
	 * Constructor. See details in parent Unit class.
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 */
	public Truck(World world, Position mapPosition) {
		super(world, mapPosition, IMAGE_PATH, TRUCK_SPEED);
		setCanBuild(true);
		isDestroyed = false;
	}
	
	/**
	 *Update behaviour in the world. See details in parent Unit class.
	 */
	@Override
	public void update(Input input, int delta, int mouseButton, Position clickPos) {
		if (!isDestroyed)
			super.update(input, delta, mouseButton, clickPos);
	}
	
	/**
	 * Convert menu input to the matching building type.
	 * @param menuInput The input number converted from the player input.
	 */
	@Override
	public String getBuildingType(int menuInput) {
		if (menuInput == CREATE_COMMANDCENTRE_INDEX)
			return menuItems[menuInput];
		else
			return "";
	}
	
	/**
	 * Truck will be destroyed after building. Other details see parent class.
	 */
	@Override
	public void afterBuilding() {
		super.afterBuilding();
		destroy();
	}
	
	/**
	 * Render the truck.
	 */
	@Override
	public void render() {
		if (!isDestroyed) super.render();
	}

	/**
	 * Render the menu.
	 */
	@Override
	public void renderMenu(Graphics g) {
		super.renderMenu(g, menuItems);
	}

	/**
	 * Destroy the truck.
	 */
	@Override
	public void destroy() {
		deselect();
		isDestroyed = true;	
	}

	/**
	 * Check whether the truck is destroyed.
	 *@return Whether the truck is destroyed.
	 */
	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}
}
