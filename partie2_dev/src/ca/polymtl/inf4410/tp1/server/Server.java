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
			ServerInterface stub = (ServerInterface) UnicastRemoteObject
					.exportObject(this, 0);

			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("server", stub);
			System.out.println("Server ready.");
		} catch (ConnectException e) {
			System.err
					.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lance ?");
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean create(String name) throws RemoteException {

		System.out.println("Creation du fichier " + name + " ...");
		File newFile = new File(name, null);

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
	public File get(String name, Integer checksum) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean lock(String name, Integer clientId, Integer checksum) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean push(String name, byte[] content, Integer clientId) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
}
