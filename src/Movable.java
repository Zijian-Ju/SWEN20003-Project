
/**
 * Allow object to move.
 */
public interface Movable {
	/**
	 * Move to target position.
	 * @param pos Target Position
	 */
	public void move(Position pos);
	/**
	 * Check whether object is moving.
	 * @return Whether object is moving.
	 */
	public boolean isMoving();
}
