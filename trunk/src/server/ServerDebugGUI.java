package server;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerDebugGUI extends JFrame{
	private static ServerDebugGUI instance;
	public JTextArea textArea;
	private JScrollPane scrollPane;
		
	private ServerDebugGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createTextArea();
		this.setSize(800, 600);
		setVisible(true);
	}

	private void createTextArea() {
		textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", Font.PLAIN, 14));
		textArea.setEditable(false);

		scrollPane = new JScrollPane(textArea);
		add(scrollPane);
	}
	
	public static ServerDebugGUI getInstance(){
		if(instance == null){
			instance = new ServerDebugGUI();
		}
		return instance;
	}
}