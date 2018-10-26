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
	 * FUN FACT: This is not a recursive back tracking problem. I'm changing it to something else. But wait it still is so you are wrong!
	 * 
	 * 
	 * Backtracking search : Just need to define quitting condition that all spaces are filled and *!*!*!*!**!All start and ends are connected/paths are full
	 * 
	 * Heuristics Chosen : None
	 * 
	 * Report : Not Done
	 * 
	 * */

	public static void main(String[] args) {

		Scanner scan;
		String fileOut = "output.txt";
		PrintWriter out;
		String fileName = "5x5maze.txt";

		try {
			out = new PrintWriter(new File(fileOut));

				File gridFile = new File(fileName);
				scan = new Scanner(gridFile);
				int xLength = 0;
				int yLength = 0;//adjusting for reading first line for x
				while (scan.hasNextLine()) {
					xLength = scan.nextLine().length();
					yLength++;
				}
				int idIter = 0;
				int perLineIter = 0;
				int lineIter = 0;

				scan = new Scanner(gridFile);//reseting scanner to the top
				Node[][] totalMaze = new Node[yLength][xLength];
				while (scan.hasNext()) {
					String currentLine = scan.nextLine();
					perLineIter = 0;
					for (char con : currentLine.toCharArray()) {
						totalMaze[lineIter][perLineIter] = new Node(idIter, con, perLineIter, lineIter);
						if(totalMaze[lineIter][perLineIter].content != '_') totalMaze[lineIter][perLineIter].start = true;
						idIter++;
						perLineIter++;
					}
					lineIter++;
					//totalMaze has nodes of all 
				}
				
				//generation of the "tree" should be called network or graph
				Tree maze = new Tree();
				for (int y = 0; y < yLength; y++) {
					for (int x = 0; x < xLength; x++) {
						Node current = totalMaze[y][x];
						maze.nodes.add(current);
							//checking all cardinal directions since can't move diagonal
						//so i know this is dumb but its the fastest thing to type and think of at the moment so change it if you want
						try {
						current.friends.add(totalMaze[y+1][x]);
						maze.addNode(totalMaze[y+1][x]);
						current.friends.add(totalMaze[y][x+1]);
						maze.addNode(totalMaze[y][x+1]);
						current.friends.add(totalMaze[y-1][x]);
						maze.addNode(totalMaze[y-1][x]);
						current.friends.add(totalMaze[y][x-1]);
						maze.addNode(totalMaze[y][x-1]);
						} catch(ArrayIndexOutOfBoundsException e) {
							try {
								current.friends.add(totalMaze[y][x+1]);
								maze.addNode(totalMaze[y][x+1]);
								current.friends.add(totalMaze[y-1][x]);
								maze.addNode(totalMaze[y-1][x]);
								current.friends.add(totalMaze[y][x-1]);
								maze.addNode(totalMaze[y][x-1]);
							} catch(ArrayIndexOutOfBoundsException r) {
								try {
									current.friends.add(totalMaze[y-1][x]);
									maze.addNode(totalMaze[y-1][x]);
									current.friends.add(totalMaze[y][x-1]);
									maze.addNode(totalMaze[y][x-1]);
								} catch(ArrayIndexOutOfBoundsException t) {
									try {
										current.friends.add(totalMaze[y][x-1]);
										maze.addNode(totalMaze[y][x-1]);
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
				
				if (backTracking(maze)) { // start the backtracking!
			        System.out.println("Found Solution");
			    }  
			    else { 
			        System.out.println("No solution"); 
			    } 

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
	 * Method for checking if a spot on the board is safe to set as a certain color or not
	 * Also handles all of the heuristics
	 * Params: the current node or 'space' on the graph that we are checking and the current color that we are wanting to check is safe to put there
	 * Returns: boolean, true if it is safe to place that color in that spot and false if not
	 */
	
	public static boolean isSafe(Node current, char color) { 
		if (current.content != '_') {
			return false;
		}
		
		int checker = 0;
		int numOfSameColor = 0;
		for(Node n : current.friends) {
			//boolean ch = true; // this wasn't doing anything ??
			if (n.content == '_') { // all this stuff i checking for corner issues
//				for (Node g : n.friends) {
//					if (g.content == '_' && g != current) {
//						ch = false;
//						break;
//					}
//					if (g.content == color) {
//						ch = false;
//						break;
//					}
//					if (!g.start) {
//						count++;
//					}
//				}
//				if (ch && count == 0) {
//					return false;
//				}
				continue;
			}
			
			//add check for the spot itself is safe
			
			
			if (n.content == color) { // is the friend the same color as the color we are checking for?
				checker++;
				numOfSameColor++;
			}
			for (Node e : n.friends) { // go through all of current nodes friends' friends
				if (e == current) {
					continue;
				}
				if (e.content == n.content) {
					checker++;
					numOfSameColor++;
				}
				if (e.content == '_') {
					checker++;
				}
			}
			if (n.start && (checker == 0 || numOfSameColor > 1)) {
				return false;
			}
			if (!n.start && (checker < 2 || numOfSameColor > 2)) {
				return false;
			}
			checker = 0;
			numOfSameColor = 0;
			
			
		}
		
		for (Node e : current.friends) {
			if (e.content == color) {
				checker++;
				numOfSameColor++;
			}
			if (e.content == '_') {
				checker++;
			}
		}
		if (current.start && (checker == 0 || numOfSameColor > 1)) {
			return false;
		}
		if (!current.start && (checker < 2 || numOfSameColor > 2)) {
			return false;
		}
		checker = 0;
		numOfSameColor = 0;
		
		//checking that there is no triple next to it.
		
//
//		for (Node n : current.friends) {
//			if (n.content == current.content) count++;	
//			if (count > 2) return false;
//			count = 0;
//		}

		
		// if there is no clash, it's safe 
		return true; 
	} 
	
	/*
	 * Main Backtracking method
	 * Params: a tree which is really a graph of nodes that all have value of a char
	 * Returns: boolean, true if the board is complete, false if it cannot find a solution
	 */

	public static boolean backTracking(Tree board) { 
		Node current = null;
	    boolean isComplete = true;
	    //need to also check starts and ends are connected
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
}
