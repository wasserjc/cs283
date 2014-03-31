

import java.net.InetAddress;

public class ClientEndPoint {
	protected InetAddress address;
	protected int port;
	protected String name;
	protected int myId;
	protected int opponentId;
	
	public ClientEndPoint(InetAddress addr, int port, String name, int id) {
		this.address = addr;
		this.port = port;
		this.name = name;
		this.myId = id;
	}

	@Override
	public int hashCode() {
		// the hashcode is the exclusive or (XOR) of the port number and the hashcode of the address object
		return this.port ^ this.address.hashCode();
	}

	public InetAddress getAddress() {
		return this.address;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public void setId(int id) {
		this.myId = id;
	}
	
	public int getId() {
		return this.myId;
	}
	
	public void setOppId(int oppId) {
		this.opponentId = oppId;
	}
	
	public int getOppId() {
		return this.opponentId;
	}
	
	public String getName() {
		return name;
	}
}
