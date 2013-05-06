package server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServerDocHandler {
	File fileListFile = null;
	List<String> fileList = new ArrayList<String>();
		
	public ServerDocHandler(){
		fileListFile = new File("fiellist");
		//fileListFile.
	}
	
	public static synchronized ServerDoc getDoc(String filename){
		return new ServerDoc(filename);	
	}
	
	public static synchronized void save(){
		
	}
	
	public static List<String> getDocList(){
		/*
		 * Dummy code, glöm inte att faktiskt implementera
		 */
		List<String> fileList = new ArrayList<String>();
		fileList.add("Hej.txt");
		fileList.add("Haj.txt");
		return fileList;	
	}
}
