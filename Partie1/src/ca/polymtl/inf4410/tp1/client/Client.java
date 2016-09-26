package ca.polymtl.inf4410.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ca.polymtl.inf4410.tp1.shared.ServerInterface;

/**
 * Classe Client. In this project this class represents the client side. This
 * program will try to connect itself to the remote server. This program is done
 * to test the different performances linked with RMI.
 * 
 * @author Jeremy
 * 
 */
public class Client {

	/**
	 * Const power of ten.
	 */
	private static final double POWER_OF_TEN = 10.;

	/**
	 * Invalid arguments error code.
	 */
	private static final int INVALID_ARGUMENTS = -10;

	/**
	 * The fake local server instance (normal call)
	 */
	private FakeServer localServer = null;

	/**
	 * The local server instance (RMI local call)
	 */
	private ServerInterface localServerStub = null;

	/**
	 * The remote server instance (RMI classic call)
	 */
	private ServerInterface distantServerStub = null;

	/**
	 * The array of bytes to try the influence of arguments size on RMI call
	 */
	private byte[] bytesArrayArgument = null;

	/**
	 * Main client program, run the client
	 * 
	 * @param args
	 *            args[0] must be the ip of the distant server and args[1] must
	 *            be a number between 0 and 8. It represents the 10^x bytes
	 *            which will be use to call the remote method to the server.
	 */
	public static void main(String[] args) {
		String distantHostname = null;
		Double powerOfTen = 0.;

		// Little code to check arguments
		if (args.length == 2) {
			distantHostname = args[0];
			powerOfTen = Double.parseDouble(args[1]);
		} else {
			System.err.println("Le programme a besoin de deux arguments :");
			System.err
					.println("L'IP du serveur distant et la puissance de 10 que vous souhaitez passe en argugment.");
			System.err.println("Call: ./client [IP] [puissance_de_dix]");
			System.exit(INVALID_ARGUMENTS);
		}

		// Quick check for the second argument
		if (powerOfTen < 0) {
			System.err.println("Le deuxieme argument doit être positif.");
			System.exit(INVALID_ARGUMENTS);
		}

		// Creation of the custom client
		Client client = new Client(distantHostname, new byte[(int) Math.pow(
				POWER_OF_TEN, powerOfTen)]);
		client.run();
	}

	/**
	 * Constructor with
	 * 
	 * @param distantServerHostname
	 *            the IP of the remote server
	 */
	public Client(String distantServerHostname) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		localServer = new FakeServer();
		localServerStub = loadServerStub("127.0.0.1");

		if (distantServerHostname != null) {
			distantServerStub = loadServerStub(distantServerHostname);
		}
	}

	/**
	 * Constructor with:
	 * 
	 * @param distantServerHostname
	 *            the IP of the remote server
	 * @param bytes
	 *            a table of bytes
	 */
	public Client(String distantServerHostname, byte[] bytes) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		localServer = new FakeServer();
		localServerStub = loadServerStub("127.0.0.1");
		bytesArrayArgument = bytes;

		if (distantServerHostname != null) {
			distantServerStub = loadServerStub(distantServerHostname);
		}
	}

	/**
	 * Private method which will run the client. The goal is to call each
	 * different server instance to see the difference of performances.
	 */
	private void run() {
		// Normal call
		appelNormal();

		// Local RMI call
		if (localServerStub != null) {
			appelRMILocal();
		}

		// Remote RMI call
		if (distantServerStub != null) {
			appelRMIDistant();
		}
	}

	/**
	 * Private method to load servers configuration.
	 * 
	 * @param hostname
	 * @return
	 */
	private ServerInterface loadServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
			stub = (ServerInterface) registry.lookup("server");
		} catch (NotBoundException e) {
			System.out.println("Erreur: Le nom  " + e.getMessage()
					+ "  n est pas defini dans le registre.");
		} catch (AccessException e) {
			System.out.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}

		return stub;
	}

	/**
	 * Private method for the normal local call
	 */
	private void appelNormal() {
		long start = System.nanoTime();
		int result = localServer.execute(4, 7);
		localServer.printByteArraySize(bytesArrayArgument);
		long end = System.nanoTime();

		System.out.println("Temps ecoule appel normal: " + (end - start)
				+ " ns");
		System.out.println("Resultat appel normal: " + result);
	}

	/**
	 * Private method for the RMI local call
	 */
	private void appelRMILocal() {
		try {
			long start = System.nanoTime();
			int result = localServerStub.execute(4, 7);
			localServerStub.printByteArraySize(bytesArrayArgument);
			long end = System.nanoTime();

			System.out.println("Temps ecoule appel RMI local: " + (end - start)
					+ " ns");
			System.out.println("Resultat appel RMI local: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	/**
	 * Private method for the remote RMI call
	 */
	private void appelRMIDistant() {
		try {
			long start = System.nanoTime();
			int result = distantServerStub.execute(4, 7);
			distantServerStub.printByteArraySize(bytesArrayArgument);
			long end = System.nanoTime();

			System.out.println("Temps ecoule appel RMI distant: "
					+ (end - start) + " ns");
			System.out.println("Resultat appel RMI distant: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}
}
