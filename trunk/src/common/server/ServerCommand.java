package common.server;
import common.Command;

public abstract class ServerCommand extends Command{
	
	public ServerCommand(String[] arg){
		super(arg);
	}
}
