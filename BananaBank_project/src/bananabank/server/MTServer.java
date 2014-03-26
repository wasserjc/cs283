package bananabank.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MTServer {
	
	// modified code from lecture
	
	private static final int PORT = 3333;

	public static void main(String[] args) throws IOException {
		
		ArrayList<WorkerThread> threads = new ArrayList<WorkerThread>();
		BananaBank bank = new BananaBank("accounts.txt");
		ServerSocket ss = new ServerSocket(PORT);
		System.out.println("MAIN: ServerSocket created");
		
		try {
			while (true) {
				Socket cs = ss.accept();
				WorkerThread t = new WorkerThread(cs,ss,bank);
				t.start();
				threads.add(t);
			}
		} catch (IOException e) {
			for (WorkerThread worker : threads) {
				try {
					worker.join();
				} catch (InterruptedException e1) {}
			}
			// save current account info to file
			bank.save("accounts.txt");
			System.out.println("Server closing.");
		}
	}

}
