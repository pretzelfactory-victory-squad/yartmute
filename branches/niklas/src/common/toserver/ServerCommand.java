package common.toserver;
import java.io.BufferedWriter;

import server.ServerDoc;
import server.exceptions.ServerExeptions;

import common.Command;

public abstract class ServerCommand extends Command{
	public ServerDoc result;
	
	public ServerCommand(String[] arg){
		super(arg);
	}
	
	public ServerCommand(String s){
		super(s);
	}
	
	public abstract void execute(BufferedWriter writer, ServerDoc doc) throws ServerExeptions;
}
