package pkg;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	 * Sever connector object that handles all communication
	 */
	ServerConnector sC;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeView() {
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
		textArea.setText(" - Waiting For connection... -");

		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 11, 400, 217);
		chatFrame.getContentPane().add(scrollPane);

		sendBtn = new JButton("Send");
		sendBtn.setBounds(321, 239, 89, 23);
		chatFrame.getContentPane().add(sendBtn);

		sendField = new JTextField();
		sendField.setBounds(10, 240, 301, 22);
		chatFrame.getContentPane().add(sendField);
		sendField.setColumns(10);
	}

	/**
	 * Atemting a connection
	 */
	public boolean startConnection() {
		// Make a new ServerConnector to deal with communication
		boolean result = false;
		sC = new ServerConnector("127.0.0.1", 59090);
		if (sC.isConnected()) {
			sC.setUsername("o mpampa sas koproskyla");
			textArea.setText(" - Connected to the ChatRoom -");
			// Send button functionality
			sendBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					sC.sendMsg(sendField.getText());
					sendField.setText("");
				}
			});
			result = true;
		}
		return result;
	}

	/**
	 * The regular function of the client after the initialization Must always run
	 * after initialization
	 */
	public void runRoutine() {
		// Timer function to update the chat regularly
		ActionListener updateChat = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (sC.isConnected()) {
					//this happens when the connection is still well and running
					if (sC.hasNewChatContents()) {
						textArea.append("\n" + sC.getChatContents());
					}
				} else {
					//this happens when we are not connected (any more)
					textArea.setText(" - Connection Lost SadFace - ");
				}
			}
		};
		chatUpdater = new Timer(500, updateChat);
		chatUpdater.start();

	}

	/**
	 * Initialize the components in the constructor
	 */
	public MainClientWidnow() {
		initializeView();
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
					window.startConnection();
					window.runRoutine();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
