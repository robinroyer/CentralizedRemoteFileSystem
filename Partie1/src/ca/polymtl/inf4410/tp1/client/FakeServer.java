package ca.polymtl.inf4410.tp1.client;

public class FakeServer {
	int execute(int a, int b) {
		return a + b;
	}

	public void printByteArraySize(byte[] bytes) {
		System.out.println("Size of the array in parameter: " + bytes.length);
		
	}
}
