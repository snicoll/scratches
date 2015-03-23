package hornetq.autoconfigure;

/**
 * Define the mode in which HornetQ can operate.
 *
 * @author Stephane Nicoll
 * @since 1.1.0
 */
public enum HornetQMode {

	/**
	 * Connect to a broker using the native HornetQ protocol (i.e. netty).
	 */
	NATIVE,

	/**
	 * Embed (i.e. start) the broker in the application.
	 */
	EMBEDDED

}