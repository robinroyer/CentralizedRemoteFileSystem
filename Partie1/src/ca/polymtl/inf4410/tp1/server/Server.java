package ca.polymtl.inf4410.tp1.server;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import ca.polymtl.inf4410.tp1.shared.ServerInterface;

/**
 * Class Server. This class represents our servers. Instances of this class will
 * just wait for client calls.
 * 
 * @author Jeremy
 * 
 */
public class Server implements ServerInterface {

	/**
	 * Main entry of the program, create a server and run it.
	 * 
	 * @param args
	 *            unrequired
	 */
	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

	/**
	 * Default constructor
	 */
	public Server() {
		super();
	}

	/**
	 * Private method which run the server.
	 */
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
}
