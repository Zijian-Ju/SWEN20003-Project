import org.newdawn.slick.Graphics;

/**
 * Child class of Building.
 * Handle training Scout, Engineer. Builder.
 */
public class CommandCentre extends Building {
	/** Creating CommandCentre does not cost metal.*/
	public static final int METAL_COST = 0;
	/** Create CommandCentre needs 15 seconds */
	public static final int TIME_COST = 15000;
	
	private static final String IMAGE_PATH = "assets/buildings/command_centre.png";
	private static String[] menuItems = new String[]{"", "Scout", "Builder", "Engineer"};

	/**
	 * Constructor. See details in parent Unit class.
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 */
	public CommandCentre(World world, Position mapPosition) {
		super(world, mapPosition, IMAGE_PATH);
	}
	
	/**
	 * Train matching Unit when get matching input.
	 * @param menuInput Menu input converted from player input.
	 */@Override
	public String getTrainingUnitType(int menuInput) {
			try{
				return menuItems[menuInput];
			}catch(RuntimeException e) {
				return "";
			}
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
