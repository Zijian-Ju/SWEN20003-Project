import org.newdawn.slick.Input;

/**
 * Child class of  Unit. 
 * Handles mining resource and transport resource to CommandCentre. 
 */
public class Engineer extends Unit {
	/** Cost to create an engineer. */
	public static final int METAL_COST = 20;
	
	private static final String IMAGE_PATH = "assets/units/engineer.png";
	private static final double ENGINEER_SPEED = 0.1;
	private static final int INITIAL_CAPACITY = 2;
	private static final int CAPACITY_INCREMENT = 1;
	private static int capacity = INITIAL_CAPACITY;
	private Resource carryingRes;
	private Resource miningResource;
	private CommandCentre nearestCC;
	private boolean isMiningMode = false;
	private Timer miningTimer;

	/**
	 * Constructor. See details in parent Unit class.
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 */
	public Engineer(World world, Position mapPosition) {
		super(world, mapPosition, IMAGE_PATH, ENGINEER_SPEED);
		setTargetPosition(mapPosition);
		//Engineer cannot create buildings.
		setCanBuild(false);
		miningTimer = new Timer(Resource.MINING_TIME);
	}
	
	/**
	 * Update behaviour in world.
	 * @param input The input of the player
	 * @param delta Time passed since last frame (milliseconds).
	 * @param mouseButton Which mouse button did the player click 
	 * @param clickPos The position of the click.
	 */
	@Override
	public void update(Input input, int delta, int mouseButton, Position clickPos) {
		super.update(input, delta, mouseButton, clickPos);	
		//When engineer is close enough to a resource and not moving for mining time 	
		//without interrupted by mouse click, the engineer will mine the resource.
		if (isMiningMode) {
			//When engineer is mining and controlled to move by mouse click, engineer will stop mining.
			if (isManualControl(mouseButton)) {
				stopMining();
			} 
			//If engineer mined for enough time, it will stop mining and carry it to CommandCentre.
			else if (isMining()) {
				miningTimer.update(delta);
				if (miningTimer.timeout()) {
					transResourceToNearestCommandCentre();
					//After mining, timer will stop and reset.
					miningTimer.stop();
					miningTimer.reset();
				}
			} 
			//When engineer is not moving and check its position and decide what to do.
			else if (!isMoving()) {
				//When its position near the mining resource which is not destroyed, it will start mining.
				if (getPos().equals(miningResource.getPos())) {
					if (!miningResource.isDestroyed())
						startMining(miningResource);
					else
						stopMining();
				} 
				//When its position near the nearest commandCentre, it will move to mining resource.
				else if (getPos().equals(nearestCC.getPos())) {
					setTargetPosition(miningResource.getPos());
				}
			}
		} 
		//If engineer is not move, near a resource and is not mining a resource, it will set the ressource to mining resource.
		else {
			if (!isMoving()) {
				Resource res = getWorld().resourceAt(getPos());
				if (res != null) {
					startMining(res);
				}
			}
		}
		//If engineer is closed to a commandCentre, it will drop the resource.
		if (getWorld().buildingAt(getPos()) instanceof CommandCentre && carryingRes != null)
			dropOffResource();
	}
	/**
	 * @param mouseButton Right or left mouse button which is the input.
	 * @return Whether engineer is being controlled by player.
	 */
	public boolean isManualControl(int mouseButton) {
		return isSelected() && mouseButton == Input.MOUSE_RIGHT_BUTTON;
	}
	/**
	 * Get the status about whether engineer is mining.
	 * @return Whether engineer is mining.
	 */
	public boolean isMining() {
		return miningTimer.isRunning();
	}
	/**
	 * Let engineer start mining resource.
	 * @param res The resource that engineer will mine.
	 */
	public void startMining(Resource res) {
		if (res.getAmount() > 0) {
			miningResource = res;
			miningTimer.start();
			isMiningMode = true;
		}
	}
	
	/**
	 * Let engineer stop mining resource.
	 */
	public void stopMining() {
		miningResource = null;
		miningTimer.stop();
		miningTimer.reset();
		isMiningMode = false;
	}
	/**
	 * Transport resource to nearest commandCentre.
	 */
	public void transResourceToNearestCommandCentre() {
		if (carryingRes == null) {
			int minedAmount = miningResource.beMined(capacity);
			if (minedAmount > 0) {
				if (miningResource instanceof Metal)
					carryingRes = new Metal(getWorld(), null, minedAmount);
				else if (miningResource instanceof Unobtainium)
					carryingRes = new Unobtainium(getWorld(), null, minedAmount);
			}
		}
		nearestCC = findNearestCommandCentre();
		setTargetPosition(nearestCC.getPos());
	}
	/**
	 * Get the commandCentre which is closest to the engineer.
	 * @return The nearest CommandCentre.
	 */
	public CommandCentre findNearestCommandCentre() {
		CommandCentre nearestCommandCentre = null;
		double minDistance = -1.0;
		for (CommandCentre cc:getWorld().getCommandCentres()) {
			double toCCDistance = getPos().distance(cc.getPos());
			if (nearestCommandCentre == null || minDistance < 0) {
				nearestCommandCentre = cc;
				minDistance = toCCDistance;
			} else if (toCCDistance < minDistance) {
				nearestCommandCentre = cc;
				minDistance = toCCDistance;
			}
		}
		return nearestCommandCentre;
	}
	/**
	 * Drop off resource which is carried by the engineer to CommandCentre.
	 */
	public void dropOffResource() {
		if (carryingRes == null)
			return;
		else if (carryingRes instanceof Metal) {
			getWorld().addMetalAmount(((Metal)carryingRes).getAmount());
		}
		else if (carryingRes instanceof Unobtainium)
			getWorld().addUnobtainiumAmount(((Unobtainium)carryingRes).getAmount());
		carryingRes = null;
	}
	/**
	 * Increase capacity of engineer.
	 */
	public static void increaseCapacity() {
		capacity += CAPACITY_INCREMENT;
	}
}
