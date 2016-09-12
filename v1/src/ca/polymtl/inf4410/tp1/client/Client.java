package ca.polymtl.inf4410.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ca.polymtl.inf4410.tp1.shared.ServerInterface;

public class Client {
	public static void main(String[] args) {
		String distantHostname = null;
		Double powerOfTen = 0.;

		if (args.length > 0) {
			distantHostname = args[0];
			powerOfTen = Double.parseDouble(args[1]);
		}

		Client client = new Client(distantHostname, new byte[(int)Math.pow(10., powerOfTen)]);
		client.run();
	}

	FakeServer localServer = null; // Pour tester la latence d un appel de
									// fonction normal.
	private ServerInterface localServerStub = null;
	private ServerInterface distantServerStub = null;
	private byte[] bytesArrayArgument = null;

	/*public Client(String distantServerHostname) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		localServer = new FakeServer();
		localServerStub = loadServerStub("127.0.0.1");

		if (distantServerHostname != null) {
			distantServerStub = loadServerStub(distantServerHostname);
		}
	}*/
	
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

	private void run() {
		for(int i = 0; i<10; i++)
		{
			appelNormal();
				
			if (localServerStub != null) {
				appelRMILocal();
			}
	
			if (distantServerStub != null) {
				appelRMIDistant();
			}
		}
	}

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

	private void appelNormal() {
		long start = System.nanoTime();
		int result = localServer.execute(4, 7);
		long end = System.nanoTime();

		System.out.println("Temps ecoule appel normal: " + (end - start)
				+ " ns");
		System.out.println("Resultat appel normal: " + result);
	}

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
