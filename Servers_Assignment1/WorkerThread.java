import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WorkerThread extends Thread {

	Socket clientSocket;

	public WorkerThread(Socket cs) {
		this.clientSocket = cs;
	}

	@Override
	public void run() {
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			String line = r.readLine();
			sendUpperCase(this.clientSocket, line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	   /**
	    * The parameter, client, is a socket that is already connected to another 
	    * program.  Get an output stream for the connection, send the current time, 
	    * and close the connection.
	    */
	   private static void sendUpperCase(Socket client, String lineFromServer) {
	      try {
	         PrintWriter outgoing;   // Stream for sending data.
	         outgoing = new PrintWriter( client.getOutputStream() );
	         outgoing.println( lineFromServer.toUpperCase() );
	         outgoing.flush();  // Make sure the data is actually sent!
	         System.out.println("Sent " + lineFromServer.toUpperCase());
	         client.close();
	      }
	      catch (Exception e){
	         System.out.println("Error: " + e);
	      }
	   } // end sendDate()


}
