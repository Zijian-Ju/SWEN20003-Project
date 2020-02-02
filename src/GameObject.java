import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Abstract class for all the game objects in the world.
 * Handles update and render of the game objects.
 */
public abstract class GameObject {
	private Image image;
	private Position pos;
	private World world;
	private Camera camera;
	private static final Position MENU_POSITION = new Position(32, 100);
	
	/**
	 * Initialize the gameObject with world, position and image
	 * @param world The world which the game objects are inside.
	 * @param pos The expecting position of the object.
	 * @param imagePath The image path which store the iamge of the object.
	 */
	public GameObject(World world, Position pos, String imagePath) {
		try {
			this.world = world;
			this.pos = pos;
			camera = world.getCamera();
			this.image = new Image(imagePath);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Set the image of a game object.
	 * @param image The image of the gameObject
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	/**
	 * Get the map position of this object
	 * @return The gameobject position map.
	 */
	public Position getPos() {
		return pos;
	}
	/**
	 * Set the gameObject to a given position.
	 * @param pos The expecting position of the gameObject
	 */
	public void setPos(Position pos) {
		this.pos = pos;
	}
	
	/**
	 * Get the screen position of the gameObject.
	 * @return The Position on the screen.
	 */
	public Position getScreenPos() {
		return camera.mapPosToScreenPos(pos);
	}	
	/**
	 * Get the world of the game object.
	 * @return world The world which the gameObject is in.
	 */
	public World getWorld() {
		return world;
	}
	/**
	 * Get the camera of the game object.
	 * @return camera The camera which the gameObject is in.
	 */
	public Camera getCamera() {
		return camera;
	}	
	/**
	 * Update the game object for a frame.
	 * @param input The input of the player
	 * @param delta Time passed since last frame (milliseconds).
	 * @param mouseButton Which mouse button did the player click 
	 * @param clickPos The position of the click.
	 */
	public abstract void update(Input input, int delta, int mouseButton, Position clickPos);
	
	/**Render the gameObjectr on screen, so it reflects the current game state.*/
	public void render() {
		if (pos == null)
			return;
		else
			image.drawCentered((float)(getScreenPos().getX()), (float)(getScreenPos().getY()));
	}
	/**
	 * Render the menu on screen.
	 * @param g The Slick graphics object, used for drawing.
	 * @param menuItems The item list that the gameObject can create
	 */
	public void renderMenu(Graphics g, String[] menuItems) {
		//The element for 0 index of all menuitem is empty to be related to the input, so the method starts from 1.
		int i = 1;
		String menu = String.valueOf(i) + "- Create " + menuItems[i];
		i++;
		while (i < menuItems.length) {
			menu += "\n" + String.valueOf(i) + "- Create " + menuItems[i];
			i++;
		}
    	g.drawString(menu, (float)MENU_POSITION.getX(), (float)MENU_POSITION.getY());
	}
	/**
	 * Overload method for the above one.
	 * @param g The Slick graphics object, used for drawing.
	 */
	public void renderMenu(Graphics g) {
		
	}
	

}
