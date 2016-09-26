/**
 * 
 */
package ca.polymtl.inf4410.tp1.shared;

/**
 * @author jewim
 *
 */
public class UnlockableFileException extends Exception {
	
	/**
	 * Best practices for each class extenting or implenting Serializable interface.
	 */
	private static final long serialVersionUID = 592932191259440100L;
	
	/**
	 * The name of the file
	 */
	private String fileName;
	

	/**
	 * Default constructor
	 */
	public UnlockableFileException() {
	}

	/**
	 * Normal constructor
	 */
	public UnlockableFileException(String message, String fileName) {
		super(message);
		this.fileName = fileName;
	}
	
	/**
	 * @param message
	 */
	public UnlockableFileException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnlockableFileException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnlockableFileException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UnlockableFileException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public String toString() {
		return this.getMessage() + " (" + fileName + ").";
	}

}
