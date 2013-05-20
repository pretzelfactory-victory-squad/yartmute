package common;

import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Baseclass for all commands sent to and from the server
 */
public class Command {
	protected String type;
	protected String[] arg;
	public static final char ARG_DIVIDER = '>';
	public static final char NEWLINE_REPLACEMENT = '<';
	
	public Command(String[] arg){
		this.arg = arg;
	}
	public Command(String a){
		arg = new String[1];
		arg[0] = a;
	}
	
	public String toString(){
		StringBuilder b = new StringBuilder(type);
		for(String s:arg){
			b.append(ARG_DIVIDER);
			b.append(escape(s));
		}
		b.append('\n');
		return b.toString();
	}
	public String getType(){
		return type;
	}
	protected String getArg(int i){
		return arg[i];
	}
	
	public static String escape(String string) {
		if(string == null){
			return null;
		}
		string = StringEscapeUtils.escapeHtml4(string);
		string = string.replace('\n', NEWLINE_REPLACEMENT);
		return string; //string.replace(">", "\\>").replace("<", "\\<").replace(':','>').replace('\n','<');
	}
	
	public static String unescape(String string) {
		if(string == null){
			return null;
		}
		string = string.replace(NEWLINE_REPLACEMENT, '\n');
		return StringEscapeUtils.unescapeHtml4(string); //string.replace("\\>",">").replace("\\<", "<").replace('>',':').replace('<', '\n');
	}
	
	public class CommandArgumentException extends IOException{
		public CommandArgumentException(String message){
			super(message);
		}
	}
}
