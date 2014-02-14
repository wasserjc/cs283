import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class SimpleClient {

/**
 * Code modified from available code at:
 * http://math.hws.edu/javanotes/c11/s4.html
 */
	
	   public static final int LISTENING_PORT = 3333;

	   public static void main(String[] args) {

	      String hostName;         // Name of the server computer to connect to.
	      String letter;
	      Socket connection;       // A socket for communicating with the server.
	      BufferedReader incoming; // For reading data from the connection.

	      /* Get computer name from command line. */

	      if (args.length > 1) {
	         hostName = args[0];
	      	 letter = args[1];
	      }
	      else {
	            // No computer name was given.  Print a message and exit.
	         System.out.println("Usage:  java SimpleClient <server_host_name> <letter_for_server>");
	         return;
	      }

	      /* Make the connection, then read and display a line of text. */

	      try {
	         connection = new Socket( hostName, LISTENING_PORT );
	         
	         PrintWriter outgoing;   // Stream for sending data.
	         outgoing = new PrintWriter( connection.getOutputStream() );
	         outgoing.println( letter );
	         outgoing.flush();  // Make sure the data is actually sent!
	         System.out.println("sent -" + letter + "-");
	         
	         
	         incoming = new BufferedReader( 
	                          new InputStreamReader(connection.getInputStream()) );
	         String lineFromServer = incoming.readLine();
	         if (lineFromServer == null) {
	               // A null from incoming.readLine() indicates that
	               // end-of-stream was encountered.
	            throw new IOException("Connection was opened, " + 
	                  "but server did not send any data.");
	         }
	         System.out.println(lineFromServer);
	         incoming.close();
	      }
	      catch (Exception e) {
	         System.out.println("Error:  " + e);
	      }

	   }  // end main()


} //end class DateClient
