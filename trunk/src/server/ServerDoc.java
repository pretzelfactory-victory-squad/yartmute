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

	/**
	 * Add a user connected to the document.
	 * @param writer Identifies a user.
	 * @return
	 */
	public int addUser(BufferedWriter writer){
		int id = users.add(writer);
		Log.debug("Added user with id:"+id);
		return id;
	}
	/**
	 * Removes a user from the document.
	 * @param writer Identifies a user.
	 */
	public void removeUser(BufferedWriter writer){
		Log.debug("Remove user");
		users.remove(writer);
	}

	/**
	 * Sends out a command to all user connected to this document.
	 * @param c Command to send out.
	 */
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

	/**
	 * Copy a document and "put" it in this
	 * @param from Document to copy from.
	 */
	public void copy(ServerDoc from){
		version = from.version;
		doc = from.doc;
		writeCommands = from.writeCommands;
		file = from.file;
		users = from.users;
	}

	/** Create a new document. Try to read the argument "file" from harddrive, 
	 *  Thorws a {@link FileNotFoundException} if the file not exist on harddrive.
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
			}
		} catch(FileNotFoundException e){
			throw e;
		}catch (IOException e) {
			Log.error(e);
		}

	}
	/**
	 *  Create a empty doc. This constructor shall only be used in conjunction with
	 *  "openFile" method for initiation.
	 * 
	 */
	public ServerDoc(){
		users = new UserList();
		this.file = null;
		doc = new ArrayList<StringBuilder>();
		writeCommands = new LinkedList<Write>();
	}
	/**
	 * Create a new file.
	 * @param file
	 */
	public synchronized void openFile(File file){
		this.file = file;
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
			out.write("0");
			out.flush();
			Log.debug("Created new document with filename '" + file.getName() + "'");
			out.close();
		} catch (Exception e){
			e.printStackTrace();
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

	/**
	 * Return the userview of the document.
	 * @return Document in userview
	 */
	public synchronized String getDoc() {
		if(doc.size() != 0){
			StringBuilder output = new StringBuilder();
			for (StringBuilder s : doc) {
				output.append(s);
				output.append('\n');
			}
			output.delete(output.length()-1, output.length());
			return output.toString();
		} else {
			return "";
		}
	}
	/**
	 * Return the version number.
	 * @return Version number of the document.
	 */

	public synchronized long getVerNbr() {
		return this.version;
	}
	/**
	 * Apply a write command to the document. Modify the command for be in sync with the document.
	 * @param command The command that writes to document.
	 * @throws OutOfSyncException Throws this if the input command is to Old for being applied to
	 * the document.
	 */


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
	/**
	 *  Return the filename of the document.
	 */
	public String toString(){
		return getFileName();
	}
	/**
	 * Compare if equal with to string as comparator.
	 */
	public boolean equals(Object obj){
		return toString().equals(obj.toString());
	}
}
