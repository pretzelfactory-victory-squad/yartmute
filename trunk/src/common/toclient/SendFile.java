package common.toclient;


public class SendFile extends ClientCommand {

	public SendFile(String[] arg) {
		super(arg);
	}
	public SendFile(String arg) {
		super(arg);
	}

	public String getFile(){
		/*String[] split1 = arg[0].":");
		String filename = split1[0];
		String contents = unescape(split1[1]);
		
		return "hej hje";*/
		return null;
	}
	
	public String unescape(String string){
		return null;
		/*string.replace("\", newChar)
		return;*/
	}
}
