package ca.polymtl.inf4410.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface ServerInterface. This interface is required for RMI implementation.
 * You will find all the signature of the callable methods of the server.
 * Implementation are done in the server side.
 * 
 * @author jewim
 * 
 */
public interface ServerInterface extends Remote {
	/**
	 * Execute commande. Sum 2 int on the server side and return the result.
	 * 
	 * @param a
	 * @param b
	 * @return a+b
	 * @throws RemoteException
	 */
	int execute(int a, int b) throws RemoteException;

	/**
	 * Simple command to print a message.
	 * 
	 * @param message
	 * @throws RemoteException
	 */
	void print(String message) throws RemoteException;

	/**
	 * The command used to benchmark the different performances. It just takes a
	 * parameter to test the network call performances through RMI. The
	 * implentation just print a message with the size of this parameter.
	 * 
	 * @param bytes
	 * @throws RemoteException
	 */
	void printByteArraySize(byte[] bytes) throws RemoteException;
}
