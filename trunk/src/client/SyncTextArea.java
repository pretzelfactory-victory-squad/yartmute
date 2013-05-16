package client;

import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class SyncTextArea extends JTextArea implements CaretListener, DocumentListener{
	ArrayList<Listener> listeners = new ArrayList<Listener>();
	boolean fromServer = true;
	private int caretPos;

	public synchronized void setText(String t, boolean fromServer) {
		boolean change = (this.fromServer != fromServer);
		if(change){
			this.fromServer = fromServer;
		}
		super.setText(t);
		
		if(change){
			this.fromServer = !fromServer;
		}
	}

	public synchronized void addListener(Listener l){
		listeners.add(l);
	}

	@Override
	public synchronized void insertUpdate(DocumentEvent e) {
		/*if(doNotify){
			caretPos = getCaretPosition();
		}else{
			setCaretPosition(caretPos);
		}*/
		
		for(Listener l : listeners){
			l.insertUpdate(e, fromServer);
		}
	}
	@Override
	public synchronized void removeUpdate(DocumentEvent e) {
		if(fromServer){
			caretPos = getCaretPosition();
		}else{
			try{
				setCaretPosition(caretPos);
			}catch(IllegalArgumentException e1){
				
			}
		}
		
		for(Listener l : listeners){
			l.removeUpdate(e, fromServer);
		}
	}
	@Override
	public synchronized void changedUpdate(DocumentEvent e) {
		// Never used by textarea
		throw new RuntimeException("changedUpdate() should never run???");
	}
	@Override
	public synchronized void caretUpdate(CaretEvent e) {
		for(Listener l : listeners){
			l.caretUpdate(e, fromServer);
		}
	}
	@Override
	public synchronized void setDocument(Document doc){
		doc.addDocumentListener(this);
		super.setDocument(doc);
	}

	public static interface Listener{
		void insertUpdate(DocumentEvent e, boolean fromServer);
		void removeUpdate(DocumentEvent e, boolean fromServer);
		void caretUpdate(CaretEvent e, boolean fromServer);
	}
}
