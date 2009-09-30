package ptm.client.exception;

@SuppressWarnings("serial")
public class UnmatchedUserException extends UserException {

	public UnmatchedUserException() {
		super();
	}

	public UnmatchedUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnmatchedUserException(String message) {
		super(message);
	}

	public UnmatchedUserException(Throwable cause) {
		super(cause);
	}

}
