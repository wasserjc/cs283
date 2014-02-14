

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MTServer {
	
	// modified code from lecture
	
	private static final int PORT = 3333;

	public static void main(String[] args) throws IOException {
		
		ServerSocket ss = new ServerSocket(PORT);
		System.out.println("MAIN: ServerSocket created");
		while (true) {
			Socket cs = ss.accept();
			new WorkerThread(cs).start();
		}
	}

}
