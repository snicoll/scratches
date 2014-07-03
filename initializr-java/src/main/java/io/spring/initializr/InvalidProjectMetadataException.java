package io.spring.initializr;

/**
 *
 * @author Stephane Nicoll
 */
@SuppressWarnings("serial")
public class InvalidProjectMetadataException extends RuntimeException {

	public InvalidProjectMetadataException(String message) {
		super(message);
	}
}
