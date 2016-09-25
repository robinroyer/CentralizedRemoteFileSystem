package ca.polymtl.inf4410.tp1.shared;

import java.io.Serializable;

/**
 * Classe Header. Done to store the classic information about a file. Using
 * headers is easier and make the transfert on the network less expensive.
 * 
 * @author Jeremy
 *
 */
public class Header implements Serializable {

	/**
	 * Best practice for every class implementing Serializable.
	 */
	private static final long serialVersionUID = -3884750658586381809L;
	
	/**
	 * The name of the file.
	 */
	private String name;
	
	/**
	 * The cliend id owner of the file.
	 */
	private Integer owner;
	
	/**
	 * If the file is locked, the value is true.
	 */
	private boolean lock;

	/**
	 * Default constructor
	 */
	public Header() {
	}

	/**
	 * Constructor with:
	 * 
	 * @param fileName:
	 *            the name of the file
	 */
	public Header(String fileName) {
		this.name = fileName;
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
	 * @param name
	 *            the name to set
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
	 * @param owner
	 *            the owner to set
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
	 * @param lock
	 *            the lock to set
	 */
	public void setLock(boolean lock) {
		this.lock = lock;
	}

	@Override
	public String toString() {
		String display = name;
		if (lock) {
			display += "\t - Verouille par client " + owner;
		} else {
			display += "\t - Non verouille";
		}
		return display;
	}
}
