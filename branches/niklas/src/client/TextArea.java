package client;

import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import client.Client.ServerException;

import common.Log;

public class TextArea extends JTextArea implements CaretListener, DocumentListener{
	private boolean doNotify;
	private ClientDoc clientDoc;
	private Client client;
	private ClientGUI clientGUI;

	public TextArea(Client client, ClientGUI clientGUI){
		addCaretListener(this);
		getDocument().addDocumentListener(this);
		this.clientDoc = clientGUI.doc;
		this.client = client;
		this.clientGUI = clientGUI;
	}
	
	public synchronized void setText(String content, boolean notify) {
		boolean change = (doNotify != notify);
		if(change){
			doNotify = notify;
		}
		super.setText(content);
		if(change){
			doNotify = !doNotify;
		}
	}
	
	private static int[] convertToLineAndSlot(String text) {
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
		//Log.debug("line: "+line+", slot: "+slot);
		int[] result = {line, slot};
		return result;
	}
	private String previous;

	public void savePrevious(){
		previous = getText();
	}

	public void caretUpdate(CaretEvent event) {
		if(isEditable()){
			clientDoc.setCaretPosition( getCaretPosition() ); // Saving caret (cursor) position
		}
	}
	public void changedUpdate(DocumentEvent e) {
		//Plain text components do not fire these events
	}
	public void insertUpdate(DocumentEvent event) {
		if(!doNotify){
			Log.debug("insert ignored");
			savePrevious();
			return;
		}
		savePrevious();

		int offset = event.getOffset();
		int length = event.getLength();

		String textBefore = previous.substring(0, offset);
		String insertion = previous.substring(offset, offset+length);

		int[] start = convertToLineAndSlot(textBefore);
		try {
			client.queueUpdate(start[0], start[0], start[1], start[1], insertion);
		} catch (ServerException e) {
			clientGUI.showError(e);
		}
		Log.debug("inserted '"+insertion+"' at line:"+start[0]+"-"+start[0]+", slot:"+start[1]+"-"+start[1]);	
	}
	public void removeUpdate(DocumentEvent event) {
		if(doNotify){
			return;
		}

		int offset = event.getOffset();
		int length = event.getLength();

		String textBefore = previous.substring(0, offset);
		String insertion = previous.substring(offset, offset+length);

		int[] start = convertToLineAndSlot(textBefore);
		int[] end = convertToLineAndSlot(insertion);
		end[0] += start[0];

		if(start[0] == end[0]){
			end[1] += start[1];
		}

		try {
			client.queueUpdate(start[0], end[0], start[1], end[1], "");
		} catch (ServerException e) {
			clientGUI.showError(e);
		}
		
		Log.debug("removed '"+insertion+"' at line:"+start[0]+"-"+end[0]+", slot:"+start[1]+"-"+end[1]);
		savePrevious();
	}
	
	private interface TextAreaListener{
		
	}
}
