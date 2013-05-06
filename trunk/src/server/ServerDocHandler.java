package server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServerDocHandler {
	static List<String> fileList = new ArrayList<String>();
		
	public ServerDocHandler(){
		 String path = "./files"; 
		 File folder = new File(path);
		 File[] listOfFiles = folder.listFiles();
		 for(File f:listOfFiles) {
			 fileList.add(f.getName());
		 }
	}
	
	public static synchronized ServerDoc getDoc(String filename){
		return new ServerDoc(filename);	
	}
	
	public static synchronized void save(){
		
	}
	
	public static List<String> getDocList(){
		
		return fileList;	
	}
}
