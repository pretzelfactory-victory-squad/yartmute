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

import org.apache.commons.lang3.StringUtils;

import server.exceptions.OutOfSyncException;
import common.Command;
import common.toserver.Write;

public class ServerDoc {

	private static final String END_OF_FILE = "\\END\\";
	private long version;
	private List<StringBuilder> doc;
	private List<Write> writeCommands;
	private String fileName;
	private List<BufferedWriter> users;
	
	public void addUser(BufferedWriter writer){
		users.add(writer);
	}
	public void removeUser(BufferedWriter writer){
		users.remove(writer);
	}

	
	public void sendCmdToConnectedUsers(Command c){
		for(BufferedWriter w : users){
			try {
				w.write(c.toString());
				w.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void copy(ServerDoc from){
		version = from.version;
		doc = from.doc;
		writeCommands = from.writeCommands;
		fileName = from.fileName;
	}
	
	// Create a new document and read it from harddrive
	public ServerDoc(String fileName) {
		users = new ArrayList<BufferedWriter>();
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
					if(line.equals(END_OF_FILE)){
						line = "";
						break;
					}
					doc.add(new StringBuilder(line));
					line = br.readLine();
				}
				//TODO: if last line is empty readLine will not read it
				
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
				String line = s.toString();
				out.write(s.toString());
				out.newLine();
			}
			out.write(END_OF_FILE);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Writes out the whole document
	public synchronized String getDoc() {
		StringBuilder output = new StringBuilder();
		for (StringBuilder s : doc) {
			output.append(s);
			output.append('\n');
		}
		output.delete(output.length()-1, output.length());
		return output.toString();
	}

	public synchronized long getVerNbr() {
		return this.version;
	}


	public synchronized void write(Write command) throws OutOfSyncException {
		command.modify(writeCommands);
		writeCommands.add(command);
		String text = command.getText();
		
		String[] lines = StringUtils.splitPreserveAllTokens(text, '\n'); // because String.split ignores trailing whitespace
		
		int lineStart = command.getLineStart();
		int lineEnd = command.getLineEnd();
		int slotStart = command.getSlotStart();
		int slotEnd = command.getSlotEnd();
		
		StringBuilder firstLine = doc.get(lineStart);
		StringBuilder lastLine = doc.get(lineEnd);

		if(lineStart != lineEnd || slotStart != slotEnd){
			for(int i =lineStart+1; i <= lineEnd; i++){
				doc.remove(lineStart+1);
			}

			firstLine.replace(slotStart, firstLine.length(), lastLine.substring(slotEnd));
		}
		
		if(lines.length == 0){ // no insertion, only delete
			
		}else if(lines.length == 1){ 	// single line insert (simplest case)
			firstLine.insert(slotStart, lines[0]);					
		}else{					// multi line insert
			String end = null;
			try{
				end = firstLine.substring(slotStart); 
			}catch(StringIndexOutOfBoundsException e){
				e.printStackTrace();
			}
			firstLine.replace(slotStart, firstLine.length(), lines[0]);
			for(int i = lineStart+1; i<lines.length-1; i++){
				doc.add(i, new StringBuilder(lines[i]));
			}
			doc.add(new StringBuilder(lines[lines.length-1] + end));
		}
		
		for(StringBuilder s : doc){
			System.out.println(s);
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
