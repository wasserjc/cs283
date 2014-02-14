import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class BenchmarkClient {

	public static void main(String[] args) {
	    
		Socket connection;
		PrintWriter outgoing;
		String letter = "a";
		
	 long begin = System.currentTimeMillis();
		
	 for (int i = 0; i < 10000; ++i) {	
		try {
				 connection = new Socket( "localhost", 3333 );
		         
		         outgoing = new PrintWriter( connection.getOutputStream() );
		         outgoing.println( letter );
		         outgoing.flush();  // Make sure the data is actually sent!
		         //System.out.println("sent -" + letter + "-");
        }
		      catch (Exception e) {
		         System.out.println("Error:  " + e);
		      }
	 }
	 
	 long total_millis = System.currentTimeMillis() - begin;
	 System.out.println("Average Time per request (ms): " + total_millis);
	 
	}

}
