/**
 * 
 */
package ca.polymtl.inf4410.tp1.shared;

/**
 * @author jewim
 *
 */
public class UnpushableFileException extends Exception {
	
	/**
	 * Best practices for each class extenting or implenting Serializable interface.
	 */
	private static final long serialVersionUID = 123456789098765432L;
	
	/**
	 * The name of the file
	 */
	private String fileName;
	

	/**
	 * Default constructor
	 */
	public UnpushableFileException() {
	}

	/**
	 * Normal constructor
	 */
	public UnpushableFileException(String message, String fileName) {
		super(message);
		this.fileName = fileName;
	}
	
	/**
	 * @param message
	 */
	public UnpushableFileException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnpushableFileException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnpushableFileException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UnpushableFileException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public String toString() {
		return this.getMessage() + " (" + fileName + ").";
	}

}
