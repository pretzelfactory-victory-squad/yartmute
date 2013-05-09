package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

import common.Command;
import common.toclient.Update;

import server.Main;

public class ClientGUI extends JFrame implements Observer{
	private JMenuBar menuBar;
	private Client client;
	private String[] files;
	private JTextArea textArea;
	private ClientSocketReader reader;

	public ClientGUI(Client client){
		this.client = client;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createMenu();
		createTextArea();
		
		dummyLoginAndOpenFirst();
		
		this.setSize(800, 600);
		setVisible(true);
	}
	
	private void createTextArea() {
		textArea = new JTextArea();
		add(textArea);
		TextAreaListener listener = new TextAreaListener();
		textArea.addCaretListener(listener);
		textArea.getDocument().addDocumentListener(listener);
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
			reader = client.getReader();
			reader.addObserver(this);
			files = client.getFileList();
		}
	}
	
	private void openFile(){
		String selection = (String)JOptionPane.showInputDialog(null, "Select file:",
		        "Open file", JOptionPane.QUESTION_MESSAGE, null, files, files[0]);
		ClientDoc doc = client.openFile(selection);
		doc.addObserver(this);
		insertNewFile(doc.getText());
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
		for(Command c : reader.getCommands()){
			if(c.getType().equals(Update.TYPE)){
				Update u = (Update)c;
				//u.getLineStart();
				//TODO: continue here
			}
		}
	}
	
	private int[] convertToLineAndPos(String text) {
		int length = text.length();
		int line = 0;
		int count = 0;
		for( int i=0; i<text.length(); i++ ) {
		    if( text.charAt(i) == '\n' ) {
		        line++;
		        count = i+1;
		    }
		}
		int pos = length - count;
		System.out.println("line: "+line+", pos: "+pos);
		int[] result = {line, pos};
		return result;
	}
	
	public void insertNewFile(String content) {
		textArea.setText(content);
	}
	
	private void dummyLoginAndListFiles(){
		if(client.connect("localhost", 3790)){
			files = client.getFileList();
		}
	}
	private void dummyLoginAndOpenFirst(){
		if(client.connect("localhost", 3790)){
			files = client.getFileList();
			ClientDoc doc = client.openFile(files[0]);
			doc.addObserver(this);
			insertNewFile(doc.getText());
		}
	}
	
	private class TextAreaListener implements CaretListener, DocumentListener{
		
		private String previous;
		
		public void savePrevious(){
			previous = textArea.getText();
		}
		
		public void caretUpdate(CaretEvent event) {
			try {
				convertToLineAndPos(textArea.getText(0, event.getDot()));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		public void changedUpdate(DocumentEvent e) {
	        //Plain text components do not fire these events
		}
		public void insertUpdate(DocumentEvent e) {
			if(previous == null){
				savePrevious();
				return;
			}
			savePrevious();
			
			int offset = e.getOffset();
			int length = e.getLength();
			
			String textBefore = previous.substring(0, offset);
			String insertion = previous.substring(offset, offset+length);
			
			int[] start = convertToLineAndPos(textBefore);
			int[] end = convertToLineAndPos(insertion);
			end[0]+=start[0];
			if(start[0] == end[0]){
				end[1]+=start[1];
			}
			
			client.queueUpdate(start[0], end[0], start[1], end[1], insertion);
	        System.out.println("inserted '"+insertion+"' at line:"+start[0]+"-"+end[0]+", slot:"+start[1]+"-"+end[1]);	
		}
		public void removeUpdate(DocumentEvent e) {
			int offset = e.getOffset();
			int length = e.getLength();
			
			String textBefore = previous.substring(0, offset);
			String insertion = previous.substring(offset, offset+length);
			
			int[] start = convertToLineAndPos(textBefore);
			int[] end = convertToLineAndPos(insertion);
			
			client.queueUpdate(start[0], end[0], start[1], end[1], "");
	        System.out.println("removed '"+insertion+"' at line:"+start[0]+"-"+end[0]+", slot:"+start[1]+"-"+end[1]);
			savePrevious();
		}
		
		/*public void insertUpdate(DocumentEvent event) {
			try {
				String textBefore = textArea.getText(0, event.getOffset());
				String insertion = textArea.getText(event.getOffset(), event.getLength());
				int[] lineAndPosBefore = convertToLineAndPos(textBefore);
				int[] lineAndPos = convertToLineAndPos(insertion);
				int lineStart = lineAndPosBefore[0];
				int slotStart = lineAndPosBefore[1];
				int lineEnd = lineStart + lineAndPos[0];
				int slotEnd = lineAndPos[1];
				client.queueUpdate(lineStart, lineEnd, slotStart, slotEnd, insertion);
		        System.out.println("inserted '"+insertion+"' at line:"+lineStart+"-"+lineEnd+", slot:"+slotStart+"-"+slotEnd);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
	    }
	    public void removeUpdate(DocumentEvent event) {
	        
	    }
	    public void changedUpdate(DocumentEvent event) {
	    	try {
				String textBefore = textArea.getText(0, event.getOffset());
				String insertion = textArea.getText(event.getOffset(), event.getLength());
				int[] lineAndPosBefore = convertToLineAndPos(textBefore);
				int[] lineAndPos = convertToLineAndPos(insertion);
				int lineStart = lineAndPosBefore[0];
				int slotStart = lineAndPosBefore[1];
				int lineEnd = lineStart + lineAndPos[0];
				int slotEnd = lineAndPos[1];
				client.queueUpdate(lineStart, lineEnd, slotStart, slotEnd, insertion);
		        System.out.println("changed to: '"+insertion+"' at line:"+lineStart+"-"+lineEnd+", slot:"+slotStart+"-"+slotEnd);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
	    }*/
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		new ClientGUI(client);
	}
}
