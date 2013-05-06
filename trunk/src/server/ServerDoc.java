package server;

import java.util.ArrayList;
import java.util.List;

public class ServerDoc {

	private long version;
	private List<String> doc;
	private String fileName;

	public ServerDoc(String fileName) {
		this.fileName = fileName;
		doc = new ArrayList<String>();
	}

	public synchronized String getDoc() {
		String output = "SHEEEEEEIT";
		/*for (String s : doc) {
			output += s;
		}*/
		return output;
	}

	public synchronized long getVerNbr() {
		return this.version;
	}

	public synchronized void write(String filename) {

	}

	public static synchronized String getFileName() {
		return null;
	}
}
