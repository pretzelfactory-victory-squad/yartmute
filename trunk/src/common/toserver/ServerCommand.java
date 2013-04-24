package common.toserver;
import java.io.BufferedWriter;

import common.Command;

public abstract class ServerCommand extends Command{
	
	public ServerCommand(String[] arg){
		super(arg);
	}
	
	public ServerCommand(String s){
		super(s);
	}
	
	public abstract void execute(BufferedWriter writer);
}
