package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import server.exceptions.OutOfSyncException;
import common.Command;
import common.Log;
import common.toserver.Write;

public class ServerDoc {

	private static final String END_OF_FILE = "\\END\\";
	private long version;
	private List<StringBuilder> doc;
	private List<Write> writeCommands;
	private File file;
	private UserList users;
	
	public int addUser(BufferedWriter writer){
		int id = users.add(writer);
		Log.debug("Added user with id:"+id);
		return id;
	}
	public void removeUser(BufferedWriter writer){
		Log.debug("Remove user");
		users.remove(writer);
	}

	
	public void sendCmdToConnectedUsers(Command c){
		for(BufferedWriter w : users){
			try {
				w.write(c.toString());
				w.flush();
			} catch(SocketException e){
				Log.debug("Socket closed");
				removeUser(w);
			}catch (IOException e) {
				Log.error(e);
			}
		}
	}

	public void copy(ServerDoc from){
		version = from.version;
		doc = from.doc;
		writeCommands = from.writeCommands;
		file = from.file;
		users = from.users;
	}
	
	/** Create a new document. Try to read the argument "file" from harddrive, 
	 *  if it not exist a new document is created. 
	 * 
	 * @param file
	 * Filename for the document.
	 */
	public ServerDoc(File file) throws FileNotFoundException {
		users = new UserList();
		this.file = file;
		doc = new ArrayList<StringBuilder>();
		writeCommands = new LinkedList<Write>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
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
				
				Log.debug("Printing file '" + file.getName() + "'");
				for (StringBuilder s : doc) {
					Log.debug(s);
				}
				br.close();
			} else {
				BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
				out.write("0");
				out.flush();
				Log.debug("Created new document with filename '" + file.getName() + "'");
				out.close();
			}
		} catch(FileNotFoundException e){
			throw e;
		}catch (IOException e) {
			Log.error(e);
		}

	}
	/** Save the document/object to disk
	 */
	public synchronized void save(){
		if(file.exists()) {
			file.delete();
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
			out.write("" + version);
			out.newLine();
			for(StringBuilder s : doc){
				out.write(s.toString());
				out.newLine();
			}
			out.write(END_OF_FILE);
			out.flush();
			out.close();
		} catch (IOException e) {
			Log.error(e);
		}
		Log.debug("Document " + file.getName() +" saved to disk.");
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
				end = firstLine.substring(slotEnd); 
			}catch(StringIndexOutOfBoundsException e){
				Log.error(e);
			}
			firstLine.replace(slotStart, firstLine.length(), lines[0]);
			for(int i = 1; i <= lines.length-2; i++){
				int docLine = lineStart + i;
				doc.add(docLine, new StringBuilder(lines[i]));
			}
			int insertLastLineAt = lineStart+lines.length-1;
			doc.add(insertLastLineAt, new StringBuilder(lines[lines.length-1] + end));
		}
		

		if(Log.debugGUI){
			ServerDebugGUI gui = ServerDebugGUI.getInstance();
			gui.textArea.setText("");
			for(StringBuilder s : doc){
				gui.textArea.append(s+"\n");
			}
		}
		version++;
	}
	public String getFileName() {
		return file.getName();
	}
	public String toString(){
		return getFileName();
	}
	public boolean equals(Object obj){
		return toString().equals(obj.toString());
	}
}
