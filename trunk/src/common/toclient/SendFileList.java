package common.toclient;

public class SendFileList extends ClientCommand {
	public static final String TYPE = "SLIST";

	public SendFileList(String[] arg) {
		super(arg);
		this.type = TYPE;
	}
	public SendFileList(String arg) {
		super(arg);
		this.type = TYPE;
	}

	public String[] getFileList(){
		return getArg(0).split("\\|");
	}
}
