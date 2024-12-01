import java.util.ArrayList;

public interface BTree {	
	public ArrayList<Integer> print();
	public Integer search(int key);
	public ArrayList<Integer> search(int start,int end);
	public void add(int key);
}
