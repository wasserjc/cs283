package com.example.tictactoe;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	// PUBLIC IP TO CONNECT
	// 54.186.249.228
	
	public static final int MAX_PACKET_SIZE = 512;
	InetAddress SERVER_ADDRESS = null;
	final int SERVER_PORT = 20000;
	DatagramSocket socket = null;
	static char symbol;
	// right now we will just hardcode the ID
	int id = 1993;
	private static int opponent_id;
	private static boolean turn = false;
	List<Character> board;
	
	Button b0;
	Button b1;
	Button b2;
	Button b3;
	Button b4;
	Button b5;
	Button b6;
	Button b7;
	Button b8;
	Button playButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		b0 = (Button) findViewById(R.id.Button0);
		b1 = (Button) findViewById(R.id.Button1);
		b2 = (Button) findViewById(R.id.Button2);
		b3 = (Button) findViewById(R.id.Button3);
		b4 = (Button) findViewById(R.id.Button4);
		b5 = (Button) findViewById(R.id.Button5);
		b6 = (Button) findViewById(R.id.Button6);
		b7 = (Button) findViewById(R.id.Button7);
		b8 = (Button) findViewById(R.id.Button8);
		playButton = (Button) findViewById(R.id.play_button);
		
		try {
			socket = new DatagramSocket(5000);
			SERVER_ADDRESS = InetAddress.getByName("54.186.249.228");
			
			new Thread() {
				public void run() {
					try {
						send("REGISTER " + id + " Jared\n", SERVER_ADDRESS,
								SERVER_PORT);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Something wrong.", Toast.LENGTH_LONG).show();
		}

		listenForPackets();
		// need to register with the server
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// ************************************************************************

	public static synchronized void setTurn(boolean isTurn) {
		turn = isTurn;
	}
	
	public static synchronized boolean getTurn() {
		return turn;
	}
	
	public static synchronized void setOppId(int id) {
		opponent_id = id;
	}
	
	public static synchronized int getOppId() {
		return opponent_id;
	}
	
	// ************************************************************************
	// CONSTANTLY LISTEN FOR INCOMING UDP PACKETS
	
	public void listenForPackets() {
		
		new Thread() {
			public void run() {
				try {
					// create a datagram socket, bind to port port. See
					// http://docs.oracle.com/javase/tutorial/networking/datagrams/ for
					// details.

					// receive packets in an infinite loop
					while (true) {
						// create an empty UDP packet
						byte[] buf = new byte[MAX_PACKET_SIZE];
						DatagramPacket packet = new DatagramPacket(buf, buf.length);
						// call receive (this will populate the packet with the received
						// data, and the other endpoint's info)
						socket.receive(packet);
						// start up a worker thread to process the packet (and pass it
						// the socket, too, in case the
						// worker thread wants to respond)
						processPacket(packet,socket);
					}
				} catch (IOException e) {
					runOnUiThread(new Thread() {
						public void run() {
							Toast.makeText(getApplicationContext(), "IOException ListenForPackets", Toast.LENGTH_LONG).show();
						}
					});
					
				} finally {
					if (socket != null && !socket.isClosed())
						socket.close();
				}
			}

		}.start();
	
	}
	
	
	// send a string, wrapped in a UDP packet, to the specified remote endpoint
	public void send(String payload, InetAddress address, int port)
			throws IOException {
		DatagramPacket txPacket = new DatagramPacket(payload.getBytes(),
				payload.length(), address, port);
		this.socket.send(txPacket);
		
	}

	
	// ************************************************************************
	// CALLBACK METHODS FOR BUTTONS PRESSED
	
	public void onButton0Pressed (View view) {
		if (getTurn()) {
			
			b0.setText(String.valueOf(symbol)); // either X or O
			b0.setClickable(false);
			
			board.set(0, symbol);
			
			new Thread() {
				public void run() {
					try {
						send("PLAY " + id + " "
								+ opponent_id + " 0" + "\n", SERVER_ADDRESS,
								SERVER_PORT);
					} catch (IOException e) {
						// run an error message on UI thread
					}
				}
			}.start();
			
			setTurn(false);
		}
	}

	public void onButton1Pressed (View view) {
		if (getTurn()) {
			b1.setText(String.valueOf(symbol)); // either X or O
			b1.setClickable(false);
			
			board.set(1, symbol);
			
			new Thread() {
				public void run() {
					try {
						send("PLAY " + id + " "
								+ opponent_id + " 1" + "\n", SERVER_ADDRESS,
								SERVER_PORT);
					} catch (IOException e) {
						// run an error message on UI thread
					}
				}
			}.start();
			
			setTurn(false);
		}

	}
	
	public void onButton2Pressed (View view) {
		if (getTurn()) {
			
			b2.setText(String.valueOf(symbol)); // either X or O
			b2.setClickable(false);
			
			board.set(2, symbol);
			
			new Thread() {
				public void run() {
					try {
						send("PLAY " + id + " "
								+ opponent_id + " 2" + "\n", SERVER_ADDRESS,
								SERVER_PORT);
					} catch (IOException e) {
						// run an error message on UI thread
					}
				}
			}.start();
			
			setTurn(false);
		}

	}
	
	public void onButton3Pressed (View view) {
		if (getTurn()) {
			
			b3.setText(String.valueOf(symbol)); // either X or O
			b3.setClickable(false);
			
			board.set(3, symbol);
			
			new Thread() {
				public void run() {
					try {
						send("PLAY " + id + " "
								+ opponent_id + " 3" + "\n", SERVER_ADDRESS,
								SERVER_PORT);
					} catch (IOException e) {
						// run an error message on UI thread
					}
				}
			}.start();
			
			setTurn(false);
		}

	}
	
	public void onButton4Pressed (View view) {
		if (getTurn()) {
			
			b4.setText(String.valueOf(symbol)); // either X or O
			b4.setClickable(false);
			
			board.set(4, symbol);
			
			new Thread() {
				public void run() {
					try {
						send("PLAY " + id + " "
								+ opponent_id + " 4" + "\n", SERVER_ADDRESS,
								SERVER_PORT);
					} catch (IOException e) {
						// run an error message on UI thread
					}
				}
			}.start();
			
			setTurn(false);
		}

	}
	
	public void onButton5Pressed (View view) {
		if (getTurn()) {
			
			b5.setText(String.valueOf(symbol)); // either X or O
			b5.setClickable(false);
			
			board.set(5, symbol);
			
			new Thread() {
				public void run() {
					try {
						send("PLAY " + id + " "
								+ opponent_id + " 5" + "\n", SERVER_ADDRESS,
								SERVER_PORT);
					} catch (IOException e) {
						// run an error message on UI thread
					}
				}
			}.start();
			
			setTurn(false);
		}

	}
	
	public void onButton6Pressed (View view) {
		if (getTurn()) {
			
			b6.setText(String.valueOf(symbol)); // either X or O
			b6.setClickable(false);
			
			board.set(6, symbol);
			
			new Thread() {
				public void run() {
					try {
						send("PLAY " + id + " "
								+ opponent_id + " 6" + "\n", SERVER_ADDRESS,
								SERVER_PORT);
					} catch (IOException e) {
						// run an error message on UI thread
					}
				}
			}.start();
			
			setTurn(false);
		}

	}
	
	public void onButton7Pressed (View view) {
		if (getTurn()) {
			
			b7.setText(String.valueOf(symbol)); // either X or O
			b7.setClickable(false);
			
			board.set(7, symbol);
			
			new Thread() {
				public void run() {
					try {
						send("PLAY " + id + " "
								+ opponent_id + " 7" + "\n", SERVER_ADDRESS,
								SERVER_PORT);
					} catch (IOException e) {
						// run an error message on UI thread
					}
				}
			}.start();
			
			setTurn(false);
		}

	}
	
	public void onButton8Pressed (View view) {
		if (getTurn()) {
			
			b8.setText(String.valueOf(symbol)); // either X or O
			b8.setClickable(false);
			
			board.set(8, symbol);
			
			new Thread() {
				public void run() {
					try {
						send("PLAY " + id + " "
								+ opponent_id + " 8" + "\n", SERVER_ADDRESS,
								SERVER_PORT);
					} catch (IOException e) {
						// run an error message on UI thread
					}
				}
			}.start();
			
			setTurn(false);
		}

	}
	
	public void onPlayButtonPressed (View view) {
		// set up a new board
		board = Collections.synchronizedList(new 
				ArrayList<Character>(Arrays.asList(' ',' ',' ',' ',' ',' ',' ',' ',' ')));

		new Thread() {
			public void run() {
				try {
					send("JOIN " + id, SERVER_ADDRESS, SERVER_PORT);
				} catch (IOException e) {
					runOnUiThread(new Thread() {
						public void run() {
							Toast.makeText(getApplicationContext(), "PlayButtonError", Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}.start();
		
	}
	
	
	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
	
	public void updateButton(int index) {
		Button b = null;
		switch (index) {
		case 0:
			b = b0;
			break;
		case 1:
			b = b1;
			break;
		case 2:
			b = b2;
			break;
		case 3:
			b = b3;
			break;
		case 4:
			b = b4;
			break;
		case 5:
			b = b5;
			break;
		case 6:
			b = b6;
			break;
		case 7:
			b = b7;
			break;
		case 8:
			b = b8;
			break;
		default:
			break;
		}
		
		if (symbol == 'X') b.setText("O");
		else b.setText("X");
		b.setClickable(false);
	}
	
	private void processPacket(final DatagramPacket packet, DatagramSocket socket) {
		new Thread() {
			
			public void run() {
				// convert the rxPacket's payload to a string
				String payload = new String(packet.getData(), 0, packet.getLength())
						.trim();

				// dispatch request handler functions based on the payload's prefix

				if (payload.startsWith("GAMESTART")) {
					onGameStartRequest(payload);
					return;
				}

				//
				// implement other request handlers here...
				//
						
				if (payload.startsWith("PLAY")) {
					onPlayRequest(payload);
					return;
				}
								
				if (payload.startsWith("ENDGAME")) {
					onEndGameRequest(payload);
					return;
				}
						
				// if we got here, it must have been a bad request, so we tell the
				// client about it
				onBadRequest(payload);

			}
			
			// callback methods
			
			private void onGameStartRequest(String payload) {
				try {
					StringTokenizer st = new StringTokenizer(payload);
					st.nextToken(); // this was the "GAMESTART" token
					
					char symbol = st.nextToken().charAt(0);
					setOppId(Integer.parseInt(st.nextToken()));
					final String opponent_name = st.nextToken();
					
					if (symbol == 'X') {
						MainActivity.setTurn(true);
						MainActivity.symbol = 'X';
						
					}
					else MainActivity.symbol = 'O';
					
					runOnUiThread(new Thread() {
						public void run() {
							Toast.makeText(getApplicationContext(),
									"Starting Game with " + opponent_name,
									Toast.LENGTH_LONG).show();
							if (getTurn())
								Toast.makeText(getApplicationContext(),
										"Your turn.", Toast.LENGTH_LONG).show();
							else Toast.makeText(getApplicationContext(),
									"Waiting for opponent move.", Toast.LENGTH_LONG).show();
						}
					});
					
				} catch (Exception e) {
					
				}
			}
			
			private void onPlayRequest(String payload) {
				try {
					StringTokenizer st = new StringTokenizer(payload);
					st.nextToken(); // this was the "PLAY" token

					int opponent_id = Integer.parseInt(st.nextToken());
					final int index = Integer.parseInt(st.nextToken());
					
					if (symbol == 'X') board.set(index,'O');
					else board.set(index,'X');
					
					// check if game over (draw or loss)
					// if game over, then send ENDGAME
					if (gameIsDraw()) {
						// send ENDGAME
						try {
							send("ENDGAME " + id + " "
									+ opponent_id + "\n", SERVER_ADDRESS,
									SERVER_PORT);
						} catch (IOException e) {
							// run an error message on UI thread
						}

						// Toast to user that the game is over - DRAW
						runOnUiThread(new Thread() {
							public void run() {
								Toast.makeText(getApplicationContext(),"It's a draw!", Toast.LENGTH_LONG).show();
								resetGame();
							}
						});
					
					}
					else if (gameIsLoss()) {
						// send ENDGAME
						try {
							send("ENDGAME " + id + " "
									+ opponent_id + "\n", SERVER_ADDRESS,
									SERVER_PORT);
						} catch (IOException e) {
							// run an error message on UI thread
						}

						// Toast that we lost
						runOnUiThread(new Thread() {
							public void run() {
								Toast.makeText(getApplicationContext(),"You lost!", Toast.LENGTH_LONG).show();
								resetGame();
							}
						});
						
						
					}
					
					else {
						// need to insert opponent symbol into the index specified
						// that corresponding button should no longer be CLICKable
						// then setTurn(true) **
						// give user a Toast telling them that it is their turn
						runOnUiThread(new Thread() {
							public void run() {
								updateButton(index);
								Toast.makeText(getApplicationContext(),
										"Your turn!",
										Toast.LENGTH_SHORT).show();
							}
						});
						
						setTurn(true);

					}
					
				} catch (Exception e) {
					
				}
			}
			
			private void onEndGameRequest(String payload) {
				if (gameIsDraw()) {
					runOnUiThread(new Thread() {
						public void run() {
							Toast.makeText(getApplicationContext(), "It's a Draw!", Toast.LENGTH_LONG).show();
						}
					});
				}
				else if (gameIsLoss()) {
					runOnUiThread(new Thread() {
						public void run() {
							Toast.makeText(getApplicationContext(), "You lost!", Toast.LENGTH_LONG).show();
						}
					});
				}
				else {
					runOnUiThread(new Thread() {
						public void run() {
							Toast.makeText(getApplicationContext(), "You won!", Toast.LENGTH_LONG).show();
						}
					});
				}
				// need to reset the entire game
				runOnUiThread(new Thread() {
					public void run() {
						resetGame();
					}
				});
				
			}
			
			private void onBadRequest(final String payload) {
				runOnUiThread(new Thread() {
					public void run() {
						Toast.makeText(getApplicationContext(),payload,Toast.LENGTH_LONG).show();
					}
				});
			}
			

			
		}.start();
	}
	
	public boolean gameIsDraw() {
		
		for (Character c : board) {
			if (c.compareTo(' ') == 0) return false;
		}

		return true; // board is full
	}
	
	public boolean gameIsLoss() {
		if (symbol == 'O') return (	
				(board.get(0) == 'X' && board.get(0) == board.get(1) && board.get(1) == board.get(2)) ||
				(board.get(3) == 'X' && board.get(3) == board.get(4) && board.get(4) == board.get(5)) ||
				(board.get(6) == 'X' && board.get(6) == board.get(7) && board.get(7) == board.get(8)) ||
				(board.get(0) == 'X' && board.get(0) == board.get(3) && board.get(3) == board.get(6)) ||
				(board.get(1) == 'X' && board.get(1) == board.get(4) && board.get(4) == board.get(7)) ||
				(board.get(2) == 'X' && board.get(2) == board.get(5) && board.get(5) == board.get(8)) ||
				(board.get(0) == 'X' && board.get(0) == board.get(4) && board.get(4) == board.get(8)) ||
				(board.get(2) == 'X' && board.get(2) == board.get(4) && board.get(4) == board.get(6))
				
			);
		
		else return (	
				(board.get(0) == 'O' && board.get(0) == board.get(1) && board.get(1) == board.get(2)) ||
				(board.get(3) == 'O' && board.get(3) == board.get(4) && board.get(4) == board.get(5)) ||
				(board.get(6) == 'O' && board.get(6) == board.get(7) && board.get(7) == board.get(8)) ||
				(board.get(0) == 'O' && board.get(0) == board.get(3) && board.get(3) == board.get(6)) ||
				(board.get(1) == 'O' && board.get(1) == board.get(4) && board.get(4) == board.get(7)) ||
				(board.get(2) == 'O' && board.get(2) == board.get(5) && board.get(5) == board.get(8)) ||
				(board.get(0) == 'O' && board.get(0) == board.get(4) && board.get(4) == board.get(8)) ||
				(board.get(2) == 'O' && board.get(2) == board.get(4) && board.get(4) == board.get(6))
				
			);
	}
	
	public void resetGame() {
		for (Character c : board) {
			c = ' ';
		}
		
		b0.setText("");
		b1.setText("");
		b2.setText("");
		b3.setText("");
		b4.setText("");
		b5.setText("");
		b6.setText("");
		b7.setText("");
		b8.setText("");
		
		b0.setClickable(true);
		b1.setClickable(true);
		b2.setClickable(true);
		b3.setClickable(true);
		b4.setClickable(true);
		b5.setClickable(true);
		b6.setClickable(true);
		b7.setClickable(true);
		b8.setClickable(true);
		
		Toast.makeText(getApplicationContext(),"Game Reset",Toast.LENGTH_LONG).show();
	}
}
