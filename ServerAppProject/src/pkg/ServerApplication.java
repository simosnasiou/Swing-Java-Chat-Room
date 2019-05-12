package pkg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import javax.swing.Timer;

public class ServerApplication {
	/**
	 * This will be the Chat Server console application hosted in some pc it's role
	 * is to broadcast the messages to all the clients
	 */

	// Set of print writers used to write messages to every client
	private static Set<PrintWriter> client_writers = new HashSet<>();

	// the class for client hanlder invoked in seperate threads for every client
	public static class ClientHandler implements Runnable {

		// The socket of for the server connection;
		private Socket socket;

		// output (sending messages to this client-thread)
		private PrintWriter output;

		// input (reading of this client's messages to the server)
		private Scanner input;

		// Constructor - pass the socket for this connection
		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		// Main execution for the different server functions
		@Override
		public void run() {
			try {
				System.out.println("- New client connected - ");
				// in-out objects initialization
				output = new PrintWriter(socket.getOutputStream(), true);
				input = new Scanner(socket.getInputStream());

				// add this one to the printing set
				client_writers.add(output);

				// repeatedly check for messages and rebroadcast - change the implementation
				// from while true to someting else
				ActionListener sendStuff = new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						//System.out.print(input.nextLine());
						if (true) {
							//String received_msg = input.nextLine();
							//output.println(received_msg);
							output.println("so cold\n");
						}
					}
				};
				Timer tt = new Timer(500, sendStuff);
				tt.start();

			} catch (Exception e) {
				System.out.println("Some exception: " + e.getMessage());
			} finally {
				/**
				 * try { //socket.close(); System.out.println("Thread closed"); } catch
				 * (IOException e) {
				 * 
				 * }
				 */
			}

		}

	}

	public static void main(String[] args) throws IOException {
		// trying to create the server
		try (ServerSocket listener = new ServerSocket(59090)) {

			// veryfication message
			System.out.println("Server Running listening on port:" + listener.getLocalPort());

			// The pool of threads- clients of fixed size
			ExecutorService thread_pool = Executors.newFixedThreadPool(15);

			// Accept the clients that request connection
			while (true) {
				thread_pool.execute(new ClientHandler(listener.accept()));
			}
		}
	}

}
