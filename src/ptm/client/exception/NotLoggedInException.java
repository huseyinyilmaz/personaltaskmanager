/**
 * Thrown if There is no current user on server
 */
package ptm.client.exception;
@SuppressWarnings("serial")
public class NotLoggedInException extends UserException{

	public NotLoggedInException() {
		super();
	}

	public NotLoggedInException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotLoggedInException(String message) {
		super(message);
	}

	public NotLoggedInException(Throwable cause) {
		super(cause);
	}

}
