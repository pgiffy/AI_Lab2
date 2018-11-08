import java.util.*;
import java.io.*;

public class Main {
	
	/*Notes Section:
	 * Done by Peter Gifford and Sam Behrens
	 * 
	 * Define Flow Free as CSP : Done - Sam Check
	 * 		Variables : 	X sub(ij)  -- i is rox and j is column
	 * 		Domain : 		{ Set of letters present in the given graph } - GOOD
	 * 		Constraints : 	All spots must be only one color. (check this for wording before putting in report)
	 * 				Maybe:	Each color must be touching another of its own color twice unless it is a start/end node in which case it can only touch one of its own color
	 * 						(i,j) -> (i+1,j),(i-1,j),(i,j+1),(i,j-1) -> At least one of which must be the same as (i,j) but no more than two can be the same)
	 * 
	 * 
	 * 
	 * 
	 * Heuristics Chosen : None
	 * 
	 * Report : Not Done
	 * 
	 * */
	
	static int steps = 0;

	public static void main(String[] args) {

		Scanner scan;
		String fileOut = "output.txt";
		PrintWriter out;
		String fileName = "14x14maze.txt";

		try {
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
				
				// generation of the "tree" should be called network or graph
				Tree maze = new Tree();
				for (int y = 0; y < yLength; y++) {
					for (int x = 0; x < xLength; x++) {
						Node current = totalMaze[y][x];
						maze.nodes.add(current);
						// checking all cardinal directions since can't move diagonal
						// So technically this try catch is not super clean but it will catch all the cases and the alternative is a bunch of if statemtents that works just as effectively
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
				
				// tack on the letters to every node rather than the loops
				updatePossibleValues(maze);
				
				long startTime = System.nanoTime();
				if (backTracking(maze)) { // start the backtracking!
			        System.out.println("Found Solution");
			    }  
			    else { 
			        System.out.println("No solution"); 
			    } 
				System.out.println("steps: " + steps); 
				long endTime   = System.nanoTime();
				long totalTime = endTime - startTime;
				long seconds = totalTime/1000000;
				System.out.println(seconds + "ms");

				for (int i = 0; i < yLength; i++) { // write the final solution to output.txt
					for(int j = 0; j < xLength; j++) {
						System.out.print(totalMaze[i][j].content);
					}
					System.out.println();
				}

				out.close();//close file
			} catch(FileNotFoundException e) {
		}	
	}
	
	/*
	 * Smart Solution
	 */
	
	/*
	 * The smart version of our solution that uses Heuristics to improve on the performance of our dumb approach
	 */
	
	public static void updatePossibleValues(Tree board) {
		for (Node n : board.nodes) {
			if (!n.start && !n.visited) {
				updateNode(n);
			}
		}
    	for (Node n : board.nodes) { // reseting all nodes to not be visited for next time we have to check 
    		if (n.visited) n.visited = false;
    	}
	}
	
	public static void updateNode(Node c) {
		// getting the possible values are all colors that can be reached by traversing empty spaces
		Stack<Node> stack = new Stack<>();
    	stack.add(c);
    	Node current;
    	while(!stack.isEmpty()) {
    		current = stack.pop();
    		if(current.content != '_') {
    			continue;
    		}
    		if(current.visited == true) {
    			continue;
    		}
    		current.visited = true;
    		c.tail.add(current);
    		for(Node n : current.friends) {
    			if (n.content != '_') {
    				if (!c.leet.contains(n.content)) c.leet.add(n.content);
    			} else {
    				stack.add(n);
    			}
    		}
    	}
    	for (Node n : c.tail) { // Set all nodes in the path of c to 
    		n.leet = c.leet;
    	}
	}
	
	public static boolean isSafeSmart(Node current, char color) { 
		
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
	 * Main Smart Backtracking method
	 * Params: a tree which is really a graph of nodes that all have value of a char
	 * Returns: boolean, true if the board is complete, false if it cannot find a solution
	 */

	public static boolean backTrackingSmart(Tree board) { 
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
	    
	    //updatePossibleValues(board);
	    
	    
	    // printMaze(board);
	    // no '_' space left 
	    if (isComplete) return true; 
	    
//	    for (Node n : board.nodes) { // me trying to get the most constrained next, which doesn't have any effect
//	    	if (n.content == '_' && n.leet.size() < current.leet.size()) {
//	    		current = n;
//	    	}
//	    }
	    
	    //enter letters
	    for (Character c : current.leet) {// instead of looping maybe try one at a time and then updating leet??
	    	if (isSafeSmart(current, c)) {
	    		current.content = c;
	    		if (backTrackingSmart(board)) {
	    			return true;
	    		} else {
	    			current.content = '_';
	    		}
	    	}
	    }
		
	    return false; 
	} 
	
	public static void printMaze(Tree maze) {
		int c = 0;
		int w = 5;
		for (Node n : maze.nodes) {
			System.out.print(n.content);
			c++;
			if (c % w == 0) {
				System.out.println("");
			}
		}
		System.out.println("");
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
	 * Params: a tree which is really a graph of nodes that all have value of a char
	 * Returns: boolean, true if the board is complete, false if it cannot find a solution
	 */

	public static boolean backTracking(Tree board) { 
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
	    	if (isSafe(current, c)) {
	    		current.content = c;
	    		if (backTracking(board)) {
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
