package ca.polymtl.inf4410.tp1.server;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.polymtl.inf4410.tp1.shared.File;
import ca.polymtl.inf4410.tp1.shared.ServerInterface;

public class Server implements ServerInterface {

	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}
	
	private List<Integer> clientsId;
	private HashMap<String, Integer> filesLockers;

	public Server() {
		super();
		clientsId = new ArrayList<Integer>();
		filesLockers = new HashMap<String, Integer>();
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

	/*
	 * Methode accessible par RMI. Additionne les deux nombres passes en
	 * parametre.
	 */
	@Override
	public int execute(int a, int b) throws RemoteException {
		return a + b;
	}

	@Override
	public void print(String message) throws RemoteException {
		System.out.println(message);	
	}

	@Override
	public void printByteArraySize(byte[] bytes) throws RemoteException {
		System.out.println("Size of the array in parameter: " + bytes.length);
	}

	@Override
	public int generateClientId() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean create(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HashMap<String, Integer> list() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
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
