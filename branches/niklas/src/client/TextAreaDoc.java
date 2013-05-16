package client;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import common.Log;

public class TextAreaDoc extends PlainDocument{
	
	private boolean doNotify = false;
	@Override
	public synchronized void insertString(int arg0, String arg1, AttributeSet arg2)
			throws BadLocationException {
		doNotify = true;
		super.insertString(arg0, arg1, arg2);
		doNotify = false;
	}
	@Override
	public synchronized void remove(int offs, int len) throws BadLocationException {
		doNotify = true;
		super.remove(offs, len);
		doNotify = false;
	}
	
	public synchronized void setTextDontNotify(String string) throws BadLocationException{
		doNotify = false;
		Log.debug("setTextDontNotify"+string);
		super.remove(0, getLength()-1);
		super.insertString(0, string, null);
		doNotify = true;
	}
	public synchronized void removeTextDontNotify() throws BadLocationException{
		doNotify = false;
		super.remove(0, getLength()-1);
		doNotify = true;
	}
	@Override
	protected synchronized void fireChangedUpdate(DocumentEvent e) {
		if(doNotify){
			Log.debug("fireChangedUpdate fired");
			super.fireChangedUpdate(e);
		}else{
			Log.debug("fireChangedUpdate ignored");
		}
	}
	@Override
	protected synchronized void fireInsertUpdate(DocumentEvent e) {
		if(doNotify){
			Log.debug("fireInsertUpdate fired");
			super.fireInsertUpdate(e);
		}else{
			Log.debug("fireInsertUpdate ignored");
		}
	}
	@Override
	protected synchronized void fireRemoveUpdate(DocumentEvent e) {
		if(doNotify){
			Log.debug("fireChangedUpdate fired");
			super.fireRemoveUpdate(e);
		}else{
			Log.debug("fireChangedUpdate ignored");
		}
	}
	@Override
	protected synchronized void fireUndoableEditUpdate(UndoableEditEvent e) {
		if(doNotify){
			Log.debug("fireUndoableEditUpdate fired");
			super.fireUndoableEditUpdate(e);
		}else{
			Log.debug("fireUndoableEditUpdate ignored");
		}
	}
}
