import java.util.*;

public class Gird {
	ArrayList<Node> nodes = new ArrayList<>();
	
	public Gird() {
        nodes = new ArrayList<>();
    }

	
	public void addNode(Node n) {
        nodes.add(n);
    }
    
    //used to remove duplication errors in the tails of nodes.
    public ArrayList<Node> removeDuplicates(ArrayList<Node> remove){
    	Set<Node> temp = new HashSet<>();
    	temp.addAll(remove);
    	remove.clear();
    	remove.addAll(temp);
    	return remove;
    	
    }


}
