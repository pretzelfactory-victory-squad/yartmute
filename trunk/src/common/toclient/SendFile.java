package common.toclient;


public class SendFile extends ClientCommand {

	public SendFile(String[] arg) {
		super(arg);
		this.type = "SFILE";
	}
	public SendFile(String arg) {
		super(arg);
		this.type = "SFILE";
	}

	public String getFile(){
		String filename = arg[0];
		long version = Long.parseLong(arg[1]);
		String contents = arg[2];
		return contents;
	}
	
	public String toString() {
		String command = "DOC__:" + arg[0] + ":" + arg[1] + ":" + arg[2];
		return command;		
	}
	
	private String escape(String string) {
		
		return null;
		/*string.replace("\", newChar)
		return;*/
	}
	
	private String unescape(String string) {
		
		return null;
		/*string.replace("\", newChar)
		return;*/
	}
}
