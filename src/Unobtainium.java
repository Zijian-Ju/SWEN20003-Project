
/**
 * Child class of Resource.
 * Can be mined
 */
public class Unobtainium extends Resource{
	private static final int INITIAL_AMOUNT = 50;
	private static final String IMAGE_PATH = "assets/resources/unobtainium_mine.png";
	
	/**
	 * Constructor for the resource on map. See details in parent Unit class.
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 */
	public Unobtainium(World world, Position mapPosition) {
		super(world, mapPosition, IMAGE_PATH, INITIAL_AMOUNT);
	}
	
	/**
	 * Constructor for the resource carried by Engineer. See details in parent Unit class.
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 * @param  initialAmount The amount of the resource.
	 */
	public Unobtainium(World world, Position mapPosition, int initialAmount) {
		super(world, mapPosition, IMAGE_PATH, initialAmount);
	}
}
