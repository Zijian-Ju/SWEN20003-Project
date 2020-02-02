/**
 * Store the position of game objects.
 * Handles calculating distance and radian between two positions.
 * Check whether two position is equal.
 */
public class Position {
	/**The distance tolerance to consider the unit has reached the position*/
	public static final double DISTANCE_TOLERANCE = 0.25;
	
	private double x;
    private double y;
	
    /**
     * Constructor to set the position.
     * @param x X-position.
     * @param y Y-position.
     */
    public Position(double x, double y) {
    	this.x = x;
    	this.y = y;
    }
	/**
	 * Get X-position of a position.
	 * @return X-position 
	 */
	public double getX() {
		return x;
	}
	/**
	 * Set X-position of a position.
	 * @param x Expecting X-position
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * Get Y-position of a position.
	 * @return Y-position 
	 */
	public double getY() {
		return y;
	}
	/**
	 * Set Y-position of a position.
	 * @param y Expecting Y-position
	 */
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * Get the distance between this position and input position.
	 * @param otherPos Other position.
	 * @return The distance between this position and input position.
	 */
	public double distance(Position otherPos) {
		return  Math.sqrt((x-otherPos.getX())*(x-otherPos.getX())+(y-otherPos.getY())*(y-otherPos.getY()));
	}
	/**
	 * 	Get the angle between two positions
	 * @param otherPos Other position.
	 * @return The angle between two positions
	 */
	public double radian(Position otherPos) {
		return Math.atan2(otherPos.getY()-y, otherPos.getX()-x);
	}
	/**
	 * 	Check whether two position are equal.
	 * @param otherPos Other position.
	 * @return The whether two position are equal.
	 */
	@Override
	public boolean equals(Object obj){
		if (obj == null) return false;
		
	    if (obj instanceof Position){
	    	Position pos = (Position) obj;
	    	if (pos == this) return true;
	    	else if (distance(pos) < DISTANCE_TOLERANCE) 
	    		return true;
	    	}
		return false;
	}
}
