package ca.polymtl.inf4410.tp1.shared;

import java.io.Serializable;

/**
 * Class File. Represent a custom file for our project. It contains a Header and
 * a Content to simplify the data transferts on the network.
 * 
 * @author Jeremy
 *
 */
public class File implements Serializable {

	/**
	 * Best practive for each class implementing Serializable.
	 */
	private static final long serialVersionUID = -4285731006093427707L;

	/**
	 * The header of the file.
	 */
	private Header header;

	/**
	 * The content of the file
	 */
	private Content content;

	/**
	 * Default constructor without any field
	 */
	public File() {
		this.header = null;
		this.content = null;
	}

	/**
	 * Constructor with:
	 * 
	 * @param name:
	 *            the name of the file
	 */
	public File(String name) {
		this.header = new Header(name);
		this.content = new Content();
	}

	/**
	 * Contructor with :
	 * 
	 * @param name:
	 *            the name of the file
	 * @param content:
	 *            the content of the file
	 */
	public File(String name, byte[] content) {
		this.header = new Header(name);
		this.content = new Content(content);
	}

	/**
	 * @return the header
	 */
	public Header getHeader() {
		return header;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(Header header) {
		this.header = header;
	}

	/**
	 * @return the content
	 */
	public Content getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(Content content) {
		this.content = content;
	}
}
