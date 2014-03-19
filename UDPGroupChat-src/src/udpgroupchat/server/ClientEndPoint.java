package udpgroupchat.server;

import java.net.InetAddress;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientEndPoint {
	protected InetAddress address;
	protected int port;
	protected Queue<String> pendingMsgs = new LinkedBlockingQueue<String>();
	protected String name;
	
	public ClientEndPoint(InetAddress addr, int port, String name) {
		this.address = addr;
		this.port = port;
		this.name = name;
	}

	@Override
	public int hashCode() {
		// the hashcode is the exclusive or (XOR) of the port number and the hashcode of the address object
		return this.port ^ this.address.hashCode();
	}
	
	public void addMessage(String message) {
		pendingMsgs.add(message);
	}
	
	public Queue<String> getMessages() {
		return pendingMsgs;
	}
	
	public void removeMessages() {
		pendingMsgs.clear();
	}
	
	public String getName() {
		return name;
	}
}
