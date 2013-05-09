package common;

public class Command {
	protected String type;
	protected String[] arg;
	
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
			b.append(':');
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
		return string.replace(':','>').replace('\n','<');
	}
	
	public static String unescape(String string) {
		if(string == null){
			return null;
		}
		return string.replace('<', '\n').replace('>',':');
	}
}
