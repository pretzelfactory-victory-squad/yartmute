package common.toserver;
import java.io.BufferedWriter;

import common.Command;

public abstract class ServerCommand extends Command{
	
	public ServerCommand(String[] arg){
		super(arg);
	}
	public abstract void execute(BufferedWriter writer);
}
