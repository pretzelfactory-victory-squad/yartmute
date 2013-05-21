package common;

/**
 * A class for logging errors, printing debug info and printing messages to the user. 
 */
public class Log {
	public static boolean debug = true;//true;
	public static boolean error = false;//true;
	public static boolean message = true;
	public static boolean debugGUI = false;//true;
	
	public static void message(Object o){
		if(message){
			print(o);
		}
	}
	public static void debug(Object o){
		if(debug){
			print(o);
		}
	}
	public static void error(Object o){
		if(error){
			print(o);
		}
	}
	private static void print(Object o){
		if(o instanceof Exception){
			((Exception)o).printStackTrace();
		}
		char[] c = o.toString().toCharArray();
		if(c.length > 0 && (c[c.length-1] == '\n' || c[c.length-1] == '\r')){
			System.out.print(c);
		}else{
			System.out.println(c);
		}
	}
}
