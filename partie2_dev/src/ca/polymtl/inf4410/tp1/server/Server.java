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
	public void print(String message) throws RemoteException {
		System.out.println(message);
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
		// Ajout du fichier dans la structure de donnees specifique a la commande list
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
		if (checksum == null) {
			System.out.println("Checksum non present.");
			System.out.println("Envoie de la version cote serveur.");
			return getFile(name);
		}
		// TODO faire du vrai code et rendre cette fonction robuste

		return null;
	}

	@Override
	public boolean lock(String name, Integer clientId, byte[] checksum) throws RemoteException {
		File file = getFile(name);
		
		if (file == null) {
			System.err.println("Le fichier " + name + " n'existe pas.");
			return false;
		}
		
		/* DEBUG ZONE */
		System.out.println("Checksum du client : " + checksum.toString());
		System.out.println("Filename indexOf(new File()) + get : " + file.getHeader().getName());
		System.out.println("Checksum associe : " + file.getContent().getChecksum().toString());
		System.out.println("Filename getFile(String) : " + getFile(name).getHeader().getName());
		System.out.println("Checksum associe : " + getFile(name).getContent().getChecksum().toString());
		/* END DEBUG ZONE */

		if (file.getContent().getChecksum().equals(checksum)) {
			// TODO demander si on doit faire ca ou non
			// En gros, on peut lock que si on a la version du serveur
			System.err.println("Checksum different, get a faire avant lock.");
			return false;
		}

		System.out.println("Verouillage du fichier en cours ...");
		file.getHeader().setLock(true);
		updateHeaderList(true, file, clientId);
		filesLockers.put(name, clientId);
		
		System.out.println("Fichier " + name + " verouille par client " + clientId);
		return true;
	}

	@Override
	public boolean push(String name, byte[] content, Integer clientId) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Returns the appropriate file according to the param name
	 * @param name: the name of the file
	 * @return the file 
	 */
	private File getFile(String name) {
		for (File file : fileList) {
			if (file.getHeader().getName().equals(name))
				return file;
		}
		return null;
	}

	/**
	 * Update the header list according to its parameters
	 * @param locker: true if we need to lock the file, false if unlock
	 * @param file: the file to set
	 * @param clientId: the id of the user
	 */
	private void updateHeaderList(boolean locker, File file, Integer clientId) {
		int index = headerList.indexOf(file.getHeader());
		headerList.get(index).setLock(true);
		headerList.get(index).setName(clientId.toString());
	}
}
