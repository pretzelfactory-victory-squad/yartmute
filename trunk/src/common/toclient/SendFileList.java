package common.toclient;

public class SendFileList extends ClientCommand {

	public SendFileList(String[] arg) {
		super(arg);
	}
	public SendFileList(String arg) {
		super(arg);
	}

	public String[] getFileList(){
		return arg[0].split("|");
	}
}
