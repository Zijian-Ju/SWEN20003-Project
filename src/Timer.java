/**
 * Measure time after receiving command.
 */
public class Timer {
	private static final int INITIAL_TIMER_VALUE = 0;
	//The time expected to count.
	private int timeoutMillis;
	//The time has passed since the timer begins.
	private int elapsedMillis;
	private boolean isRunning;
	
	/**
	 * Constructor. Set the expected time.
	 * @param timeoutMillis The time expected to be counted.
	 */
	public Timer(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
		this.elapsedMillis = INITIAL_TIMER_VALUE;
		isRunning = false;
	}
	/**
	 * Set elapsed time to given time.
	 * @param millis The elapsed time in millisecond.
	 */
	public void setElapsedMillis(int millis) {
		elapsedMillis = millis;
	}
	/**
	 * Let timer start working.
	 */
	public void start() {
		isRunning = true;
	}
	/**
	 * Let timer stop working.
	 */
	public void stop() {
		isRunning = false;
	}
	/**
	 * Check whether the timer is runing.
	 * @return Whether the timer is running.
	 */
	public boolean isRunning() {
		return isRunning;
	}
	/**
	 * Update the timer for a frame.
	 * @param millis The time for each frame.
	 */
	public void update(int millis) {
		if (isRunning)
			elapsedMillis += millis;
	}
	/**
	 * Check whether timer has reached the setting time.
	 * @return Whether timer has reached the setting time.
	 */
	public boolean timeout() {
		return elapsedMillis >= timeoutMillis;
	}
	/**
	 * Reset timer to 0.
	 */
	public void reset() {
		elapsedMillis = INITIAL_TIMER_VALUE;
	}
}
