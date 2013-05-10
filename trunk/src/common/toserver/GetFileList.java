package common.toserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import common.toclient.SendFileList;

import server.ServerDoc;
import server.ServerDocHandler;


public class GetFileList extends ServerCommand {
	public static final String TYPE = "LIST_";

	public GetFileList() {
		super(new String[0]);
		this.type = TYPE;
	}
	@Override
	public void execute(BufferedWriter writer, ServerDoc doc) {
		List<String> l = ServerDocHandler.instance.getDocList();
		String arg = l.get(0);
		for(int i=1; i<l.size(); i++){
			arg += "|" + l.get(i);
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
