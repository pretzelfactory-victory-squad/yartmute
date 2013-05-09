package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import common.toserver.Write;

public class ServerDoc {

	private long version;
	private List<StringBuilder> doc;
	private List<Write> writeCommands;
	private String fileName;

	// Create a new document and read it from harddrive
	public ServerDoc(String fileName) {
		this.fileName = fileName;
		doc = new ArrayList<StringBuilder>();
		writeCommands = new LinkedList<Write>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("./files/" + fileName));
			String line = br.readLine();
			if(line != null){
				// Read Version number
				version = Long.valueOf(line);
				line = br.readLine();
				// Read document
				while (line != null) {
					doc.add(new StringBuilder(line + "\n"));
					line = br.readLine();
				}
				System.out.println("Printing file '" + fileName + "'");
				for (StringBuilder s : doc) {
					System.out.println(s);
				}
				br.close();
			} else {
				BufferedWriter out = new BufferedWriter(new FileWriter("./files/" + fileName, false));
				out.write("0");
				out.flush();
				System.out.println("Created new document with filename '" + fileName + "'");
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	// Save the document to harddrive
	public synchronized void save(){
		File file = new File(fileName +".txt");
		if(file.exists()) {
			file.delete();
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("./files/" + fileName, false));
			out.write("" + version);
			out.newLine();
			for(StringBuilder s : doc){
				out.write(s.toString());
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
		for (StringBuilder s : doc) {
			output += s.toString();
		}
		return output;
	}

	public synchronized long getVerNbr() {
		return this.version;
	}


	public synchronized void write(Write command) {
		command.modify(writeCommands);
		writeCommands.add(command);
		String text = command.getArg(5);
		String[] lines = text.split("\n");
		int lineStart = Integer.valueOf(command.getArg(0));
		int lineEnd = Integer.valueOf(command.getArg(1));
		int slotStart = Integer.valueOf(command.getArg(3));
		int slotEnd = Integer.valueOf(command.getArg(4));

		if(lineStart==lineEnd){
			StringBuilder currentline = doc.get(lineStart);
			currentline.delete(slotStart, slotEnd);
			currentline.insert(slotStart, text);
		} else {
			//Add first line
			StringBuilder firstline = doc.get(lineStart);
			firstline.delete(slotStart, doc.size());
			firstline.insert(slotStart, text);
			//Remove lines in the middle
			for(int i =lineStart+1; lineStart < lineEnd; i++){
				doc.remove(i);
			}
			//Add the new lines.
			for(int i = lineStart+1; lineStart<lines.length-1; i++){
				doc.add(i, new StringBuilder(lines[i]));
			}	
			//Add the last line
			StringBuilder lastline = doc.get(lineEnd);
			lastline.delete(0, slotEnd);
			lastline.insert(lineEnd, text);
		}
		version++;
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
