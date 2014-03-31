

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;


public class Server {

	// constants
	public static final int DEFAULT_PORT = 20000;
	public static final int MAX_PACKET_SIZE = 512;

	// port number to listen on
	protected int port;

	// Map of Integer IDs to ClientEndPoints
	// note that this is synchronized, i.e. safe to be read/written from
	// concurrent threads without additional locking
	protected final static Map<Integer,ClientEndPoint> clientEndPoints =
			Collections.synchronizedMap(new HashMap<Integer,ClientEndPoint>());
	
	// Mapping of group names to the set of ClientEndPoints
	// in that particular group
	// uses a synchronized map so safe to read/write from concurrent threads
	// without additional locking
	protected final static Map<String,Set<ClientEndPoint>> groups = 
			Collections.synchronizedMap(new HashMap<String,Set<ClientEndPoint>>());
	
	
	protected final static Queue<ClientEndPoint> clientQueue = new LinkedBlockingQueue<ClientEndPoint>();
	int nextGameId = 1;

	List<WorkerThread> threads = new ArrayList<WorkerThread>();
	
	protected static Timer timer;


	// constructor
	Server(int port) {
		this.port = port;
	}

	// start up the server
	public void start() {
		DatagramSocket socket = null;
		try {
			// create a datagram socket, bind to port port. See
			// http://docs.oracle.com/javase/tutorial/networking/datagrams/ for
			// details.

			socket = new DatagramSocket(port);

			// receive packets in an infinite loop
			while (true) {
				// create an empty UDP packet
				byte[] buf = new byte[Server.MAX_PACKET_SIZE];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				// call receive (this will poulate the packet with the received
				// data, and the other endpoint's info)
				socket.receive(packet);
				// start up a worker thread to process the packet (and pass it
				// the socket, too, in case the
				// worker thread wants to respond)
				WorkerThread t = new WorkerThread(packet, socket);
				threads.add(t);
				t.start();
			}
		} catch (IOException e) {
			
			for (WorkerThread worker : threads) {
				try {
					worker.join();
				} catch (InterruptedException e1) {}
			}
			System.out.println("Server closing.");

			
		} finally {
			if (socket != null && !socket.isClosed())
				socket.close();
		}
	}

	// main method
	public static void main(String[] args) {
		int port = Server.DEFAULT_PORT;

		// check if port was given as a command line argument
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.out.println("Invalid port specified: " + args[0]);
				System.out.println("Using default port " + port);
			}
		}

		// instantiate the server
		Server server = new Server(port);

		System.out
				.println("Starting server. Connect with netcat (nc -u localhost "
						+ port
						+ ") or start multiple instances of the client app to test the server's functionality.");

		// start it
		server.start();

	}

}
