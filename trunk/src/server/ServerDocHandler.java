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

	public synchronized ServerDoc getDoc(String filename) throws FileNotFoundException{
		if(fileList.containsKey(filename)){
			return fileList.get(filename);
		} else {
			ServerDoc doc = new ServerDoc(new File(folder, filename));
			fileList.put(filename, doc);
			return doc;
		}
	}
	public synchronized ServerDoc newDoc(String filename){
		ServerDoc doc = new ServerDoc();
		doc.openFile(new File(folder, filename));
		fileList.put(filename, doc);
		return doc;
		
	}
	public synchronized void saveAll(){
		Collection<ServerDoc> s = fileList.values();
		for(ServerDoc d: s){
			d.save();
		}	
	}
	public List<String> getDocList(){	
		return new ArrayList<String>(fileList.keySet());	
	}
}
