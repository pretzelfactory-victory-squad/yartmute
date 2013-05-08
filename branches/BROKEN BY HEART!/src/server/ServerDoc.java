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

	// Create a new document and read it from harddrive
	public ServerDoc(String fileName) {
		this.fileName = fileName;
		doc = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("./files/" + fileName));
			BufferedWriter out = new BufferedWriter(new FileWriter("./files/" + fileName, false));
			String line = br.readLine();
			if(line != null){
				// Read Version number
				version = Integer.valueOf(line).longValue();
				line = br.readLine();
				// Read document
				while (line != null) {
					doc.add(line + "\n");
					line = br.readLine();
				}
				System.out.println("Printing file '" + fileName + "'");
				for (String s : doc) {
					System.out.println(s);
				}
			} else {
				// Create new document with given filename
				out.write("0");
				out.flush();
				System.out.println("Created new document with filename '" + fileName + "'");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	// Save the document to harddrive
	public void save(){
		File file = new File(fileName +".txt");
		if(file.exists()) {
	        file.delete();
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("./files/" + fileName, false));
			out.write("" + version);
			out.newLine();
			for(String s : doc){
				out.write(s);
				out.newLine();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Writes out the whole document
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
		//TODO:  
	}

	public void close(){
		save();
		/*
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
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
