package ca.polymtl.inf4410.tp1.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import ca.polymtl.inf4410.tp1.shared.File;
import ca.polymtl.inf4410.tp1.shared.Header;
import ca.polymtl.inf4410.tp1.shared.ServerInterface;

public class Client {

	private static final String FILE_PATH = ".user";
	private static String REMOTE_SERVER_IP = "132.207.12.200";
	private static int ID_LOCK_CANCEL = 10;
	private static int ID_PUSH_CANCEL = -10;
	private ServerInterface distantServerStub = null;

	public static void main(String[] args) throws IOException {
		Client client = new Client(REMOTE_SERVER_IP);

		String action = args[0];
		String filename = "";

		// TODO: Verification des arguments
		// TODO: Modification architecture pour run ? Surement moyen de faire
		// plus propre et plus court !
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
		boolean result = false;
		try {
			result = distantServerStub.create(filename);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
		if (result) {
			System.out.println("Fichier " + filename + " ajoute.");
		} else {
			System.err.println("Un probleme a eu lieu pendant la creation du fichier \"" + filename + "\".");
		}
	}

	/**
	 * Private method to display the list
	 */
	private void displayList() {
		try {
			ArrayList<Header> list = distantServerStub.list();
			if (list.isEmpty()) {
				System.out.println("Il n'y a pas encore de fichier dans la liste !");
			} else {
				// Display all the elemen of the list
				for (Header h : list) {
					System.out.println(h);
				}
			}
		} catch (RemoteException e) {
			System.out.println("Erreur : " + e.getMessage());
		}
	}

	/**
	 * Private method to push a local file onto the server
	 * 
	 * @param filename:
	 *            the name of the file
	 */
	private void pushFile(String filename) {
		// Get the local client id
		int clientId = getUserId();

		// Get the local file
		byte[] content = null;
		try {
			content = getLocalFile(filename);
		} catch (NoSuchFileException e) {
			// Canceled if the file does not exist locally
			System.out.println("Fichier non detecte en local.");
			System.out.println("Annulation de l'operation de televersement du fichier " + filename + ".");
			System.exit(ID_PUSH_CANCEL);
		}

		try {
			distantServerStub.push(filename, content, clientId);
		} catch (RemoteException e) {
			System.err.println("Erreur RMI :" + e.getMessage());
		}
	}

	/**
	 * Lock a file on the remte server
	 * 
	 * @param filename:
	 *            the name of the file to lock
	 */
	private void lockFile(String filename) {
		// Get the local client id
		int clientId = getUserId();

		// Try to get the local file
		byte[] data = null;
		try {
			data = getLocalFile(filename);
		} catch (NoSuchFileException e) {
			// Abort if the file is not present localy (avoid a lock
			// unloackable)
			System.out.println("Fichier non detecte en local.");
			System.out.println("Annulation de l'operation de verouillage.");
			System.exit(ID_LOCK_CANCEL);
		}

		// Compute the local checksum
		byte[] checksum = computeChecksum(data);

		// Lock the file
		boolean result = false;
		try {
			result = distantServerStub.lock(filename, clientId, checksum);
		} catch (RemoteException e) {
			System.err.println("Erreur RMI : " + e.getMessage());
		}

		if (result) {
			System.out.println("Fichier " + filename + " verouille.");
		} else {
			System.out.println("Fichier deja verouille par un autre utilisateur.");
		}

	}

	private void getFile(String filename) {
		byte[] file = null;
		byte[] checksum = null;

		try {
			file = getLocalFile(filename);
			checksum = computeChecksum(file);
		} catch (NoSuchFileException e) {
			System.out.println("Version locale du fichier \"" + filename + "\" inexistante.");
		}

		File result = null;

		try {
			System.out.println("Recuperation de la version du serveur.");
			result = distantServerStub.get(filename, checksum);
			System.out.println("Fichier " + result.getHeader().getName() + " recupere.");
		} catch (RemoteException e) {
			System.err.println("Erreur RMI getFile(" + filename + "," + checksum + ").");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("Le fichier " + filename + " n'existe pas cote serveur.");
			System.err.println("Executer ./client list pour voir la liste des fichiers disponibles.");
		}

		try {
			storeLocalFile(result);
		} catch (IOException e) {
			System.err.println("IOE Exception : " + e.getMessage());
		}
	}

	private void synchroLocalDirectory() {
		ArrayList<File> results = null;

		try {
			System.out.println("Synchronisaion des fichiers avec le serveur.");
			results = distantServerStub.syncLocalDir();
			System.out.println("Fichier recuperes.");
		} catch (RemoteException e) {
			System.err.println("Erreur RMI synchroLocalDirectory()");
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < results.size(); i++) {
				storeLocalFile(results.get(i));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	private Integer getUserId() {

		Integer id = new Integer(-1);
		try {
			java.io.File f = new java.io.File(FILE_PATH);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			line = line.substring(line.lastIndexOf("=") + 1).trim();

			br.close();
			fr.close();

			if (!line.isEmpty()) {
				id = Integer.parseInt(line);
				System.out.println("Vous etes deja l'utilisateur : " + id);
			} else {
				id = new Integer(distantServerStub.generateClientId());
				storeUserId(id);
				System.out.println("Demande d'un nouvel id aupres du serveur ...");
				System.out.println("Vous etes l'utilisateur :" + id);
			}
		} catch (FileNotFoundException exception) {
			try {
				id = new Integer(distantServerStub.generateClientId());
				storeUserId(id);
				System.out.println("Demande d'un nouvel id aupres du serveur ...");
				System.out.println("Vous etes l'utilisateur :" + id);
			} catch (RemoteException e) {
				System.err.println("Remote exception : " + e);
			} catch (IOException e) {
				System.err.println("Erreur acces au fichier : " + e);
			}
		} catch (RemoteException e) {
			System.err.println("Erreur: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Erreur lors de la lecture : " + e.getMessage());
		} catch (NumberFormatException e) {
			System.err.println("Fichier d'id corrompu : " + e.getMessage());
		}

		return id;
	}

	/**
	 * Store the client id into a file to recognize the client
	 * 
	 * @param id
	 *            the id of the client
	 * @throws IOException
	 */
	private void storeUserId(Integer id) throws IOException {

		String stringToStore = "id=" + id;
		java.io.File userFile = new java.io.File(FILE_PATH);
		if (!userFile.exists()) {
			userFile.createNewFile();
		}

		try (FileWriter userFileWritter = new FileWriter(userFile, false)) {
			userFileWritter.write(stringToStore);
		}
	}

	private byte[] getLocalFile(String filename) throws NoSuchFileException {
		byte[] file = null;
		try {
			file = Files.readAllBytes(Paths.get(filename));
		} catch (NoSuchFileException e) {
			// TODO savoir quoi faire dans ce cas
			throw new NoSuchFileException("Fichier inexistant en local.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
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

	private void storeLocalFile(File file) throws FileNotFoundException, IOException {
		FileOutputStream stream = new FileOutputStream(file.getHeader().getName());
		stream.write(file.getContent().getContent());
		stream.close();
	}
}
