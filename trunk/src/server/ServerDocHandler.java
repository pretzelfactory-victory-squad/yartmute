package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

public class ServerDocHandler {
	static private TreeMap<String, ServerDoc> fileList = new TreeMap<String, ServerDoc>();
	
	public static ServerDocHandler instance;
	private File folder;
	/**
	 * Set up a handler which import files from the given folder.
	 * @param folder the relative search path eg. "myfiles/ultimatefolder"
	 */
	public ServerDocHandler(File folder){
		this.folder = folder;
		File[] listOfFiles = folder.listFiles();
		for(File f:listOfFiles) {
			try{
				fileList.put(f.getName(), new ServerDoc(f));
			}catch(FileNotFoundException e){
				System.err.println("Unable to open file "+f+": "+e.getMessage());
			}
		}
		System.err.println();
		instance = this;
	}
	/**
	 * Returns a given document.
	 * @param filename filename of the document.
	 * @return Returns the ServerDoc for the given filename.
	 * @throws FileNotFoundException returns if file not found.
	 */
	public synchronized ServerDoc getDoc(String filename) throws FileNotFoundException{
		if(fileList.containsKey(filename)){
			return fileList.get(filename);
		} else {
			ServerDoc doc = new ServerDoc(new File(folder, filename));
			fileList.put(filename, doc);
			return doc;
		}
	}
	/**
	 * Create a new document with given filename.
	 * @param filename filename of the document.
	 * @return Returns the ServerDoc for the given filename.
	 */
	public synchronized ServerDoc newDoc(String filename){
		ServerDoc doc = new ServerDoc();
		doc.openFile(new File(folder, filename));
		fileList.put(filename, doc);
		return doc;
		
	}
	/**
	 * Save all open documents.
	 */
	public synchronized void saveAll(){
		Collection<ServerDoc> s = fileList.values();
		for(ServerDoc d: s){
			d.save();
		}	
	}
	/**
	 *  Returns a list of all open document.
	 * 
	 * @return
	 */
	public List<String> getDocList(){	
		return new ArrayList<String>(fileList.keySet());	
	}
}
