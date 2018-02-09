package pl.symentis.shorturl.domain;

public class ConflictException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6928520333928798043L;

	public ConflictException(String message) {
        super(message);
    }
}
