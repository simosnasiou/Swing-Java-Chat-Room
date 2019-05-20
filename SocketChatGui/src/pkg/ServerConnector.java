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
	 * setter for the username with restriction filters
	 */
	public void setUsername(String usrnm) {
		usrnm =usrnm.trim();
		usrnm = usrnm.replaceAll("\\s", "").substring(0, Math.min(10,usrnm.length()));
		this.username = usrnm;
	}

	/**
	 *  Initialize the socket (rather try to) and the i/o streams
	 */
	public boolean initializeConnection(String serverIP, int socketNumber) {
		boolean result=true;
		try {
			this.socket = new Socket(serverIP, socketNumber);
			this.input = new Scanner(socket.getInputStream());
			this.output = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			result =false;
		}
		return result;
	}
	

	/**
	 *  Sending the server to print
	 */
	public void sendMsg(String stringToSend) {
		if (!socket.isClosed()) {
			output.println(username + " : " + stringToSend);
		}
	}

	/**
	 * Getting the contents of the chat from the server
	 */
	public String getChatContents() {
		String resultText = null;
		try {
			resultText = input.nextLine();
		} catch (NoSuchElementException ex) {
			resultText = "An error occured reading from Chat Server : " + ex.getMessage();
		}
		return resultText;
	}


	/**
	 * Show weather the chat has new input to be print
	 */
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

}
