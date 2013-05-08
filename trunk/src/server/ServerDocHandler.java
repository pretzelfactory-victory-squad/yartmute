package server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

public class ServerDocHandler {
	static private TreeMap<String, ServerDoc> fileList = new TreeMap<String, ServerDoc>();
	
	
	public ServerDocHandler(){
		 String path = "./files"; 
		 File folder = new File(path);
		 File[] listOfFiles = folder.listFiles();
		 for(File f:listOfFiles) {
			 fileList.put(f.getName(), new ServerDoc(f.getName()));
		 }
	}
	
	public static synchronized ServerDoc getDoc(String filename){
		if(fileList.containsKey(filename)){
			return fileList.get(filename);
		} else {
			ServerDoc doc = new ServerDoc(filename);
			fileList.put(filename, doc);
			return doc;
		}
	}
	
	public static synchronized void saveAll(){
		Collection<ServerDoc> s = fileList.values();
		for(ServerDoc d: s){
			d.save();
		}	
	}
	
	public static List<String> getDocList(){	
		return new ArrayList<String>(fileList.keySet());	
	}
}
