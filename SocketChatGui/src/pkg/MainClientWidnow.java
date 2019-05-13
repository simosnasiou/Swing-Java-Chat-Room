package pkg;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;

public class MainClientWidnow {
	/**
	 * Components
	 */
	private JFrame chatFrame;
	private JTextArea textArea;
	private Timer chatUpdater;
	private JScrollPane scrollPane;
	private JButton sendBtn;
	private JTextField sendField;

	/**
	 * Server communication objects
	 */
	Socket socket;
	Scanner input;
	PrintWriter output;

	/**
	 * Create the application.
	 */
	public MainClientWidnow() {
		initializeConnection();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// initialize the components
		chatFrame = new JFrame();
		chatFrame.setTitle("ChatWindow");
		chatFrame.setBounds(100, 100, 450, 321);
		chatFrame.setResizable(false);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.getContentPane().setLayout(null);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 45, 399, 205);
		textArea.setAutoscrolls(true);
		textArea.setText("Waiting For connection");

		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 11, 400, 217);
		chatFrame.getContentPane().add(scrollPane);

		sendBtn = new JButton("Send");
		sendBtn.setBounds(321, 239, 89, 23);
		chatFrame.getContentPane().add(sendBtn);

		sendField = new JTextField();
		sendField.setBounds(10, 240, 301, 20);
		chatFrame.getContentPane().add(sendField);
		sendField.setColumns(10);
	}

	/**
	 * Initialize the connection through the socket etc
	 */
	public void initializeConnection() {
		try {
			this.socket = new Socket("127.0.0.1", 59090);
			input = new Scanner(socket.getInputStream());
			output = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			// exile do someting!
		}
	}

	// overloading with arguments
	public void initializeConnection(String serverIP, int socketNumber) {
		try {
			this.socket = new Socket(serverIP, socketNumber);
			input = new Scanner(socket.getInputStream());
			output = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			// exile do someting!
		}
	}

	/**
	 * The regular function of the client after the initialization Must always run
	 * after initialization
	 */
	public void runRoutine() {
		// button click function send msg clear fields etc
		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.println(sendField.getText());
				sendField.setText("");
			}
		});

		// Timer function to update the chat regularly
		ActionListener updateChat = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					int inputSize = socket.getInputStream().available();// not blocking
					if (inputSize != 0) {
						String receivedText = input.nextLine();
						textArea.append(receivedText);
					}
				} catch (NoSuchElementException | IOException ex) {
					System.out.println("No more client input");
				}
			}

		};

		chatUpdater = new Timer(500, updateChat);
		chatUpdater.start();

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainClientWidnow window = new MainClientWidnow();
					window.chatFrame.setVisible(true);
					window.runRoutine();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
