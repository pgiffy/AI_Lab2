import java.util.*;
//should be named graph or network, this wound up not technically being a tree
public class Graph {
	ArrayList<Node> nodes = new ArrayList<>();
	
	public Graph() {
        nodes = new ArrayList<>();
    }

	
	public void addNode(Node n) {
        nodes.add(n);
    }

    
    public Node getNode(int id) { return nodes.get(id); }
    public ArrayList<Node> getNodes(){ return nodes; }
    public int numNodes() { return nodes.size(); }
    
    //used to remove duplication errors in the tails of nodes.
    public ArrayList<Node> removeDuplicates(ArrayList<Node> remove){
    	Set<Node> temp = new HashSet<>();
    	temp.addAll(remove);
    	remove.clear();
    	remove.addAll(temp);
    	return remove;
    	
    }
    //initialization of classes can be seen below
 
    public ArrayList<Node> depthFirst(Node start) {
    	Stack<Node> stack = new Stack<>();
    	stack.add(start);
    	Node current;
    	while(!stack.isEmpty()) {
    		current = stack.pop();
    		if(current.content != '_') {
    			continue;
    		}
    		if(current.visited == true) {
    			continue;
    		}
    		if(current.getContent() == start.content) {
    			return current.tail;
    		}
    		current.visited = true;
    		current.tail.add(current);
    		for(Node n : current.friends) {
    			
    			n.tail.addAll(removeDuplicates(current.tail));
    			stack.add(n);
    		}
    		
    	}
    	return null;
    }
    
}
