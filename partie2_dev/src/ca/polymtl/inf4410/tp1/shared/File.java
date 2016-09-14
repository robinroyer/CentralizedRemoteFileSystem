package ca.polymtl.inf4410.tp1.shared;

import java.io.Serializable;

/**
 * 
 * @author Jeremy
 *
 */
public class File implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4285731006093427707L;
	private Header header;
	private Content content;
	
	/**
	 * Default constructor without any field
	 */
	public File() {
		this.header = null;
		this.content = null;
	}
	
	/**
	 * 
	 * @param name: the name of the file
	 * @param content: the content of the file
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
	 * @param header the header to set
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
	 * @param content the content to set
	 */
	public void setContent(Content content) {
		this.content = content;
	}
}
