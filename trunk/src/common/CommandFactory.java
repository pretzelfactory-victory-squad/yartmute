package common;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import common.toclient.*;
import common.toserver.*;


public class CommandFactory {
	
	/**
	 * Create a command from a string
	 * @param s The string
	 * @return The created command
	 * @throws MalformedCommandException
	 */
	public static Command getCommand(String s) throws MalformedCommandException{
		String[] arg = StringUtils.splitPreserveAllTokens(s, Command.ARG_DIVIDER);
		
		for(int i=0; i<arg.length; i++){
			arg[i] = Command.unescape(arg[i]);
		}
		
		switch(arg[0]){
		case GetFileList.TYPE:
			return new GetFileList();
		case SendFileList.TYPE:
			return new SendFileList(removeFirst(arg));
		case Open.TYPE:
			return new Open(removeFirst(arg));
		case Read.TYPE:
			return new Read(removeFirst(arg));
		case Write.TYPE:
			return new Write(removeFirst(arg));
		case Update.TYPE:
			return new Update(removeFirst(arg));
		case SendFile.TYPE:
			return new SendFile(removeFirst(arg));
		case Create.TYPE:
			return new Create(removeFirst(arg));
		default:
			throw new MalformedCommandException("Can't identify command: "+arg[0]);
		}
	}
	
	/**
	 * Create a command from a byte stream
	 * @param s The bytestream
	 * @return The created command
	 * @throws MalformedCommandException
	 */
	public static Command getCommand(byte[] s) throws MalformedCommandException{
		return getCommand(new String(s));
	}
	
	/**
	 * Create a command from a char array
	 * @param s The char array
	 * @return The created command
	 * @throws MalformedCommandException
	 */
	public static Command getCommand(char[] s) throws MalformedCommandException{
		return getCommand(new String(s));
	}
	
	/**
	 * Removes the first arg (containing the command type) from the array
	 * @param arg the arguments
	 */
	private static String[] removeFirst(String[] arg){
		String[] s = new String[arg.length-1];
		for(int i=1; i<arg.length; i++){
			s[i-1] = arg[i];
		}
		return s;
	}
	
	public static class MalformedCommandException extends RuntimeException{
		public MalformedCommandException(String string) {
			super(string);
		}
	}
}
