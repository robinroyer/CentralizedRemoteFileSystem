package ca.polymtl.inf4410.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import ca.polymtl.inf4410.tp1.shared.Header;
import ca.polymtl.inf4410.tp1.shared.ServerInterface;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Client {

	private static final String FILE_PATH= ".user";
	private static String REMOTE_SERVER_IP = "132.207.12.200";
	private ServerInterface distantServerStub = null;

	public static void main(String[] args) throws IOException {
		Client client = new Client(REMOTE_SERVER_IP);

		String action = args[0];
		String filename = "";

		// TODO: Verification des arguments
		// TODO: Modification architecture pour run ? Surement moyen de faire
		// plus propre
		// et plus court !
		switch (action) {
		case "create":
			filename = checkFirstArgument(args);
			client.createFile(filename);
			break;
		case "list":
			client.displayList();
			break;
		case "syncLocalDir":
			client.synchroLocalDirectory();
			break;
		case "get":
			filename = checkFirstArgument(args);
			client.getFile(filename);
			break;
		case "lock":
			filename = checkFirstArgument(args);
			client.lockFile(filename);
			break;
		case "push":
			filename = checkFirstArgument(args);
			client.pushFile(filename);
			break;
		}
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

	// public Client(String distantServerHostname, byte[] bytes) {
	// super();
	//
	// if (System.getSecurityManager() == null) {
	// System.setSecurityManager(new SecurityManager());
	// }
	//
	// bytesArrayArgument = bytes;
	//
	// if (distantServerHostname != null) {
	// distantServerStub = loadServerStub(distantServerHostname);
	// }
	// }

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

	private void createFile(String filename) {
		try {
			distantServerStub.create(filename);
			System.out.println("Fichier " + filename + " ajoute.");
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	private void displayList() {
		try {
			ArrayList<Header> list = distantServerStub.list();
			for (Header h : list) {
				System.out.println(h);
			}
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	private void pushFile(String filename) {
		// TODO Auto-generated method stub

	}

	private void lockFile(String filename) throws IOException {
		// TODO Auto-generated method stub
//                System.out.println(getUserId());
                storeUserId(4);

	}

	private void getFile(String filename) {
		// TODO Auto-generated method stub

	}

	private void synchroLocalDirectory() {
		// TODO Auto-generated method stub

	}
	
	private static String checkFirstArgument(String[] args) {
		String fileName = args[1];
		try {
			if (fileName == null) {
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid argument: please check readme.txt");
		}
		return fileName;
	}

	private Integer getUserId() throws IOException {

		Integer id = new Integer(-1);
		try{
                    java.io.File f = new java.io.File (FILE_PATH);
		    FileReader fr = new FileReader (f);
		    BufferedReader br = new BufferedReader (fr);
		    String line = br.readLine();
                    line = line.substring(line.lastIndexOf("=")).trim();

                    br.close();
                    fr.close();


                    if (!line.isEmpty()) {
                            id = Integer.parseInt(line);
                    }
                    else{
                            id = new Integer(distantServerStub.generateClientId());
                            storeUserId(id);
                    }
		}
		catch(FileNotFoundException exception){
			System.out.println ("Le fichier n'a pas ete trouve");
			id = new Integer(distantServerStub.generateClientId());
			storeUserId(id);
		}
		catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
		catch (IOException exception){
                    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
                }
                catch(NumberFormatException e){
                    System.out.println ("Fichier d'id corrompu : " + e.getMessage());
                }

		return id;
	}

	private void storeUserId(Integer id) throws IOException {

		String stringToStore = "id="+ id;
		java.io.File userFile = new java.io.File(FILE_PATH);
                if (! userFile.exists()) {
                        userFile.createNewFile();
                }
                
                try (FileWriter userFileWritter = new FileWriter(userFile, false)) {
                    userFileWritter.write(stringToStore);
                }
	}
}
