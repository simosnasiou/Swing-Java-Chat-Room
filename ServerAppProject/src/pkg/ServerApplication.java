package pkg;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.HashSet;
import java.util.Set;

/**
 * This will be the Chat Server console application hosted in some pc it's role
 * is to broadcast the messages to all the clients
 */

public class ServerApplication {

	/**
	 * Set of print writers used to write messages to every client
	 */
	protected static final Set<PrintWriter> clientWriters = new HashSet<>();

	public static void main(String[] args) throws IOException {
		// trying to create the server
		try (ServerSocket listener = new ServerSocket(59090)) {
			
			// verification message
			System.out.println("Server Running listening on port:" + listener.getLocalPort());

			// The pool of threads- clients of fixed size
			ExecutorService threadPool = Executors.newFixedThreadPool(15);

			// Accept the clients that request connection
			while (true) {
				threadPool.execute(new ClientHandler(listener.accept()));
			}
		}
	}

}
