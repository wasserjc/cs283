import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * SimpleServer.java
 * Single threaded server:
 * Echo back the text received from client in upper case.
 * 
 * *** Code modified from http://math.hws.edu/javanotes/c11/s4.html
 * implementation of a trivial client/server.
 *
 */
public class SimpleServer {
	
	   public static final int LISTENING_PORT = 3333;

	   public static void main(String[] args) {

	      ServerSocket listener;  // Listens for incoming connections.
	      Socket connection;      // For communication with the connecting program.

	      /* Accept and process connections forever, or until some error occurs.
	         (Note that errors that occur while communicating with a connected 
	         program are caught and handled in the sendDate() routine, so
	         they will not crash the server.) */

	      try {
	         listener = new ServerSocket(LISTENING_PORT);
	         System.out.println("Listening on port " + LISTENING_PORT);
	         while (true) {
	                // Accept next connection request and handle it.
	            connection = listener.accept(); 
	            
		         BufferedReader incoming = new BufferedReader( 
                         new InputStreamReader(connection.getInputStream()) );
		         String lineFromServer = incoming.readLine();

	            System.out.println("Received " + lineFromServer);
	            sendUpperCase(connection, lineFromServer);
	         }
	      }
	      catch (Exception e) {
	         System.out.println("Sorry, the server has shut down.");
	         System.out.println("Error:  " + e);
	         return;
	      }

	   }  // end main()


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
