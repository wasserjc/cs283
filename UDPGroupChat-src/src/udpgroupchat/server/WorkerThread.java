package udpgroupchat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
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
			
			try {
				System.out.println("ACK not received.");
				// need to resend the message that wasn't ACKED
				ClientEndPoint client = Server.clientEndPoints.get(id);
				try {
					send(client.getMessages().peek(),client.address,client.port);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch(Exception e) {
				onBadRequest("");
			}

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

		if (payload.startsWith("UNREGISTER")) {
			onUnregisterRequested(payload);
			return;
		}

		if (payload.startsWith("SEND")) {
			onSendRequested(payload);
			return;
		}

		//
		// implement other request handlers here...
		//
		
		if (payload.startsWith("JOIN")) {
			onJoinRequest(payload);
			return;
		}
		
		if (payload.startsWith("POLL")) {
			onPollRequest(payload);
			return;
		}
		
		if (payload.startsWith("ACK")) {
			onAckReceived(payload);
			return;
		}
		
		if (payload.startsWith("SHUTDOWN")) {
			onShutdownRequest(payload);
			return;
		}
		
		if (payload.startsWith("LISTGROUPS")) {
			onGroupListRequest(payload);
			return;
		}
		
		if (payload.startsWith("QUIT")) {
			onQuitRequest(payload);
			return;
		}

		// if we got here, it must have been a bad request, so we tell the
		// client about it
		onBadRequest(payload);
	}

	private void onPollRequest(String payload) {
				
		try {
			StringTokenizer st = new StringTokenizer(payload);
			st.nextToken(); // this is "POLL" token
			int id = Integer.parseInt(st.nextToken());

			ClientEndPoint client = Server.clientEndPoints.get(id);
			if (!client.getMessages().isEmpty()) {
				// attempt to send the message
				send(client.getMessages().peek(),client.address,client.port);
				// set timeout of 10 sec - then expires - send again?
				Server.timer = new Timer();
				Server.timer.schedule(new SendTask(id), 10000);
			}
						
			else {
				try {
					send("Server: Successfully POLLED\n", this.rxPacket.getAddress(),
							this.rxPacket.getPort());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			onBadRequest(payload);
		}
	}
	
	private void onAckReceived(String payload) {
		Server.timer.cancel();
		System.out.println("ACK Received.");
		StringTokenizer st = new StringTokenizer(payload);
		st.nextToken(); // this is "POLL" token
		int id = Integer.parseInt(st.nextToken());
		
		ClientEndPoint client = Server.clientEndPoints.get(id);
		client.pendingMsgs.poll();
		String toFinishPoll = "POLL " + id;
		onPollRequest(toFinishPoll);

	}

	private void onJoinRequest(String payload) {
		try {
			StringTokenizer st = new StringTokenizer(payload);
			st.nextToken(); // this was the "JOIN" token
			String groupName = st.nextToken();
			int id = Integer.parseInt(st.nextToken());
			
			ClientEndPoint client = Server.clientEndPoints.get(id);
			
			if (client == null) {
				onBadRequest("");
				return;
			}
			
			if (!Server.groups.containsKey(groupName)) {
				Server.groups.put(groupName, new HashSet<ClientEndPoint>());
			}
			Server.groups.get(groupName).add(client);
			
			// tell client we're OK
			try {
				send("Server: JOINED\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
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
				Server.clientEndPoints.put(id, new ClientEndPoint(address,port,name));
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

	private void onUnregisterRequested(String payload) {
		
		StringTokenizer st = new StringTokenizer(payload);
		st.nextToken(); // this was the "UNREGISTER" token
		int id = Integer.parseInt(st.nextToken());
		
		ClientEndPoint client = Server.clientEndPoints.get(id);
		
		// check if client is in the set of registered clientEndPoints
		if (client != null) {
			// yes, remove it
			Server.clientEndPoints.remove(id);
			// clear all pending messages for that client
			client.removeMessages();
			
			// remove ClientEndPoint from all groups where it is a current member
			for (Set<ClientEndPoint> set : Server.groups.values()) {
				set.remove(client);
			}
			
			try {
				send("Server: UNREGISTERED\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// no, send back a message
			try {
				send("Server: CLIENT NOT REGISTERED\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void onSendRequested(String payload) {
		
		try {
			StringTokenizer st = new StringTokenizer(payload);
			st.nextToken(); // this was the "SEND" token
			String groupName = st.nextToken();
			int id = Integer.parseInt(st.nextToken());
			String senderName = Server.clientEndPoints.get(id).getName();
			
			String message = "";
			while (st.hasMoreTokens()) message += (" " + st.nextToken());
		
			for (ClientEndPoint clientEndPoint : Server.groups.get(groupName)) {
				clientEndPoint.addMessage("(" + groupName + ") " + senderName + ": " + message + "\n");
			}
			try {
				send("Server: SENT " + groupName + message + "\n", this.rxPacket.getAddress(),
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
	
	private void onGroupListRequest(String payload) {
		try {
			try {
				StringTokenizer st = new StringTokenizer(payload);
				st.nextToken(); // this was the "SEND" token
				int id = Integer.parseInt(st.nextToken());
				
				send("Server: Current Groups:\n",this.rxPacket.getAddress(),
						this.rxPacket.getPort());
				for (String group : Server.groups.keySet()) {
					if (Server.groups.get(group).contains(Server.clientEndPoints.get(id)))
						send(group + "\n", this.rxPacket.getAddress(),
							this.rxPacket.getPort());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			onBadRequest(payload);
		}
	}
	
	private void onQuitRequest(String payload) {
		try {
			StringTokenizer st = new StringTokenizer(payload);
			st.nextToken(); // this was the "SEND" token
			String groupName = st.nextToken();
			int id = Integer.parseInt(st.nextToken());
			
			// remove the client from the given group
			Server.groups.get(groupName).remove(Server.clientEndPoints.get(id));
			if (Server.groups.get(groupName).isEmpty())
				Server.groups.remove(groupName);
			
			// tell client they are removed
			try {
				send("Server: QUIT GROUP " + groupName + "\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			onBadRequest(payload);
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
