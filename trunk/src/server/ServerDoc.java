package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerDoc {

	private long version;
	private List<String> doc;
	private String fileName;
	private File file;
	private BufferedWriter out;

	public ServerDoc(String fileName) {
		this.fileName = fileName;
		doc = new ArrayList<String>();
		FileWriter fstream;
		try {
			BufferedReader br = new BufferedReader(new FileReader("./files/" + fileName));
			String line = br.readLine();

	        while (line != null) {
	        	doc.add(line + "\n");
	        	line = br.readLine();
	        }
	        System.out.println("Printing file '" + fileName + "'");
	        for (String s : doc) {
				System.out.println(s);
			}
			fstream = new FileWriter("./files/" + fileName, true);
			BufferedWriter out = new BufferedWriter(fstream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}

	public synchronized String getDoc() {
		String output = "";
		for (String s : doc) {
			output += s;
		}
		return output;
	}

	public synchronized long getVerNbr() {
		return this.version;
	}

	public synchronized void write(String filename) {

	}
	public void save(){
		// TODO: Save current document. 
	}
	public String getFileName() {
		return fileName;
	}
	public String toString(){
		return getFileName();
	}
	public boolean equals(Object obj){
		return fileName.equals(obj.toString());
	}
}
