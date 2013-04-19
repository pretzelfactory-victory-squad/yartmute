package common;

import common.server.*;
import common.client.*;


public class CommandFactory {
	public static Command getCommand(String s) throws Exception{
		String[] arg = s.split(":");
		switch(arg[0]){
		case "LIST":
			return new List(removeFirst(arg));
		case "CLOSE":
			return new Close(removeFirst(arg));
		case "OPEN":
			return new Open(removeFirst(arg));
		case "READ":
			return new Read(removeFirst(arg));
		case "WRITE":
			return new Write(removeFirst(arg));
		case "UPDATE":
			return new Update(removeFirst(arg));
		default:
			throw new Exception("Can't identify command.");
		}
	}
	public static Command getCommand(byte[] s) throws Exception{
		return getCommand(new String(s));
	}
	public static Command getCommand(char[] s) throws Exception{
		return getCommand(new String(s));
	}
	private static String[] removeFirst(String[] arg){
		String[] s = new String[arg.length-1];
		for(int i=1; i<arg.length; i++){
			s[i-1] = arg[i];
		}
		return s;
	}
	
}
