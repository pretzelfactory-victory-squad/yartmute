package common.toserver;


public class ListCommand extends ServerCommand {

	public ListCommand(String[] arg) {
		super(arg);
		type = "LIST";
	}
	public ListCommand(){
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
