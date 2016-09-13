package ca.polymtl.inf4410.tp1.shared;

public class Content {
	/**
	 * Field content, the content of a file, table of bytes
	 */
	private byte[] content;
	/**
	 * Field checksum, content a representation of the content
	 */
	private Integer checksum;
	
	/**
	 * Default constructor
	 */
	public Content() 
	{
	}
	
	/**
	 * Constructor with:
	 * @param content: the content of a file, a table of bytes
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
	 * @param content the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	/**
	 * @return the checksum
	 */
	public Integer getChecksum() {
		return checksum;
	}

	/**
	 * @param checksum the checksum to set
	 */
	public void setChecksum(Integer checksum) {
		this.checksum = checksum;
	}

	/**
	 * Calculate the checksum of the file
	 * @param content
	 * @return checksum value
	 */
	private Integer calculateChecksum(byte[] content) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Update the checksum of the file, has to be done after an update of the content
	 */
	private void updateChecksum() 
	{		setChecksum(calculateChecksum(this.content));
	}
}
