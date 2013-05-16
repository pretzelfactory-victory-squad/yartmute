package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import client.Client.ServerException;

import common.Log;

public class ClientGUI extends JFrame implements Observer{
	private JMenuBar menuBar;
	private Client client;
	private String[] files;
	private TextArea textArea;
	ClientDoc doc;
	private JScrollPane scrollPane;

	private static final String START_TEXT = ""+
" __   __ _______  ______ _______ _______ _     _ _______ _______\n"+
"   \\_/   |_____| |_____/    |    |  |  | |     |    |    |______\n"+
"    |    |     | |    \\_    |    |  |  | |_____|    |    |______\n"+
"\n"+
"                            ,\n"+
"                           //\\\n"+
"                          / | ;\n"+
"                          | /_|\n"+
"                        .-\"`  `\"-.\n"+
"                      /`          `\\\n"+
"                     /              \\\n"+
"               .-.,_|      .-\"\"\"\"-. |\n"+
"              |     `\",_,-'  (((-. '(\n"+
"               \\ (`\"=._.'/   (  (o>'-`\"#\n"+
"    ,           '.`\"-'` /     `--`  '==;\n"+
"   /\\            `'--'`\\         _.'~~\n"+
"  / | \\                  `.,___,-}\n"+
"  | |  |                   )  {  }\n"+
"   \\ \\ (.--==---==-------=' o {  }\n"+
"    \",/` (_) (_)  (_)    (_)   \\ /\n"+
"     / ()   o   ()    ()        ^|\n"+
"     \\   ()  (    () o        ;  /\n"+
"      `\\      \\         ;    / } |\n"+
"        )      \\       /   /`  } /\n"+
"     ,-'       |=,_   |   /,_ ,'/\n"+
"     |    _,.-`/   `\"=\\   \\\\   \\\n"+
"     | .\"` \\  |        \\   \\`\\  \\\n"+
"     | |    \\ \\         `\\  \\ `\\ \\\n"+
"     | |     \\ \\          `\\ \\  \\ \\\n"+
"     | |      \\ \\           \\ \\  \\ \\\n"+
"     | |       \\ \\           \\ \\  \\ \\\n"+
"     | |        \\ \\           \\ \\  \\ \\\n"+
"     | |         ) \\           \\ \\  ) \\\n"+
" jgs `) \\        ^ww            ) \\ ^ww\n"+
"      ^ww                       ^ww";
	
	public ClientGUI(Client client, boolean dummyLogin){
		this.client = client;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createMenu();
		createTextArea();
		client.addObserver(this);

		if(dummyLogin){
			dummyLogin();
		}

		this.setSize(800, 600);
		setVisible(true);
	}

	private void createTextArea() {
		textArea = new TextArea(client, this);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 14));
		textArea.setText(START_TEXT, false);
		textArea.setEditable(false);

		scrollPane = new JScrollPane(textArea);
		add(scrollPane);
	}

	private void createMenu(){
		menuBar = new JMenuBar();

		JMenu menu = new JMenu("A Menu");
		menuBar.add(menu);

		JMenuItem serverItem = new JMenuItem("Connect to server");
		menu.add(serverItem);

		serverItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				connectToServerDialog();
			}
		});

		JMenuItem openFileItem = new JMenuItem("Open file");
		menu.add(openFileItem);

		openFileItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				openFileDialog();
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

	private void connectToServerDialog(){
		JLabel label = new JLabel("Enter server address and port (format host:port)");
		String answer = JOptionPane.showInputDialog(this, label, "Enter host address and port", JOptionPane.QUESTION_MESSAGE);
		if(answer != null){
			String[] answerSplit = answer.split(":");
			if(answerSplit.length == 2){
				String host = answerSplit[0];
				int port = Integer.parseInt(answerSplit[1]);
				connectToServer(host, port);
			}else{
				showError("Illegal input format");
				connectToServerDialog();
			}
		}
	}
	private boolean connectToServer(String host, int port){
		try{
			client.connect(host, port);
			files = client.getFileList();
			openFileDialog();
			return true;
		}catch(ServerException e){
			showError(e);
			return false;
		}
	}
	
	private void showError(String msg){
		JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	void showError(Exception e){
		showError(e.getMessage());
	}

	private boolean openFile(String filename){
		if(filename != null){
			try{
				doc = client.openFile(filename);
				doc.addObserver(this);
				insertNewFile(doc.getText());
				textArea.setEditable(true);
				return true;
			} catch(ServerException e){
				showError(e);
			}
		}
		return false;
	}
	
	private void openFileDialog(){
		if(files != null){
			String selection = (String)JOptionPane.showInputDialog(null, "Select file:",
			        "Open file", JOptionPane.QUESTION_MESSAGE, null, files, files[0]);
			openFile(selection); 		
		} else {
			showError("Please connect to a server first.");
		}
	}

	private void uploadFile(){
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Log.debug(arg0.getClass());
		TextAreaDoc tDoc = ((TextAreaDoc)textArea.getDocument());
		if(arg0.equals(doc)){
			try{
				tDoc.setTextDontNotify(doc.getText());
				synchronized(textArea){
					try{
						textArea.setCaretPosition( doc.getCaretPosition() ); // Restoring caret position
					}catch(IllegalArgumentException e){
						
					}
				}
			}catch(BadLocationException e){
				Log.error(e);
			}
		}else if(arg0.equals(client)){
			if(arg1 instanceof Exception){
				showError((Exception)arg1);
				if(!client.isConnected()){
					try{
						tDoc.setTextDontNotify("");
						textArea.setEditable(false);
					}catch(BadLocationException e){
						Log.error(e);
					}
				}
			}
		}
	}

	public void insertNewFile(String content) {
		textArea.setText(content, false);
	}
	
	private void dummyLogin(){
		if(connectToServer("localhost", 3790)){
			try {
				files = client.getFileList();
			} catch (ServerException e) {
				showError(e);
			}
		}
	}
}
