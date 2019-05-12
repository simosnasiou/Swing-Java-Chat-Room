package pkg;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainClientWidnow {
	/**
	 * Components
	 */
	private JFrame chatFrame;
	private JTextArea textArea;
	private Timer timer;
	private JScrollPane scrollPane;
	private JButton send_btn;
	private JTextField send_field;

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
		textArea.setText("let's see if it works\n");

		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 11, 400, 217);
		chatFrame.getContentPane().add(scrollPane);

		send_btn = new JButton("Send");
		send_btn.setBounds(321, 239, 89, 23);
		chatFrame.getContentPane().add(send_btn);

		send_field = new JTextField();
		send_field.setBounds(10, 240, 301, 20);
		chatFrame.getContentPane().add(send_field);
		send_field.setColumns(10);

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

	/**
	 * The regular function of the client after the initialization
	 */
	public void runRoutine() {

		// buton click funciton
		send_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (send_field.getText().equals("")) {
					output.println(send_field.getText());
					send_field.setText("");
				}
			}
		});

		// Regularly updating the chat field
		ActionListener updateChat = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					if (input.hasNextLine()) {
						textArea.append(input.nextLine());
					}
				} catch (Exception e) {
					textArea.setText(e.getMessage());
				}
			}
		};
		timer = new Timer(1000, updateChat);
		timer.start();
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
