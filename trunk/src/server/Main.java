package server;

import java.io.File;
import java.net.URI;

import common.Log;

public class Main {
	public static final int STANDARD_PORT = 3790;

	public static void main(String[] args) {
		try {
			int port = STANDARD_PORT;
			
			URI uri = Main.class.getClassLoader().getResource(".").toURI();
			File folder = new File(new File(uri).getParent(), "files");
			
			if(args.length>0){
				port = Integer.parseInt(args[0]);
			}
			if(args.length>1){
				folder = new File(System.getProperty("user.dir"), args[1]);
				if(!folder.isDirectory()){
					throw new Exception(folder+" is not a folder");
				}
				Log.debug("folder: "+folder.getAbsolutePath());
			}
			
			if(args.length > 0){
				port = Integer.parseInt(args[0]);
			}
			new ServerDocHandler(folder);
			new ServerSocketHandler(port);
		} catch(Exception e){
			//e.printStackTrace();
			System.err.println(e.getMessage());
			System.err.println("Usage: java server.Main portnr folder");
			System.exit(-1);
		}
	}

}
