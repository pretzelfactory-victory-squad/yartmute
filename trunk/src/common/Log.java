package common;

public class Log {
	public static boolean debug = true;
	public static void debug(Object o){
		char[] c = o.toString().toCharArray();
		if(c.length > 0 && (c[c.length-1] == '\n' || c[c.length-1] == '\r')){
			System.out.print(c);
		}else{
			System.out.println(c);
		}
	}
}
