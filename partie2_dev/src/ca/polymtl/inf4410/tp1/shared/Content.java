package ca.polymtl.inf4410.tp1.shared;

import java.security.MessageDigest;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 * Classe Content. Represent the content of a file. Done to represent the real
 * content and checksum of a file.
 * 
 * @author Jeremy
 *
 */
public class Content implements Serializable {

	/**
	 * Best practive for each class implementing Serializable.
	 */
	private static final long serialVersionUID = 5619314497772762182L;
	
	/**
	 * Field content, the content of a file, table of bytes
	 */
	private byte[] content;
	
	/**
	 * Field checksum, content a representation of the content
	 */
	private byte[] checksum;

	/**
	 * Default constructor
	 */
	public Content() {
		this(new byte[0]);
	}

	/**
	 * Constructor with:
	 * 
	 * @param content:
	 *            the content of a file, a table of bytes
	 */
	public Content(byte[] content) {
		this.content = content;
		this.checksum = calculateChecksum(content);
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @return the checksum
	 */
	public byte[] getChecksum() {
		return checksum;
	}

	/**
	 * @param checksum
	 *            the checksum to set
	 */
	public void setChecksum(byte[] checksum) {
		this.checksum = checksum;
	}

	/**
	 * Calculate the checksum of the file
	 * 
	 * @param content
	 * @return checksum value
	 */
	private byte[] calculateChecksum(byte[] content) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md.digest(content);
	}
}
