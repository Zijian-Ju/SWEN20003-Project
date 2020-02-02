import org.newdawn.slick.Graphics;

public class Factory extends Building {
	/** Factory takes 100 metals to be built.*/
	public static final int METAL_COST = 100;
	/** Factory takes 10 seconds to be built.*/
	public static final int TIME_COST = 10000;
	
	private static final String IMAGE_PATH = "assets/buildings/factory.png";
	private static String[] menuItems = new String[]{"", "Truck"};
	private static final int CREATE_TRUCK_INDEX = 1;
	
	/**
	 * Constructor. See details in parent Unit class.
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 */
	public Factory(World world, Position mapPosition) {
		super(world, mapPosition, IMAGE_PATH);
	}
	/**
	 * Train truck when getting matching input.
	 * @param menuInput Menu input converted from player input.
	 */
	@Override
	public String getTrainingUnitType(int menuInput) {
		if (menuInput == CREATE_TRUCK_INDEX)
			return menuItems[menuInput];
		else
			return "";
	}
    /**
	* Render the menu on screen.
	* @param g The Slick graphics object, used for drawing.
	*/
	@Override
	public void renderMenu(Graphics g) {
		super.renderMenu(g, menuItems);
	}
}
