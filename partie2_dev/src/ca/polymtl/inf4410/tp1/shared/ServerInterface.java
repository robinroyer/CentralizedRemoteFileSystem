package ca.polymtl.inf4410.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {
	// Méthodes implémentées pour la partie 1 
	void print(String message) throws RemoteException;
	
	// Méthodes implémentées pour la partie 2
	int generateClientId() throws RemoteException;
	boolean create(String name) throws RemoteException;
	ArrayList<Header> list() throws RemoteException;
	ArrayList<File> syncLocalDir() throws RemoteException;
	File get(String name, Integer checksum) throws RemoteException;
	boolean lock(String name, Integer clientId, Integer checksum) throws RemoteException;
	boolean push(String name, byte[] content, Integer clientId) throws RemoteException;
}
