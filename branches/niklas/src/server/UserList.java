package server;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UserList implements Iterable<BufferedWriter>{
	List<BufferedWriter> users = new ArrayList<BufferedWriter>();
	List<Integer> ids = new ArrayList<Integer>();
	private int counter = 0;
	public int add(BufferedWriter user){
		counter++;
		users.add(user);
		ids.add(counter);
		return counter;
	}
	public void remove(BufferedWriter writer) {
		int i = users.indexOf(writer);
		users.remove(i);
		ids.remove(i);
	}
	@Override
	public Iterator<BufferedWriter> iterator() {
		return users.iterator();
	}
}
