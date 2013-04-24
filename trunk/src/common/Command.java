package common;

public class Command {
	protected String type;
	private String[] arg;
	
	
	public Command(String[] arg){
		this.arg = arg;
	}
	public String toString(){
		StringBuilder b = new StringBuilder(type);
		for(String s:arg){
			b.append(':');
			b.append(s);
		}
		return b.toString();
	}
	public String getType(){
		return type;
	}
}
