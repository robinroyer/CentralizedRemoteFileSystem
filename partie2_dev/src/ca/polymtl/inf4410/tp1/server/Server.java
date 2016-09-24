package ca.polymtl.inf4410.tp1.server;

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

public class Server implements ServerInterface {

	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

	private ArrayList<Integer> clientsId;
	private HashMap<String, Integer> filesLockers;
	private ArrayList<File> fileList;
	private ArrayList<Header> headerList;

	public Server() {
		super();
		clientsId = new ArrayList<Integer>();
		filesLockers = new HashMap<String, Integer>();
		fileList = new ArrayList<File>();
		headerList = new ArrayList<Header>();
	}

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
		// generate random id
		int id = (int) (Math.random() * Integer.MAX_VALUE);

		// check for uniqueness of the id
		while (clientsId.contains(id)) {
			id = (int) (Math.random() * Integer.MAX_VALUE);
		}

		System.out.println("Nouvel user id cree avec l'id : " + id);
		return id;
	}

	@Override
	public boolean create(String name) throws RemoteException {
		// Creation du fichier
		System.out.println("Creation du fichier " + name + " ...");
		File newFile = new File(name);

		// Check if the file already exists
		if (fileList.contains(getFile(name))) {
			System.out.println("Fichier \"" + name + "\" deja existant.");
			return false;
		}

		// Ajout du fichier dans la structure de donnees specifique a la
		// commande list
		headerList.add(newFile.getHeader());
		// Ajout du fichier dans la liste des fichiers
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
		System.out.println("Demande de synchronisation...");
		return fileList;
	}

	@Override
	public File get(String name, byte[] checksum) throws RemoteException {
		// Recuperation du fichier cote server
		File file = getFile(name);

		// Fichier inexistant cote serveur
		if (file.equals(null)) {
			System.out.println("Fichier \"" + name + "\" inexistant cote serveur." );
			return null;
		}

		// Absence de checksum, envoie du fichier au client
		if (checksum == null) {
			System.out.println("Checksum non present.");
			System.out.println("Envoie de la version cote serveur.");
			return file;
		}
		
		// Checksum identique, inutile d'envoyer le fichier
		if (checksum.equals(file.getContent().getChecksum())) {
			return null;
		}
		
		return file;
	}

	@Override
	public boolean lock(String name, Integer clientId, byte[] checksum) throws RemoteException {
		// Check if the file exists
		File file = getFile(name);
		if (file == null) {
			System.err.println("Le fichier " + name + " n'existe pas.");
			return false;
		}

		// Check if the file is already locked
		if (filesLockers.containsKey(name)) {
			System.err.println("Le fichier " + name + " est deja verouille.");
			return false;
		}

		// Check the checksum of the local file and the remote file
		if (file.getContent().getChecksum().equals(checksum)) {
			// TODO demander si on doit faire ca ou non
			// En gros, on peut lock que si on a la version du serveur
			System.err.println("Checksum different, get a faire avant lock.");
			return false;
		}

		// Lock the file
		System.out.println("Verouillage du fichier en cours ...");
		file.getHeader().setLock(true);
		// Update header list to optimize network transfert
		updateHeaderList(true, file, clientId);
		// Update files/lockers hashmap
		filesLockers.put(name, clientId);

		System.out.println("Fichier " + name + " verouille par client " + clientId);
		return true;
	}

	@Override
	public boolean push(String name, byte[] content, Integer clientId) throws RemoteException {
		System.out.println("Essaie de push du fichier \"" + name + "\" par le client " + clientId + " ...");
		File file = getFile(name);

		// Check if the file exist
		if (file == null) {
			System.out.println("Fichier \"" + name + "\" inexistant cote serveur.");
			return false;
		}

		// Check if the file has been locked before the push
		Integer locker = filesLockers.get(name);
		if (locker == null) {
			System.out.println("Fichier \"" + name + "\" non verouille.");
			return false;
		}
		
		if (!locker.equals(clientId)) {
			System.out.println("Fichier deja verouille par le client : " + locker);
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
	 * @param name:
	 *            the name of the file
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
	 * @param locker:
	 *            true if we need to lock the file, false if unlock
	 * @param file:
	 *            the file to set
	 * @param clientId:
	 *            the id of the user
	 */
	private void updateHeaderList(boolean locker, File file, Integer clientId) {
		int index = headerList.indexOf(file.getHeader());
		headerList.get(index).setLock(true);
		headerList.get(index).setOwner(clientId);
	}
}
