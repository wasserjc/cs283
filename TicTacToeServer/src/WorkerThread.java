

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.StringTokenizer;
import java.util.TimerTask;

public class WorkerThread extends Thread {

	private DatagramPacket rxPacket;
	private DatagramSocket socket;
	
	class SendTask extends TimerTask {
		int id;
		SendTask(int id_) {
			id = id_;
		}
		
		public void run() {
			

		}
	}

	public WorkerThread(DatagramPacket packet, DatagramSocket socket) {
		this.rxPacket = packet;
		this.socket = socket;
	}

	@Override
	public void run() {
		// convert the rxPacket's payload to a string
		String payload = new String(rxPacket.getData(), 0, rxPacket.getLength())
				.trim();

		// dispatch request handler functions based on the payload's prefix

		if (payload.startsWith("REGISTER")) {
			onRegisterRequested(payload);
			return;
		}

		//
		// implement other request handlers here...
		//
		
		if (payload.startsWith("JOIN")) {
			onJoinRequest(payload);
			return;
		}
		
		if (payload.startsWith("PLAY")) {
			onPlayRequest(payload);
			return;
		}
				
		if (payload.startsWith("ACK")) {
			onAckReceived(payload);
			return;
		}
		
		if (payload.startsWith("ENDGAME")) {
			onEndGameRequest(payload);
			return;
		}
		if (payload.startsWith("SHUTDOWN")) {
			onShutdownRequest(payload);
			return;
		}
				
		// if we got here, it must have been a bad request, so we tell the
		// client about it
		onBadRequest(payload);
	}

	
	private void onAckReceived(String payload) {
		Server.timer.cancel();
		System.out.println("ACK Received.");
		StringTokenizer st = new StringTokenizer(payload);
		st.nextToken(); // this is "POLL" token
		int id = Integer.parseInt(st.nextToken());
		
		// figure out what we need to do here
	}
	

	private void onJoinRequest(String payload) {
		try {
			StringTokenizer st = new StringTokenizer(payload);
			st.nextToken(); // this was the "JOIN" token
			int id = Integer.parseInt(st.nextToken());
			
			ClientEndPoint client = Server.clientEndPoints.get(id);
			
			if (client == null) {
				onBadRequest("");
				return;
			}
						
			
			// tell client we're OK
			try {
				send("Server: JOINED\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (Server.clientQueue.size() == 0) {
				// add the client to the client queue
				Server.clientQueue.add(client);
			}
			else {
				ClientEndPoint opponent = Server.clientQueue.poll();
				client.setOppId(opponent.getId());
				opponent.setOppId(client.getId());
				// alert clients that game is starting
				// gives each client GAMESTART < X || O > <opp_id> <opp_name>
				try {
					send("GAMESTART X " + opponent.getId() + " "
							+ opponent.getName() + "\n", this.rxPacket.getAddress(),
							this.rxPacket.getPort());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					send("GAMESTART O " + client.getId() + " "
							+ client.getName() + "\n", opponent.getAddress(),
							opponent.getPort());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			
			
		} catch (Exception e) {
			onBadRequest(payload);
		}
		
		
	}

	// send a string, wrapped in a UDP packet, to the specified remote endpoint
	public void send(String payload, InetAddress address, int port)
			throws IOException {
		DatagramPacket txPacket = new DatagramPacket(payload.getBytes(),
				payload.length(), address, port);
		this.socket.send(txPacket);
		
	}

	private void onRegisterRequested(String payload) {
		
		try {
			// get the address of the sender from the rxPacket
			InetAddress address = this.rxPacket.getAddress();
			// get the port of the sender from the rxPacket
			int port = this.rxPacket.getPort();

			StringTokenizer st = new StringTokenizer(payload);
			st.nextToken(); // this was the "REGISTER" token
			int id = Integer.parseInt(st.nextToken());
			
			String name = st.nextToken();
			
			// create a client object, and put it in the map that assigns names
			// to client objects
			// if Client ID is already registered, update address and port
			// to allow for survival of IP address change
			ClientEndPoint client = Server.clientEndPoints.get(id);
			if (client != null) {
				client.address = address;
				client.port = port;
			}
			else {
				Server.clientEndPoints.put(id, new ClientEndPoint(address,port,name,id));
			}
			// note that calling clientEndPoints.add() with the same endpoint info
			// (address and port)
			// multiple times will not add multiple instances of ClientEndPoint to
			// the set, because ClientEndPoint.hashCode() is overridden. See
			// http://docs.oracle.com/javase/7/docs/api/java/util/Set.html for
			// details.

			// tell client we're OK
			try {
				send("Server: REGISTERED " + name + "\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			onBadRequest(payload);
		}
	}

	
	private void onShutdownRequest(String payload) {
		
		try {
			send("Server: SHUTDOWN REQUEST RECEIVED\n", this.rxPacket.getAddress(),
					this.rxPacket.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Client requested shutdown.");
		// close the Datagram Socket
		// this will trigger the IOException in main Server thread
		this.socket.close();
	}

		
	private void onPlayRequest(String payload) {
		StringTokenizer st = new StringTokenizer(payload);
		st.nextToken(); // this was the "SEND" token
		int myId = Integer.parseInt(st.nextToken());
		int oppId = Integer.parseInt(st.nextToken());
		int index = Integer.parseInt(st.nextToken());
		
		ClientEndPoint client = Server.clientEndPoints.get(myId);
		ClientEndPoint opponent = Server.clientEndPoints.get(oppId);

		// send move to the opponent client
		try {
			send("PLAY " + client.getId() + " " +
					index + "\n", opponent.getAddress(),
					opponent.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	private void onEndGameRequest(String payload) {
		StringTokenizer st = new StringTokenizer(payload);
		st.nextToken(); // this was the "SEND" token
		int myId = Integer.parseInt(st.nextToken());
		int oppId = Integer.parseInt(st.nextToken());

		ClientEndPoint client = Server.clientEndPoints.get(myId);
		ClientEndPoint opponent = Server.clientEndPoints.get(oppId);
		
		// send ENDGAME to the opponent client
		try {
			send("ENDGAME " + client.getId() +
					 "\n", opponent.getAddress(),
					opponent.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void onBadRequest(String payload) {
		try {
			send("Server: BAD REQUEST\n", this.rxPacket.getAddress(),
					this.rxPacket.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
