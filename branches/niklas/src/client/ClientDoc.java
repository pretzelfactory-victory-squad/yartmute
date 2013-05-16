package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import common.Command;
import common.Log;
import common.toclient.Update;

public class ClientDoc implements Observer{
	private StringBuilder text;
	private long version;
	private ClientSocketReader reader;
	private int caretPosition;
	private Client client;
	private ArrayList<Listener> listeners = new ArrayList<Listener>();
	
	public ClientDoc(Client c, String text, Long version){
		client = c;
		reader = c.getReader();
		reader.addObserver(this);
		this.version = version;
		this.text = new StringBuilder(text);
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
			version = c.getVersion();
			int start = convertToSlot(c.getLineStart(), c.getSlotStart());
			int end = convertToSlot(c.getLineEnd(), c.getSlotEnd());
			String s = text.toString();
			text.replace(start, end, c.getText());
			if(client.getUserId() != c.getUserId() && end < caretPosition){
				caretPosition += c.getText().length()-(end-start);
			}
			notifyListeners();
		}
	}
	
	public void addListener(Listener l){
		listeners.add(l);
	}
	public void removeListener(Listener l){
		listeners.remove(l);
	}
	
	public void notifyListeners(){
		for(Listener l : listeners){
			l.update(this);
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
		//Log.debug("pos: "+result);
		return result;
	}
	public void setCaretPosition(int position) {
		caretPosition = position;
	}
	public int getCaretPosition() {
		return caretPosition;
	}
	
	public interface Listener{
		void update(ClientDoc clientDoc);
	}
}