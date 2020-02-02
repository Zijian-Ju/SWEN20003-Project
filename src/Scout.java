/**
 * Child class of Unit.
 * Only can move.
 */
public class Scout extends Unit {
	/**Cost to train a scout. */
	public static final int METAL_COST = 5;
	
	private static final String IMAGE_PATH = "assets/units/scout.png";
	private static final double SCOUT_SPEED = 0.3;

	/**
	 * Constructor. See details in parent Unit class.
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 */
	public Scout(World world, Position mapPosition) {
		super(world, mapPosition, IMAGE_PATH, SCOUT_SPEED);
		setCanBuild(false);
	}
}
