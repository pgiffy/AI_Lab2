import java.util.*;


//Self build Node class that can hold extra content
//all names inside are self explanatory.
public class Node {
	public boolean visited = false;
	ArrayList<Node> tail = new ArrayList<>();
	ArrayList<Node> friends = new ArrayList<>();
    public int id;
    public int X;
    public int Y;
    public char content;
    ArrayList<Node> parents = new ArrayList<>();
    public Node end;

    public Node(int nodeId, char inside, int xSpot, int ySpot){ 
    	id = nodeId;
    	content = inside;
    	X = xSpot;
    	Y = ySpot;
    }
    
    public int getManhattan() {return Math.abs(X - end.X) + Math.abs(Y - end.Y);}
    
    public String toString() { return "<" + id + ">"; }
}
