import org.newdawn.slick.Input;

/**
 * Parent class for all resources.
 * Handles be mined and destroyed.
 */
public class Resource extends GameObject implements Destructible {
	/** If distance between resource and engineer is below the radius. engineer start mining.*/
	public static final int RESOURCE_AREA_RADIUS = 32;
	/** Mining time that the engineer need to take before get resource.*/
	public static final int MINING_TIME = 5000;
	private int amount;
	private boolean isDestroyed;
	
	/**
	 * Constructor. See details in parent GameObject class.
	 * @param world
	 * @param mapPosition
	 * @param imagePath
	 * @param initialAmount
	 */
	public Resource(World world, Position mapPosition, String imagePath, int initialAmount) {
		super(world, mapPosition, imagePath);
		isDestroyed = false;
		amount = initialAmount;
	}
	/**
	 * Get remaining amount of the resource.
	 * @return amount The remaining amount of the resource.
	 */
	public int getAmount() {
		return amount;
	}	

	/**
	 * Get the amount that actually mined by engineer.
	 * @param miningCapacity The capacity of engineers.
	 * @return minedAmount Actually be mined amount of this resource.
	 */
	public int beMined(int miningCapacity) {
		int minedAmount = 0;
		if (amount >= miningCapacity) {
			amount -= miningCapacity;
			minedAmount = miningCapacity;
		} else {
			minedAmount = amount;
			amount = 0;
		}
		if (amount==0) {
			destroy();
		}
		return minedAmount;
	}
	
	/**
	 * Get status whether the resource is destroyed
	 *@return Whether the resource is destroyed
	 */
	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}
	
	/**
	 *Destroy resource.
	 */
	@Override
	public void destroy() {
		isDestroyed = true;
	}
	/**
	 * Update the resource for a frame.
	 * @param input The input of the player
	 * @param delta Time passed since last frame (milliseconds).
	 * @param mouseButton Which mouse button did the player click 
	 * @param clickPos The position of the click.
	 */
	@Override
	public void update(Input input, int delta, int mouseButton, Position clickPos) {
		if (amount == 0) isDestroyed = true;
	}

	/**
	 *Render the resource.
	 */
	@Override
	public void render() {
		if (!isDestroyed) super.render();
	}
}

	

