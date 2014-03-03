package bananabank.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class WorkerThread extends Thread {

	Socket clientSocket;
	ServerSocket serverSocket;
	BananaBank bank;

	public WorkerThread(Socket cs, ServerSocket ss, BananaBank bank) {
		this.clientSocket = cs;
		this.serverSocket = ss;
		this.bank = bank;
	}

	@Override
	public void run() {
		
		int amount = 0;
		int srcAccountNumber = 0;
		int dstAccountNumber = 0;
		
		// need to parse through string that we get:
		// "<amount> <src> <dst>\n"
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));			
			
			while (true) {
				String str = reader.readLine();
				if (str == null) break;
				StringTokenizer st = new StringTokenizer(str);
				String firstToken = st.nextToken();			
				
				try{
					amount = Integer.parseInt(firstToken);
					srcAccountNumber = Integer.parseInt(st.nextToken());
					dstAccountNumber = Integer.parseInt(st.nextToken());
					
					Account srcAccount = bank.getAccount(srcAccountNumber);
					Account dstAccount = bank.getAccount(dstAccountNumber);
					
					// need to acquire LOCKs here
					srcAccount.lock.lock();
					dstAccount.lock.lock();
					
					srcAccount.transferTo(amount, dstAccount);
					System.out.println(amount + 
										" transfered from account " +
										srcAccountNumber +
										" to account " +
										dstAccountNumber + "\n");
					
					PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream());
					outgoing.println("Server: Transfer was a success.");
					outgoing.flush();
					
					// need to release LOCKs here
					srcAccount.lock.unlock();
					dstAccount.lock.unlock();
				} catch (NumberFormatException e) {
					// stop main server thread from accepting client connections
					serverSocket.close();
					// save current account info to file
					bank.save("accounts.txt");
					// send total amount of money in bank back to client that requested SHUTDOWN
					PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream());
					int sum = 0;
					for (Account acc : bank.getAllAccounts()) {
						sum += acc.getBalance();
					}
					outgoing.println(sum);
					outgoing.flush();
					System.out.println("Sent bank total of\n"
										+ sum +
										" to client requesting SHUTDOWN");

				} catch (NullPointerException e1) {
					PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream());
					outgoing.println("Server: Failure - wrong syntax.");
					outgoing.flush();
				}

			}
			
		} catch (IOException e) {
			
		}
		
		
	}
	

}
