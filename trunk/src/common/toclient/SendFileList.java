package common.toclient;

public class SendFileList extends ClientCommand {

	public SendFileList(String[] arg) {
		super(arg);
		this.type = "SLIST";
	}
	public SendFileList(String arg) {
		super(arg);
		this.type = "SLIST";
	}

	public String[] getFileList(){
		return arg[0].split("|");
	}
}
