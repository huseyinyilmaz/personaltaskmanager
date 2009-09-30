package ptm.client.exception;

@SuppressWarnings("serial")
public abstract class ScratchpadException extends Exception {

	public ScratchpadException() {
		super();
	}

	public ScratchpadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ScratchpadException(String message) {
		super(message);
	}

	public ScratchpadException(Throwable cause) {
		super(cause);
	}

}
