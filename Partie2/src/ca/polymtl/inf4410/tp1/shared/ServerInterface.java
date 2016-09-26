package ca.polymtl.inf4410.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Server interface : contains the signature of all the methods we must
 * implement on the server side.
 * 
 * @author Jeremy
 * 
 */
public interface ServerInterface extends Remote {
	/**
	 * Generate a client id
	 * 
	 * @return the client id
	 * @throws RemoteException
	 */
	int generateClientId() throws RemoteException;

	/**
	 * Command to create a file on the remote server
	 * 
	 * @param name
	 * @return false if the operation failed
	 * @throws RemoteException
	 */
	boolean create(String name) throws RemoteException;

	/**
	 * Command to ask for the list of files available on the server
	 * 
	 * @return a list of the headers available
	 * @throws RemoteException
	 */
	ArrayList<Header> list() throws RemoteException;

	/**
	 * Command to synchronize the remote directory and the local directory
	 * 
	 * @return the files from the server
	 * @throws RemoteException
	 */
	ArrayList<File> syncLocalDir() throws RemoteException;

	/**
	 * Command to get a file from the server
	 * 
	 * @param name
	 *            the name of the file
	 * @param checksum
	 *            the checksum of the local file you have
	 * @return the file from the server
	 * @throws RemoteException
	 */
	File get(String name, byte[] checksum) throws RemoteException;

	/**
	 * Command to lock a file on the remote server. If the checksum is
	 * different, you must get the file first.
	 * 
	 * @param name
	 *            the name of the file
	 * @param clientId
	 *            the client id of the client
	 * @param checksum
	 *            the checksum of the local file of the client
	 * @return null if the client has the file
	 * @throws RemoteException
	 */
	File lock(String name, Integer clientId, byte[] checksum)
			throws RemoteException, UnlockableFileException;

	/**
	 * Command to push a local file to the remote server
	 * 
	 * @param name
	 *            the name of the file
	 * @param content
	 *            the content of the file
	 * @param clientId
	 *            the client id of the client
	 * @return false if the operation failed
	 * @throws RemoteException
	 * @throws UnlockableFileException
	 *             if the file is already locked by another client.
	 */
	boolean push(String name, byte[] content, Integer clientId)
			throws RemoteException;
}
