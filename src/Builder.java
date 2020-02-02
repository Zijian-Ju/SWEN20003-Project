import org.newdawn.slick.Graphics;

public class Builder extends Unit {
	/** Creating a builder costs 10 metals*/
	public static final int METAL_COST = 10;
	
	private static final String IMAGE_PATH = "assets/units/builder.png";
	private static final double BUILDER_SPEED = 0.1;
	private static String[] menuItems = new String[]{"", "Factory"};
	private static final int CREATE_FACTROY_INDEX = 1;

	/**
	 * Constructor. See details in parent Unit class.
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 */public Builder(World world, Position mapPosition) {
		super(world, mapPosition, IMAGE_PATH, BUILDER_SPEED);
		setCanBuild(true);
	}
	/**
	* Creating Factory when getting matching input.
	* @param menuInput Menu input converted from player input.
	*/	 
	@Override
	public String getBuildingType(int menuInput) {
		if (menuInput == CREATE_FACTROY_INDEX )
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

