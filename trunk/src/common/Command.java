package common;

import java.util.ArrayList;

public abstract class Command {
	protected String type;
	private ArrayList<String> arg;
	
	public Command(ArrayList<String> arg){
		this.arg = arg;
	}
	
	public String toString(){
		return null;	
	}
	public String getType(){
		return type;
	}
	
}
