package ca.polymtl.inf4410.tp1.shared;

public class Header {
	private String name; 
	private Integer owner;
	private boolean lock;
	
	/**
	 * Default constructor
	 */
	public Header() 
	{
	}
	
	/**
	 * Constructor with:
	 * @param fileName: the name of the file
	 */
	public Header(String fileName)
	{	this.name = fileName;
		this.owner = -1;
		this.lock = false;
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
}
