package ca.polymtl.inf4410.tp1.shared;

// TODO: Split a file: have one header and one content
public class File {
	private String name; 
	private Integer owner;
	private Integer checksum;
	private boolean lock;
	private byte[] content;
	
	/**
	 * Default constructor without any field
	 */
	public File() {
	}
	
	/**
	 * 
	 * @param name: the name of the file
	 * @param content: the content of the file
	 */
	public File(String name, byte[] content) {
		super();
		this.name = name;
		this.owner = -1;
		this.checksum = generateChecksum(content);
		this.lock = false;
		this.content = content;
	}
	
	private Integer generateChecksum(byte[] content) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the owner
	 */
	public Integer getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Integer owner) {
		this.owner = owner;
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
	 * @return the lock
	 */
	public boolean isLock() {
		return lock;
	}
	/**
	 * @param lock the lock to set
	 */
	public void setLock(boolean lock) {
		this.lock = lock;
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
}
