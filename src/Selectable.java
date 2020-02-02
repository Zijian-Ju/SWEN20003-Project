
/**
 * Allow object to be selected.
 */
public interface Selectable {
	/**
	 * Select the object. 
	 */
	public void select();
	/**
	 * Deselect the object. 
	 */
	public void deselect();
	/**
	 * Check whether the object is selected currently.
	 * @return Whether the object is selected currently.
	 */
	public boolean isSelected();
}
