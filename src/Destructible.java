
/**
 * Allow object to be destroyed.
 */
public interface Destructible {
	/**
	 * Destroy object.
	 */
	public void destroy();
	/**
	 * Check whether the object is destroyed.
	 * @return Whether the object is destroyed.
	 */
	public boolean isDestroyed();
}
