package pkg;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerConnector {
	/**
	 * This class is supposed to hold communication functionality for the client
	 * thus leaving MainClient Window dealing mainly with the Gui part of the
	 * Client-application and encapsulating elements
	 */

	/**
	 * Server communication objects
	 */
	private Socket socket;
	private Scanner input;
	private PrintWriter output;

	/**
	 * the name of the user to be printed
	 */
	private String username = "anonymous";

	/**
	 * Connection Status
	 */
	public boolean isConnected() {
		boolean isit = false;
		try {
			if (!socket.isClosed() && socket.isConnected()) {
				isit = true;
			}
		} catch (Exception e) {
			isit = false;
		}
		return isit;
	}

	// seting username
	public void setUsername(String usrnm) {
		usrnm = usrnm.trim().replaceAll("\\s", "").substring(0,10);
		this.username = usrnm;
	}

	// Initialize the socket (rather try to) and the i/o streams
	public void initializeConnection(String serverIP, int socketNumber) {
		try {
			this.socket = new Socket(serverIP, socketNumber);
			this.input = new Scanner(socket.getInputStream());
			this.output = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			// exile do someting!
		}
	}
	
	//add desctiption
	public void sendMsg(String stringToSend) {
		if (!socket.isClosed()) {
			output.println(username + " : " + stringToSend);
		}
	}

	//add decription
	public String getChatContents() {
		String resultText = null;
		try {
			resultText = input.nextLine();
		} catch (NoSuchElementException ex) {
			resultText = "An error occured reading from Chat Server : " + ex.getMessage();
		}
		return resultText;
	}

	// weather chat has new to messages to show
	public boolean hasNewChatContents() {
		boolean value = false;
		try {
			if (socket.getInputStream().available() != 0) {
				value = true;
			}
		} catch (IOException ex) {
			// do nothing for now
		}
		return value;
	}

	/**
	 * Constructor.
	 */
	public ServerConnector(String serverIP, int socketNumber) {
		initializeConnection(serverIP, socketNumber);
	}
}
