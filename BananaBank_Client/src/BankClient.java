import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class BankClient {

	public static final int LISTENING_PORT = 3333;
	public static void main(String[] args) {
		
		System.out.println("Basic Bank Client:");
		System.out.println("Sends a transfer request to server.");
		System.out.println("Then requests SHUTDOWN.\n");
		
		try {
		     Socket connection;       // A socket for communicating with the server.
			
	         connection = new Socket("localhost", LISTENING_PORT);
	         
	         PrintWriter outgoing;   // Stream for sending data.
	         outgoing = new PrintWriter( connection.getOutputStream() );
	         outgoing.print("5 22222 11111\n");
	         outgoing.flush();  // Make sure the data is actually sent!
	         System.out.println("Need to transfer: 5 22222 11111");
	         Thread.sleep(500);
	         BufferedReader incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	         System.out.println(incoming.readLine());
	         Thread.sleep(500);
	         outgoing.println("SHUTDOWN");
	         outgoing.flush();
	         System.out.println("Request for shutdown");
	         System.out.println(incoming.readLine());
	         
	         	         
		} catch (Exception e) {
		}
	}

}
