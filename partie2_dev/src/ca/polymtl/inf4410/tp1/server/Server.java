package ca.polymtl.inf4410.tp1.server;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
		System.out.println("Creation du fichier " + name + " ...");
		File newFile = new File(name);

		System.out.println("Fichier " + name + " ajoute.");
		headerList.add(newFile.getHeader());
		return fileList.add(newFile);
	}

	@Override
	public ArrayList<Header> list() throws RemoteException {
		System.out.println("Demande de la liste ...");
		return headerList;
	}

	@Override
	public ArrayList<File> syncLocalDir() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
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

	private File getFile(String name) {
		for(File file : fileList) {
			if (file.getHeader().getName().equals(name))
				return file;
		}
		return null;
	}

	@Override
	public boolean lock(String name, Integer clientId, byte[] checksum) throws RemoteException {
		File file = null;

		if ((file = fileList.get(fileList.indexOf(name))) == null) {
			System.err.println("Le fichier " + name + " n'existe pas.");
			return false;
		}

		if (file.getContent().getChecksum() != checksum) {
			// TODO demander si on doit faire ca ou non
			// En gros, on peut lock que si on a la version du serveur
			System.err.println("Checksum different, get a faire avant lock.");
			return false;
		}

		System.out.println("Verouillage du fichier en cours ...");
		file.getHeader().setLock(true);
		System.out.println("Fichier " + name + " verouille par client " + clientId);
		return true;
	}

	@Override
	public boolean push(String name, byte[] content, Integer clientId) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	private byte[] computeChecksum(byte[] file) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return md.digest(file);
	}
}
