package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;

public class ClientDoc extends Observable{
	private String text;
	private long version;
	public ClientDoc(InputStream is){
		
	}
	public ClientDoc(String text) {
		this.text = text;
	}
	public void insertCharacter(char c){
		
	}
	public String getText() {
		return text;
	}
	public long getVersion() {
		return version;
	}
}