package ca.polymtl.inf8480.tp1.client;
import java.util.ArrayList;

public class FakeServer {
	int execute(int a, int b, byte[] c) {
		return a + b;
	}
	
	int test(byte[] bytes) {
		return bytes.length;
	}
}
