package common.server;


public class List extends ServerCommand {

	public List(String[] arg) {
		super(arg);
		type = "LIST";
	}
	public List(){
		super(null);
		type = "LIST";	
	}
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
