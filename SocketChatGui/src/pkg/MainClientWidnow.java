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
import javax.swing.JLabel;
import java.awt.Font;

public class MainClientWidnow {
	/**
	 * Components
	 */
	private JFrame chatFrame;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JButton sendBtn;
	private JTextField sendField;
	private JTextField nameField;
	private JTextField ipField;
	private JTextField portField;
	private JLabel portLabel;
	JButton connectBtn;

	/**
	 * Sever connector object that handles all communication
	 * Also a variable for connection Status
	 */
	ServerConnector sC;
	boolean isConnected=false; 



	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeView() {
		// initialize the components
		chatFrame = new JFrame();
		chatFrame.setTitle("ChatWindow");
		chatFrame.setBounds(100, 100, 551, 315);
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
		
		JLabel nameLabel = new JLabel("User Name");
		nameLabel.setBounds(420, 17, 81, 14);
		chatFrame.getContentPane().add(nameLabel);
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel ipLabel = new JLabel("Server I.P");
		ipLabel.setBounds(420, 73, 81, 14);
		chatFrame.getContentPane().add(ipLabel);
		ipLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		portLabel = new JLabel("Port");
		portLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		portLabel.setBounds(420, 129, 81, 14);
		chatFrame.getContentPane().add(portLabel);
		
		nameField = new JTextField();
		nameField.setToolTipText("nickname other users see");
		nameField.setText("anonymous");
		nameField.setBounds(420, 42, 86, 20);
		chatFrame.getContentPane().add(nameField);
		nameField.setColumns(10);
		
		ipField = new JTextField();
		ipField.setText("127.0.0.1");
		ipField.setToolTipText("ip of the server to connceto to");
		ipField.setBounds(420, 98, 86, 20);
		chatFrame.getContentPane().add(ipField);
		ipField.setColumns(10);
		
		portField = new JTextField();
		portField.setText("59090");
		portField.setToolTipText("server port");
		portField.setBounds(420, 154, 86, 20);
		chatFrame.getContentPane().add(portField);
		portField.setColumns(10);
		
		connectBtn = new JButton("Connect !");
		connectBtn.setToolTipText("Press to connect to the server");
		connectBtn.setBounds(420, 197, 100, 31);
		chatFrame.getContentPane().add(connectBtn);
		
		//add Enter button functionality
		chatFrame.getRootPane().setDefaultButton(sendBtn);
	}

	/**
	 * Attempting a connection
	 */
	public void startConnection() {
		// Make a new ServerConnector to deal with communication using the input data
		sC = new ServerConnector();
		isConnected=sC.initializeConnection(ipField.getText(), Integer.parseInt(portField.getText()));
		if (isConnected) {
			sC.setUsername(nameField.getText());
			textArea.setText(" - Connected to the Chat Room -");			
			nameField.setEditable(false);
			isConnected =true;
			ipField.setEditable(false);
			portField.setEditable(false);
			connectBtn.setEnabled(false);
			connectBtn.setText("Connected");
			
			//Start periodic running functionality
			runRoutine();
			// Send button functionality
			sendBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					sC.sendMsg(sendField.getText());
					sendField.setText("");
				}
			});

		} else {
			textArea.setText(" - Connection with the chat server failed. Try again later - ");
			//Assign null to eventually free resources
			sC=null;
		}
	}

	/**
	 * The regular function of the client after the initialization Must always run
	 * after initialization
	 */
	public void runRoutine() {
		// Timer function to update the chat regularly
		ActionListener updateChat = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (isConnected) {
					//this happens when the connection is still well and running
					if (sC.hasNewChatContents()) {
						textArea.append("\n" + sC.getChatContents());					
					}
				} else {
					//not expected to ever run in the current form
					textArea.setText("Some problem occured while updating chat conntents");
				}
			}
		};
		Timer chatUpdater = new Timer(200, updateChat);
		chatUpdater.start();
	}

	/**
	 * Initialize the components in the constructor
	 */
	public MainClientWidnow() {
		initializeView();
		connectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startConnection();
			}
		});
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
