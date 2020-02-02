import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;

/**
 * Child class of Building.
 * Can be activated and then add Engineer capacity.
 */
public class Pylon extends Building{
	private static final String IMAGE_PATH = "assets/buildings/pylon.png";
	private static final String ACTIVE_IMAGE_PATH = "assets/buildings/pylon_active.png";
	private static final int PYLON_ACTIVATION_AREA = 32;
	private boolean activated = false;

	/**
	 * Constructor. See details in parent Unit class.
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 */
	public Pylon(World world, Position mapPosition) {
		super(world, mapPosition, IMAGE_PATH);
	}
	
	/**
	 * Update the pylon for a frame.
	 * @param input The input of the player
	 * @param delta Time passed since last frame (milliseconds).
	 * @param mouseButton Which mouse button did the player click 
	 * @param clickPos The position of the click.
	 */
	@Override
	public void update(Input input, int delta, int mouseButton, Position clickPos) {
		super.update(input, delta, mouseButton, clickPos);
		//If any unit near the pylon, it will be activated.
		for(Unit unit : getWorld().getUnits()) {
			if(getPos().distance(unit.getPos()) <= PYLON_ACTIVATION_AREA) {
				setActivated();
				break;
			}
		}
	}
	
	/**
	 * Set the status of pylon to active.
	 */
	public void setActivated() {
		if (!activated) {
			activated = true;
			Engineer.increaseCapacity();
			try {
				setImage(new Image(ACTIVE_IMAGE_PATH));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Check the status whether the pylon is activated.
	 * @return Whether the pylon is activated.
	 */
	public boolean isActivated() {
		return activated;
	}
}
