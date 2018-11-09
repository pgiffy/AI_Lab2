import java.util.*;
import java.io.*;

public class Main {
	
	/*
	 * Created by Peter Gifford and Sam Behrens
	 * For Artificial Intelligence at Montana State University
	 * Assignment 2
	 */
	
	static int steps = 0; // keep track of how many checks are done by backtracking search

	public static void main(String[] args) {

		Scanner scan;
		String fileOut = "output.txt"; // output file
		PrintWriter out;
		String fileName = args[0];

		try { // try catch for the output file in case it does not exist
			
			/*
			 * This next chunk just scanning in the input file and reading in the board to a graph
			 */
			
			out = new PrintWriter(new File(fileOut));

			File gridFile = new File(fileName);
			scan = new Scanner(gridFile);
			int xLength = 0;
			int yLength = 0;// adjusting for reading first line for x
			while (scan.hasNextLine()) {
				xLength = scan.nextLine().length();
				yLength++;
			}
			int idIter = 0;
			int perLineIter = 0;
			int lineIter = 0;

			scan = new Scanner(gridFile);// reseting scanner to the top
			Node[][] totalMaze = new Node[yLength][xLength];
			while (scan.hasNext()) {
				String currentLine = scan.nextLine();
				perLineIter = 0;
				for (char con : currentLine.toCharArray()) {
					totalMaze[lineIter][perLineIter] = new Node(idIter, con, perLineIter, lineIter);
					if (totalMaze[lineIter][perLineIter].content != '_') totalMaze[lineIter][perLineIter].start = true;
					idIter++;
					perLineIter++;
				}
				lineIter++;
				// totalMaze has nodes of all 
			}
			
			// generation of the graph
			Graph maze = new Graph();
			for (int y = 0; y < yLength; y++) {
				for (int x = 0; x < xLength; x++) {
					Node current = totalMaze[y][x];
					maze.nodes.add(current);
					// checking all cardinal directions since can't move diagonal
					// So technically this try catch is not super clean but it will catch all the cases and the alternative is a bunch of if statements that works just as effectively
					try {
						current.friends.add(totalMaze[y+1][x]);
						current.friends.add(totalMaze[y][x+1]);
						current.friends.add(totalMaze[y-1][x]);
						current.friends.add(totalMaze[y][x-1]);
					} catch(ArrayIndexOutOfBoundsException e) {
						try {
							current.friends.add(totalMaze[y][x+1]);
							current.friends.add(totalMaze[y-1][x]);
							current.friends.add(totalMaze[y][x-1]);
						} catch(ArrayIndexOutOfBoundsException r) {
							try {
								current.friends.add(totalMaze[y-1][x]);
								current.friends.add(totalMaze[y][x-1]);
							} catch(ArrayIndexOutOfBoundsException t) {
								try {
									current.friends.add(totalMaze[y][x-1]);
								} catch(ArrayIndexOutOfBoundsException u) {
										
								}
							}
						}
					}
				}
			}
			
			/*
			 * End of reading in chunk
			 */
			
			for (Node n : maze.nodes) { // remove duplicates from friends
				Set<Node> temp = new HashSet<>();
				temp.addAll(n.friends);
				n.friends.clear();
				n.friends.addAll(temp);
			}
			
			ArrayList<Character> allLetters = new ArrayList<>();
			for (Node n : maze.nodes) { // go through the nodes and get all the possible letters that a node could be
				if(!allLetters.contains(n.content) && n.content != '_') {
					allLetters.add(n.content);
				}
			}
			String allLettersString = ""; // temporary var to convert Character set to string and then to char array
			for (Character c : allLetters) { // add the possible letters to a string to a 
				allLettersString += c;
			}
			char[] letters = allLettersString.toCharArray(); // then take that string and convert it to a char array
			
			for (Node n : maze.nodes) { // go through all nodes and set their possible letters to the letter array
				n.setCharacters(letters);
			}
			
			updatePossibleValues(maze); // set all possible values for nodes and order them
			
			long startTime = System.nanoTime(); // Start time for performance check
			
			if (backtrackingDumb(maze)) { // start the backtracking!
		        System.out.println("Found Solution");
		    }  
		    else { 
		        System.out.println("No solution"); 
		    } 
			System.out.println("steps: " + steps); 
			
			long endTime   = System.nanoTime(); // End time for performance check
			long totalTime = endTime - startTime; // Total elapsed time
			long seconds = totalTime/1000000; // convert to seconds
			System.out.println(seconds + "ms");

			for (int i = 0; i < yLength; i++) { // write the final solution to output.txt
				for(int j = 0; j < xLength; j++) {
					out.print(totalMaze[i][j].content);
				}
				out.println();
			}

			out.close();//close file
		} catch(FileNotFoundException e) {
		}
	}
	
	/*
	 * Smart Solution
	 * 
	 * The smart version of our solution that uses Heuristics to improve on the performance of our dumb approach
	 */
	
	/*
	 * Runs through all nodes that are not visited and are not a starting node and updates their possible values
	 * Once that is complete, the nodes are set to not visited for future updates of nodes
	 * params {board: Graph} the current board
	 */
	
	public static void updatePossibleValues(Graph board) {
		for (Node n : board.nodes) {
			if (!n.start && !n.visited) {
				updateNode(n);
			}
		}
    	for (Node n : board.nodes) { // reseting all nodes to not be visited for next time we have to check 
    		if (n.visited) n.visited = false;
    	}
	}
	
	/*
	 * Runs a depth first implementation to get all possible colors for the current node 
	 * by traversing empty spaces and keeps track of all other nodes that were in its path
	 * then updates each node in the path to have the same possible values
	 * params {c: Node} the node to update possible values of
	 */
	
	public static void updateNode(Node c) {
		Stack<Node> stack = new Stack<>();
    	stack.add(c); // add the starting node to the stack
    	Node current;
    	while(!stack.isEmpty()) {
    		current = stack.pop();
    		if(current.content != '_') { // if the current space is not empty then continue
    			continue;
    		}
    		if(current.visited == true) { // if the current node is visited then continue so that we don't endlessly search
    			continue;
    		}
    		current.visited = true; // set the current node to visited so we do not endlessly search
    		c.tail.add(current);
    		for(Node n : current.friends) { // check all of the adjacent spaces
    			if (n.content != '_') { // if they contain a color, then add that color to the list if it is not already in the list
    				if (!c.possibleLetters.contains(n.content)) c.possibleLetters.add(n.content);
    			} else {
    				stack.add(n); // add all adjacent spaces to the stack if they are empty
    			}
    		}
    	}
    	for (Node n : c.tail) { // Set all nodes in the path of c to have the same possible values
    		n.possibleLetters = c.possibleLetters;
    	}
	}
	
	/*
	 * Checks to see if it is safe to set a node's content to the passed in color
	 * params {current: Node, color: char} the node to check and the color to check it against
	 * Returns: boolean, true if it is safe to set node.content to the color param, false if not
	 */
	
	public static boolean isSafeSmart(Node current, char color) { 
		
		if (current.content != '_') { // if there is already a letter there then it cannot be checked
			return false;
		}
		
		
		int checker = 0;
		int numOfSameColor = 0;
		for(Node n : current.friends) {
			if (n.content == '_') { //if its blank we don't need to check it
				continue;
			}
			

			//this whole next section is just checking for if the nodes next to the current one will have at least two ways out if the current node is set to the current color option
			
			if (n.content == color) { //checking if the friends color is the same as the one that we are checking if it is ok to put there because if it is then the current node will be giving it a path out
				checker++;
				numOfSameColor++;
			}
			for (Node e : n.friends) { // go through all of current nodes friends' friends - except for the current node itself hence the check above
				if (e == current) {
					continue;
				}
				if (e.content == n.content) { // one of the other nodes has the same color
					checker++;
					numOfSameColor++;
				}
				if (e.content == '_') { // a blank space is still an opportunity to get out
					checker++;
				}
			}
			if (n.start && (checker == 0 || numOfSameColor > 1)) { // check for start nodes since they only need one way out
				return false;
			}
			if (!n.start && (checker < 2 || numOfSameColor > 2)) { // check for all the other nodes
				return false;
			}
			checker = 0;
			numOfSameColor = 0;
			
			
		}
		
		for (Node e : current.friends) { // check like the one above, but for the current node (will is get trapped)
			if (e.content == color) {
				checker++;
				numOfSameColor++;
			}
			if (e.content == '_') {
				checker++;
			}
		}
		if (current.start && (checker == 0 || numOfSameColor > 1)) { // I realize we dont need this but lets leave it anyway
			return false;
		}
		if (!current.start && (checker < 2 || numOfSameColor > 2)) {
			return false;
		}
		
		// if there is no clash, it's safe 
		return true; 
	} 
	
	/*
	 * Main Smart Backtracking method
	 * Params {board: Graph} a Graph of nodes that all have a char as content
	 * Returns: boolean, true if the board is complete, false if it cannot find a solution
	 */

	public static boolean backTrackingSmart(Graph board) { 
		// so it looks like what I need to do is figure out how to get the backtracking to through one at a time and check each character in leet and then update the possible options after
		steps++;
		Node current = null;
	    boolean isComplete = true;
	    for (Node n : board.nodes) { // checking if board is complete i.e. no spaces with '_'
	    	
            if (n.content == '_')  { 
            	current = n;
                // we still have some remaining missing values
            	isComplete = false;  
                break; 
            }
	        if (!isComplete) break; 
	    }
	    
	    // no '_' space left 
	    if (isComplete) return true; 
	    
	    //enter letters
	    for (Character c : current.possibleLetters) { // run through all possible letters set by updatePossibleValues
	    	if (isSafeSmart(current, c)) { // check if each one is safe
	    		current.content = c; // if safe, set the color to the current possible color
	    		if (backTrackingSmart(board)) { // run backtracking and return true if the backtracking function returned true
	    			return true;
	    		} else {
	    			current.content = '_'; // reset the value to empty because backtracking failed
	    		}
	    	}
	    }
		
	    return false; 
	} 
	
	/*
	 * end of Smart version
	 */
	
	/*
	 * Dumb solution
	 */
	
	/*
	 * Method for checking if a spot on the board is safe to set as a certain color or not
	 * Also handles all of the heuristics
	 * Params: the current node or 'space' on the graph that we are checking and the current color that we are wanting to check is safe to put there
	 * Returns: boolean, true if it is safe to place that color in that spot and false if not
	 */
	
	public static boolean isSafe(Node current, char color) { 
		
		if (current.content != '_') { // if there is already a letter there then it cannot be checked
			return false;
		}
		
		
		int checker = 0;
		int numOfSameColor = 0;
		for(Node n : current.friends) {
			if (n.content == '_') { //if its blank we dont need to check it
				continue;
			}
			

			//this whole next section is just checking for if the nodes next to the current one will have at least two ways out if the current node is set to the current color option
			
			if (n.content == color) { //checking if the friends color is the same as the one that we are checking if it is ok to put there because if it is then the current node will be giving it a path out
				checker++;
				numOfSameColor++;
			}
			for (Node e : n.friends) { // go through all of current nodes friends' friends - except for the current node itself hence the check above
				if (e == current) {
					continue;
				}
				if (e.content == n.content) { // one of the other nodes has the same color
					checker++;
					numOfSameColor++;
				}
				if (e.content == '_') { // a blank space is still an opportunity to get out
					checker++;
				}
			}
			if (n.start && (checker == 0 || numOfSameColor > 1)) { // check for start nodes since they only need one way out
				return false;
			}
			if (!n.start && (checker < 2 || numOfSameColor > 2)) { // check for all the other nodes
				return false;
			}
			checker = 0;
			numOfSameColor = 0;
			
			
		}
		
		for (Node e : current.friends) { // check like the one above, but for the current node (will is get trapped)
			if (e.content == color) {
				checker++;
				numOfSameColor++;
			}
			if (e.content == '_') {
				checker++;
			}
		}
		if (current.start && (checker == 0 || numOfSameColor > 1)) { // I realize we dont need this but lets leave it anyway
			return false;
		}
		if (!current.start && (checker < 2 || numOfSameColor > 2)) {
			return false;
		}
		
		// if there is no clash, it's safe 
		return true; 
	} 
	
	/*
	 * Main Backtracking method
	 * Params: a graph of nodes that all have value of a char
	 * Returns: boolean, true if the board is complete, false if it cannot find a solution
	 */

	public static boolean backtrackingDumb(Graph board) { 
		steps++;
		Node current = null;
	    boolean isComplete = true;
	    for (Node n : board.nodes) { // checking if board is complete i.e. no spaces with '_'
	            if (n.content == '_')  { 
	            	current = n;
	                // we still have some remaining missing values
	            	isComplete = false;  
	                break; 
	            }
	        if (!isComplete) break; 
	    }
	    
	    // no '_' space left 
	    if (isComplete) return true; 

	   //enter letters
	    
	    for (char c : current.letters) {// personalize data
	    	if (isSafe(current, c)) { // if safe set content to possible color
	    		current.content = c;
	    		if (backtrackingDumb(board)) {
	    			return true;
	    		} else {
	    			current.content = '_';
	    		}
	    	}
	    }
	    return false; 
	} 
	
	/*
	 * End of dumb solution
	 */
}
