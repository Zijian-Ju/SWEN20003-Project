import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.Input;
/**
 * Camera class. Capture part of world.
 * Can follow objects and move independently.
 */
public class Camera {
	/** Initially camera is at (0,0) of  the map.*/
	public static final Position INITIAL_POSITION = new Position(0, 0);
	
	private TiledMap map;
	private double left;
	private double top;
	private GameObject followingObject;
	private static final double CAMERA_MANUAL_MOVE_SPEED = 0.4;

	/**
	 * Constructor. Initially making camera at the left-top corner of the map.
	 * @param map The map of the world.
	 */
	public Camera(TiledMap map) {
		this.map = map;
		left = INITIAL_POSITION.getX();
		top =INITIAL_POSITION.getY();
		followingObject = null;
	}
	/**
	 * Get left edge position of the camera.
	 * @return The left edge of the map.
	 */
	public double getLeft() {
		return left;
	}	
	/**
	 * Get top edge position of the camera.
	 * @return The top edge of the map.
	 */
	public double getTop() {
		return top;
	}
	/**
	 * Set following object to given game object.
	 * @param gameObj To be followed game object.
	 */
	public void setFollowingObject(GameObject gameObj) {
		followingObject = gameObj;
	}
	/**
	 * Update camera position for a frame.
	 * @param input The input of the player
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(Input input, int delta) {
		//When KEY_W,A,S,D are pressed, the camera will move to matching direction for delta*speed.
		double manualMoveDistance = delta*CAMERA_MANUAL_MOVE_SPEED;
		if (input.isKeyDown(Input.KEY_W)) {
			if(top - manualMoveDistance >= 0) {
				top -= manualMoveDistance;
			} else {
				top = 0;
			}
			//When camera is moved by input. It will stop following objects.
			followingObject = null;
		} else if (input.isKeyDown(Input.KEY_S)) {
			if(top + manualMoveDistance <= map.getTileHeight()*map.getHeight()-App.WINDOW_HEIGHT) {
				top += manualMoveDistance;
			} else {
				top = map.getTileHeight()*map.getHeight()-App.WINDOW_HEIGHT;
			}
			followingObject = null;
		} else if (input.isKeyDown(Input.KEY_A)) {
			if(left - manualMoveDistance >= 0) {
				left -= manualMoveDistance;		
			} else {
				left = 0;
			}
			followingObject = null;
		} else if (input.isKeyDown(Input.KEY_D)) {
			if(left + manualMoveDistance <= map.getTileWidth()*map.getWidth()-App.WINDOW_WIDTH) {
				left += manualMoveDistance;
			} else {
				left = map.getTileWidth()*map.getWidth()-App.WINDOW_WIDTH;
			}
			followingObject = null;
		}
		
		if (followingObject != null) {
			followObj(followingObject);
		}
	}
	
	public void followObj(GameObject gameObject) {
		if (gameObject == null) 
			return;
		//Control the camera can only move inside the map.
		if (gameObject.getPos().getX() < App.WINDOW_WIDTH/2) {
			left = 0;
		} else if (gameObject.getPos().getX() > map.getTileWidth()*map.getWidth() - App.WINDOW_WIDTH/2) {
			left = map.getTileWidth()*map.getWidth()-App.WINDOW_WIDTH;
		} else {
			left = gameObject.getPos().getX() - App.WINDOW_WIDTH/2;
		}
		
		if (gameObject.getPos().getY() < App.WINDOW_HEIGHT/2) {
			top = 0;
		} else if (gameObject.getPos().getY() > map.getTileHeight()*map.getHeight() - App.WINDOW_HEIGHT/2) {
			top = map.getTileHeight()*map.getHeight() - App.WINDOW_HEIGHT;
		} else {
			top = gameObject.getPos().getY() - App.WINDOW_HEIGHT/2;
		}
	}
	
	/**
	 * Transfer camera coordinates to map coordinates 
	 * @param pos The object position on camera.
	 * @return The position on map.
	 */
	public Position screenPosToMapPos(Position pos) {
		double x = pos.getX() + left;
		double y = pos.getY() + top;
		Position mapPosition = new Position(x,y);
		return mapPosition;
	}
	/**
	 * Transfer map coordinates to camera coordinates 
	 * @param pos The object position on map.
	 * @return  The position on camera.
	 */
	public Position mapPosToScreenPos(Position pos) {
		double x = pos.getX() - this.left;
		double y = pos.getY() - this.top;
		Position screenPosition = new Position(x,y);
		return screenPosition;
	}	
}

