package common.toserver;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import common.Command;
import common.toclient.SendFile;

import server.ServerDoc;
import server.ServerDocHandler;


public class Open extends ServerCommand {

	public Open(String[] arg) {
		super(arg);
		type = "OPEN";
	}
	
	public Open(String s){
		super(s);
		type = "OPEN";
	}

	@Override
	public void execute(BufferedWriter writer) {
		ServerDoc doc = ServerDocHandler.getDoc(arg[0]);
		Command c = new SendFile(doc.getDoc());
		try {
			writer.write(c.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
}
