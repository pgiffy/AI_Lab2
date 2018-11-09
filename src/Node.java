import java.util.*;


//Self build Node class that can hold extra content
//all names inside are self explanitory.
public class Node {
	boolean visited = false;
	boolean start = false;
	char[] letters;
	ArrayList<Node> tail = new ArrayList<>();
    ArrayList<Node> friends = new ArrayList<>();
    ArrayList<Character> possibleLetters = new ArrayList<>();
    int id;
    int X;
    int Y;
    char content;
    

    public Node(int nodeId, char inside, int xSpot, int ySpot){ 
    	id = nodeId;
    	content = inside;
    	X = xSpot;
    	Y = ySpot;
    }
    
   
    
    public void setCharacters(char[] chars) {letters = chars;}
    
    public void setContent(char newContent) {content = newContent;}

    public int getId(){ return id; }
    
    public char getContent() { return content; }

    public void setId(int newId) { id = newId; }
    
    public int getX() { return X; }
    public int getY() { return Y; }
    
//    public int getConstrainedness() {
//    	return constrainedness;
//    }
    
    public String toString() { return "<" + id + ">"; }
}
