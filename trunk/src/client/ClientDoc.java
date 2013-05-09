package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

import common.Command;
import common.toclient.Update;

public class ClientDoc extends Observable implements Observer{
	private StringBuilder text;
	private long version;
	private ClientSocketReader reader;
	private int caretPosition;
	
	public ClientDoc(ClientSocketReader r, String text){
		reader = r;
		reader.addObserver(this);
		this.text = new StringBuilder(text);
	}
	public void insertCharacter(char c){
		
	}
	public String getText() {
		return text.toString();
	}
	public long getVersion() {
		return version;
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		Update c = (Update)reader.getCommand(Update.TYPE);
		if(c != null){
			int start = convertToSlot(c.getLineStart(), c.getSlotStart());
			int end = convertToSlot(c.getLineEnd(), c.getSlotEnd());
			String s = text.toString();
			text.replace(start, end, c.getText());
			if(start <= end){
				caretPosition += c.getText().length();
			}
			setChanged();
			notifyObservers();
		}
	}
	
	private int convertToSlot(int line, int slot) {
		int countLine = 0;
		int result = 0;
		while(countLine < line){
			result = text.indexOf(""+'\n', result)+1;
		    countLine++;
		}
		result += slot;
		System.out.println("pos: "+result);
		return result;
	}
	public void setCaretPosition(int position) {
		caretPosition = position;
	}
	public int getCaretPosition() {
		return caretPosition;
	}
}