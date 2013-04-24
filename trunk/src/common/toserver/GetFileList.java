package common.toserver;

import java.io.BufferedWriter;
import java.util.List;

import common.toclient.SendFileList;

import server.ServerDocHandler;


public class GetFileList extends ServerCommand {

	public GetFileList(String[] arg) {
		super(arg);
		type = "LIST";
	}
	public GetFileList(){
		super(null);
		type = "LIST";	
	}
	@Override
	public void execute(BufferedWriter writer) {
		List<String> l = ServerDocHandler.getDocList();
		String arg = "";
		for(String s:l){
			arg = arg + s + "|";
		}
		SendFileList c = new SendFileList(arg);
		
	}
}
