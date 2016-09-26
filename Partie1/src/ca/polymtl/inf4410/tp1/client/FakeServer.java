package ca.polymtl.inf4410.tp1.client;

/**
 * Class FakeServer. This class is done to see if normal local calls are also
 * influenced by the size of the arguments.
 * 
 * @author Jeremy
 * 
 */
public class FakeServer {
	/**
	 * Fake execute method, sum of 2 int.
	 * 
	 * @param a
	 * @param b
	 * @return a+b
	 */
	int execute(int a, int b) {
		return a + b;
	}

	/**
	 * Fake printByteArraySize method, just print the size of the arguments.
	 * 
	 * @param bytes
	 */
	public void printByteArraySize(byte[] bytes) {
		System.out.println("Size of the array in parameter: " + bytes.length);

	}
}
