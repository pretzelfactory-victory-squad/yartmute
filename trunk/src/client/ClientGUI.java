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
import javax.swing.JScrollPane;
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
	private ClientDoc doc;
	private TextAreaListener listener;
	private JScrollPane scrollPane;

	public ClientGUI(Client client, boolean dummyLogin){
		this.client = client;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createMenu();
		createTextArea();
		
		if(dummyLogin){
			dummyLoginAndOpenFirst();
		}
		
		this.setSize(800, 600);
		setVisible(true);
	}
	
	private void createTextArea() {
		textArea = new JTextArea();
		listener = new TextAreaListener();
		textArea.addCaretListener(listener);
		textArea.getDocument().addDocumentListener(listener);
		
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
		        "Open file", JOptionPane.QUESTION_MESSAGE, null, files, files[0]);
		doc = client.openFile(selection);
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
		synchronized(textArea){
			listener.ignore = true;
			doc.setCaretPosition( textArea.getCaretPosition() ); // Saving caret (cursor) position
			textArea.setText(doc.getText());					 // Updating text and caret position
			textArea.setCaretPosition( doc.getCaretPosition() ); // Restoring caret position
			listener.ignore = false;
		}
	}
	
	private int[] convertToLineAndSlot(String text) {
		int length = text.length();
		int line = 0;
		int count = 0;
		for( int i=0; i<text.length(); i++ ) {
		    if( text.charAt(i) == '\n' ) {
		        line++;
		        count = i+1;
		    }
		}
		int slot = length - count;
		System.out.println("line: "+line+", slot: "+slot);
		int[] result = {line, slot};
		return result;
	}
	
	public void insertNewFile(String content) {
		synchronized(listener){
			listener.ignore = true;
			textArea.setText(content);
			listener.ignore = false;
		}
	}
	
	private void dummyLoginAndListFiles(){
		if(client.connect("localhost", 3790)){
			files = client.getFileList();
		}
	}
	private void dummyLoginAndOpenFirst(){
		if(client.connect("localhost", 3790)){
			files = client.getFileList();
			doc = client.openFile(files[0]);
			doc.addObserver(this);
			insertNewFile(doc.getText());
		}
	}
	
	private class TextAreaListener implements CaretListener, DocumentListener{
		
		private String previous;
		public boolean ignore = false;
		
		public void savePrevious(){
			previous = textArea.getText();
		}
		
		public void caretUpdate(CaretEvent event) {
			try {
				int[] a = convertToLineAndSlot(textArea.getText(0, event.getDot()));
				//int pos = convertToSlot(a[0], a[1]);
				//update(null, null);
				//System.out.println("from "+event.getDot()+" to "+a[0]+","+a[1]+" back to "+pos);
				
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		public void changedUpdate(DocumentEvent e) {
	        //Plain text components do not fire these events
		}
		public void insertUpdate(DocumentEvent e) {
			if(ignore){
				System.out.println("insert ignored");
				savePrevious();
				return;
			}
			savePrevious();
			
			int offset = e.getOffset();
			int length = e.getLength();
			
			String textBefore = previous.substring(0, offset);
			String insertion = previous.substring(offset, offset+length);
			
			int[] start = convertToLineAndSlot(textBefore);
			/*int[] end = convertToLineAndSlot(insertion);
			end[0] += start[0];
			if(start[0] == end[0]){
				end[1] += start[1]-1;
			}
			
			client.queueUpdate(start[0], end[0], start[1], end[1], insertion);
	        System.out.println("inserted '"+insertion+"' at line:"+start[0]+"-"+end[0]+", slot:"+start[1]+"-"+end[1]);	
			*/
			client.queueUpdate(start[0], start[0], start[1], start[1], insertion);
	        System.out.println("inserted '"+insertion+"' at line:"+start[0]+"-"+start[0]+", slot:"+start[1]+"-"+start[1]);	
		}
		public void removeUpdate(DocumentEvent e) {
			if(ignore){
				return;
			}
			
			int offset = e.getOffset();
			int length = e.getLength();
			
			String textBefore = previous.substring(0, offset);
			String insertion = previous.substring(offset, offset+length);
			
			int[] start = convertToLineAndSlot(textBefore);
			int[] end = convertToLineAndSlot(insertion);
			end[0] += start[0];
			
			if(start[0] == end[0]){
				end[1] += start[1];
			}
			
			client.queueUpdate(start[0], end[0], start[1], end[1], "");
	        System.out.println("removed '"+insertion+"' at line:"+start[0]+"-"+end[0]+", slot:"+start[1]+"-"+end[1]);
			savePrevious();
		}
	}
}
