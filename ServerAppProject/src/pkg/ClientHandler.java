package pkg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.Timer;

/**
 * 
 * @author pope_ This is the class that the server uses to handle each client in
 *         a separate thread
 */
public class ClientHandler implements Runnable {

	// The socket of for the server connection;
	private Socket socket;

	// output (sending messages to this client-thread)
	private PrintWriter output;

	// input (reading of this client's messages to the server)
	private Scanner input;

	// The timer
	Timer tt;

	// Constructor - pass the socket for this connection
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	// Main execution for the different server functions for each each thread
	@Override
	public void run() {
		try {
			System.out.println("- New client connected - ");
			// in-out objects initialization
			output = new PrintWriter(socket.getOutputStream(), true);
			input = new Scanner(socket.getInputStream());

			// add this one to the printing set
			ServerApplication.clientWriters.add(output);

			// repeatedly check for messages and rebroadcast - change the implementation
			ActionListener sendStuff = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						int inputSize = socket.getInputStream().available();// not blocking
						if (inputSize != 0) {
							String receivedText = input.nextLine();
							for (PrintWriter writer : ServerApplication.clientWriters) {
								writer.println(receivedText);
							}
						}
					} catch (NoSuchElementException | IOException ex) {
						System.out.println("No more client input");
					}
				}

			};
			tt = new Timer(500, sendStuff);
			tt.start();

		} catch (Exception e) {
			System.out.println("Some exception: " + e.getMessage());
		} finally {
			
		}

	}
}
