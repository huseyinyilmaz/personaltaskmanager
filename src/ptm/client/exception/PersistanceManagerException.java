package ptm.client.exception;

@SuppressWarnings("serial")
public class PersistanceManagerException extends ScratchpadException {

	public PersistanceManagerException() {
		super();
	}

	public PersistanceManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistanceManagerException(String message) {
		super(message);
	}

	public PersistanceManagerException(Throwable cause) {
		super(cause);
	}

}
