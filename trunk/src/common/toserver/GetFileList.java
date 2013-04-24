package common.toserver;


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
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	public String[] getFileList(){
		return arg[0].split("|");
	}
}
