package ca.polymtl.inf4410.tp1.server;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import ca.polymtl.inf4410.tp1.shared.File;
import ca.polymtl.inf4410.tp1.shared.Header;
import ca.polymtl.inf4410.tp1.shared.ServerInterface;
import ca.polymtl.inf4410.tp1.shared.UnlockableFileException;
import ca.polymtl.inf4410.tp1.shared.UnpushableFileException;

/**
 * Server class. Represent the remove server in our project.
 * 
 * @author Jeremy
 * 
 */
public class Server implements ServerInterface {

	/**
	 * Main to run the server. No args required.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

	/**
	 * The list of the known clients.
	 */
	private ArrayList<Integer> clientsId;

	/**
	 * A data structure which contains all the information about the file which
	 * are locked and who locked them.
	 */
	private HashMap<String, Integer> filesLockers;

	/**
	 * A data structure which contains all the file available on the server.
	 */
	private ArrayList<File> fileList;

	/**
	 * A data structure to store all the header of each file. This data
	 * structure has been done to limit the amount of data send by the network
	 * for the list command for example.
	 */
	private ArrayList<Header> headerList;

	/**
	 * Constructor
	 */
	public Server() {
		super();
		clientsId = new ArrayList<Integer>();
		filesLockers = new HashMap<String, Integer>();
		fileList = new ArrayList<File>();
		headerList = new ArrayList<Header>();
	}

	/**
	 * Main method to run the server.
	 */
	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("server", stub);
			System.out.println("Server ready.");
		} catch (ConnectException e) {
			System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lance ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}

	@Override
	public int generateClientId() throws RemoteException {
		// Generate random id
		int id = (int) (Math.random() * Integer.MAX_VALUE);

		// Check for uniqueness of the id
		while (clientsId.contains(id)) {
			id = (int) (Math.random() * Integer.MAX_VALUE);
		}

		System.out.println("Nouvel user id cree avec l'id : " + id);
		return id;
	}

	@Override
	public boolean create(String name) throws RemoteException, FileAlreadyExistsException {
		// Creation of the file
		System.out.println("Creation du fichier " + name + " ...");
		File newFile = new File(name);

		// Check if the file already exists
		if (fileList.contains(getFile(name))) {
			String message = "Fichier \"" + name + "\" deja existant.";
			System.err.println(message);
			throw new FileAlreadyExistsException(message);
		}

		// Add the file to the data structures
		headerList.add(newFile.getHeader());
		System.out.println("Fichier " + name + " ajoute.");
		return fileList.add(newFile);
	}

	@Override
	public ArrayList<Header> list() throws RemoteException {
		System.out.println("Demande de la liste ...");
		return headerList;
	}

	@Override
	public ArrayList<File> syncLocalDir() throws RemoteException {
		System.out.println("Demande de synchronisation ...");
		return fileList;
	}

	@Override
	public File get(String name, byte[] checksum) throws RemoteException {
		// Get the file on the server side
		File file = getFile(name);

		// If the file does not exist on the server side
		if (file.equals(null)) {
			System.out.println("Fichier \"" + name + "\" inexistant cote serveur.");
			return null;
		}

		// No checksum detected, send the file to the client
		if (checksum == null) {
			System.out.println("Checksum non present.");
			System.out.println("Envoie de la version cote serveur.");
			return file;
		}

		// Checksum are the same, useless to re send the file to the client
		if (checksum.equals(file.getContent().getChecksum())) {
			return null;
		}

		return file;
	}

	@Override
	public File lock(String name, Integer clientId, byte[] checksum)
			throws RemoteException, UnlockableFileException, NoSuchFileException {
		System.out.println("Essai de veouillage du fichier " + name + " par le client : " + clientId);
		// Check if the file exists
		File file = getFile(name);
		if (file == null) {
			System.err.println("Le fichier " + name + " n'existe pas.");
			throw new NoSuchFileException(name);
		}

		// Check if the file is already locked
		if (filesLockers.containsKey(name)) {
			Integer locker = filesLockers.get(name);
			System.out.println("Fichier deja verouille par le client : " + locker);
			if (clientId.equals(locker)) {
				throw new UnlockableFileException("Vous avez deja verouille le fichier", name);
			} else {
				throw new UnlockableFileException("Verouillage du fichier impossible.", name);
			}
		}

		// Lock the file
		System.out.println("Verouillage du fichier en cours ...");
		file.getHeader().setLock(true);
		// Update header list to optimize network transfert
		updateHeaderList(true, file, clientId);
		// Update files/lockers hashmap
		filesLockers.put(name, clientId);

		System.out.println("Fichier " + name + " verouille par client " + clientId);

		// Check if the checksum of the local file if different from the remote
		// file
		if (!file.getContent().getChecksum().equals(checksum)) {
			System.out.println("Checksum different, get a faire avant lock.");
			System.out.println("Envoie du fichier au client.");
			return getFile(name);
		}

		return null;
	}

	@Override
	public boolean push(String name, byte[] content, Integer clientId)
			throws RemoteException, NoSuchFileException, UnpushableFileException {
		System.out.println("Essaie de push du fichier \"" + name + "\" par le client " + clientId + " ...");
		File file = getFile(name);

		// Check if the file exist
		if (file == null) {
			System.out.println("Fichier \"" + name + "\" inexistant cote serveur.");
			throw new NoSuchFileException(name);
		}

		// Check if the file has been locked before the push
		Integer locker = filesLockers.get(name);
		if (locker == null) {
			System.out.println("Fichier \"" + name + "\" non verouille.");
			throw new UnpushableFileException("Le fichier n'est pas lock", name);
		}

		if (!locker.equals(clientId)) {
			System.out.println("Fichier deja verouille par le client : " + locker);
			throw new UnpushableFileException(
					"Vous n'avez pas le lock sur le fichier, le lock est possede par le client : " + clientId, name);
		}

		// Change the content of the file and update the header
		System.out.println("Fichier \"" + name + "\" en cours de modification ...");
		file.getContent().setContent(content);
		file.getHeader().setLock(false);

		// Unlock the file
		filesLockers.remove(file.getHeader().getName());

		return true;
	}

	/**
	 * Returns the appropriate file according to the param name
	 * 
	 * @param name
	 *            : the name of the file
	 * @return the file or null if it does not exist
	 */
	private File getFile(String name) {
		for (File file : fileList) {
			if (file.getHeader().getName().equals(name)) {
				return file;
			}
		}
		return null;
	}

	/**
	 * Update the header list according to its parameters
	 * 
	 * @param locker
	 *            : true if we need to lock the file, false if unlock
	 * @param file
	 *            : the file to set
	 * @param clientId
	 *            : the id of the user
	 */
	private void updateHeaderList(boolean locker, File file, Integer clientId) {
		int index = headerList.indexOf(file.getHeader());
		headerList.get(index).setLock(true);
		headerList.get(index).setOwner(clientId);
	}
}
