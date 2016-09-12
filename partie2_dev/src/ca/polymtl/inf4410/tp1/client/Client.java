package ca.polymtl.inf4410.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ca.polymtl.inf4410.tp1.shared.ServerInterface;

public class Client {

	private static String REMOTE_SERVER_IP = "132.207.12.200";
	private ServerInterface distantServerStub = null;
	private byte[] bytesArrayArgument = null;

	public static void main(String[] args) {
		/*
		 * String distantHostname = null; Double powerOfTen = 0.;
		 * 
		 * if (args.length > 0) { distantHostname = args[0]; powerOfTen =
		 * Double.parseDouble(args[1]); }
		 * 
		 * Client client = new Client(distantHostname, new
		 * byte[(int)Math.pow(10., powerOfTen)]); client.run();
		 */
		Client client = new Client(REMOTE_SERVER_IP);
		client.run();
	}

	public Client(String distantServerHostname) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		if (distantServerHostname != null) {
			distantServerStub = loadServerStub(distantServerHostname);
		}
	}

//	public Client(String distantServerHostname, byte[] bytes) {
//		super();
//
//		if (System.getSecurityManager() == null) {
//			System.setSecurityManager(new SecurityManager());
//		}
//
//		bytesArrayArgument = bytes;
//
//		if (distantServerHostname != null) {
//			distantServerStub = loadServerStub(distantServerHostname);
//		}
//	}

	private void run() {
		if (distantServerStub != null) {
			appelRMIDistant();
		}
	}

	private ServerInterface loadServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
			stub = (ServerInterface) registry.lookup("server");
		} catch (NotBoundException e) {
			System.out.println("Erreur: Le nom  " + e.getMessage() + "  n est pas defini dans le registre.");
		} catch (AccessException e) {
			System.out.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}

		return stub;
	}

	private void appelRMIDistant() {
		try {
			long start = System.nanoTime();
			int result = distantServerStub.execute(4, 7);
			distantServerStub.printByteArraySize(bytesArrayArgument);
			long end = System.nanoTime();

			System.out.println("Temps ecoule appel RMI distant: " + (end - start) + " ns");
			System.out.println("Resultat appel RMI distant: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}
}
