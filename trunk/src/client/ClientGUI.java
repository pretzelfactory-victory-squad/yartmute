package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI extends JFrame implements Observer{
	private JMenuBar menuBar;
	private Thread socketThread;
	private Client client;
	private List<String> files;
	private JTextArea textArea;

	public ClientGUI(Client client){
		this.client = client;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createMenu();
		createTextArea();
		
		this.setSize(800, 600);
		setVisible(true);
	}
	
	private void createTextArea() {
		textArea = new JTextArea();
		add(textArea);
	}

	private void createMenu(){
		menuBar = new JMenuBar();

		JMenu menu = new JMenu("A Menu");
		menuBar.add(menu);

		JMenuItem serverItem = new JMenuItem("Connect to server");
		menu.add(serverItem);
		
		serverItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				connectToServer();
			}
		});
		
		JMenuItem openFileItem = new JMenuItem("Open file");
		menu.add(openFileItem);
		
		openFileItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				openFile();
			}
		});
		
		JMenuItem uploadFileItem = new JMenuItem("Upload file");
		menu.add(uploadFileItem);
		
		uploadFileItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				uploadFile();
			}
		});
		
		setJMenuBar(menuBar);
	}
	
	private void connectToServer(){
		JLabel label = new JLabel("Enter server address and port (format host:port)");
		String answer = JOptionPane.showInputDialog(this, label, "Enter host address and port", JOptionPane.QUESTION_MESSAGE);
		
		String[] answerSplit = answer.split(":");
		String host = answerSplit[0];
		int port = Integer.parseInt(answerSplit[1]);
		
		System.out.println("host: "+host+", port: "+port);
		
		if(client.connect(host, port)){
			files = client.getFileList();
		}
	}
	
	private void openFile(){
		String selection = (String)JOptionPane.showInputDialog(null, "Select file:",
		        "Zoo Quiz", JOptionPane.QUESTION_MESSAGE, null, files.toArray(), files.get(0));
		String text = client.openFile(selection);
		
		textArea.setText(text);
	}

	private void uploadFile(){

	}
	
	private void insertCharacter(char c){
		client.insertCharacter(c);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
	}
}
