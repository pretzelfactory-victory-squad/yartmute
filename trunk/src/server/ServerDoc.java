package server;

import java.util.ArrayList;
import java.util.List;

public class ServerDoc {
	
	private long version;
	private List<String> doc;
	
	private ServerDoc(){
		doc = new ArrayList<String>();
	}
	public synchronized String getDoc(){
		String output ="";
		for(String s: doc){
			output +=s;
		}
		return output;
	}
	public synchronized void write(String filename){
		
	}	
	public static synchronized String getFileName(){
		return null;
	}
}
