package common.toserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import common.toclient.SendFileList;

import server.ServerDocHandler;


public class GetFileList extends ServerCommand {

	public GetFileList() {
		super(new String[0]);
		type = "LIST";
	}
	@Override
	public void execute(BufferedWriter writer) {
		List<String> l = ServerDocHandler.getDocList();
		String arg = "";
		// FIXA BORT SISTA PIPE:N ev.
		for(String s:l){
			arg = arg + s + "|";
		}
		SendFileList c = new SendFileList(arg);
		try {
			writer.write(c.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
