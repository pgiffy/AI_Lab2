import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		/*Notes Section:
		 * Done by Peter Gifford and Sam Behrens
		 * 
		 * Define Flow Free as CSP : Done - Sam Check
		 * 		Variables : 	X sub(ij)  -- i is rox and j is column
		 * 		Domain : 		{ Set of letters present in the given graph } - GOOD
		 * 		Constraints : 	All spots must be only one color. (check this for wording before putting in report)
		 * 				Maybe:	Each color must be touching another of its own color at least once but no more than two times
		 * 						(i,j) -> (i+1,j),(i-1,j),(i,j+1),(i,j-1) -> At least one of which must be the same as (i,j) but no more than two can be the same)
		 * 						I'm not sure if I like how that is written but we have plenty of time to change it
		 * 
		 * FUN FACT: This is not a recursive back tracking problem. I'm changing it to something else
		 * 
		 * 
		 * Backtracking search : Just need to define quitting condition that all spaces are filled and *!*!*!*!**!All start and ends are connected/paths are full
		 * 
		 * Heuristics Chosen : None
		 * 
		 * Report : Not Done
		 * 
		 * */

		Scanner scan;
		String fileOut = "output.txt";
		PrintWriter out;
		String fileName = "5x5maze.txt";

		try {
			out = new PrintWriter(new File(fileOut));
			//outer loop for controlling type of search algo

				//inner loop for controlling files for algorithms

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
					for (Node n : maze.nodes) {
						Set<Node> temp = new HashSet<>();
						temp.addAll(n.friends);
						n.friends.clear();
						n.friends.addAll(temp);
					}
					
					String temp = "";
					ArrayList<Character> tempL = new ArrayList<>();
					for (Node n : maze.nodes) {
						if(!tempL.contains(n.content) && n.content != '_') {
							tempL.add(n.content);
						}
					}
					for (Character c : tempL) {
						temp += c;
					}
					char[] letters = temp.toCharArray();
					
					for (Node n : maze.nodes) {
						n.setCharacters(letters);
					}
					
					//tack on the letters to every node rather than the loops
					
					if (backTracking(maze)) { 
				        System.out.println("Found Solution");
				    }  
				    else { 
				        System.out.println("No solution"); 
				    } 

					for (int i = 0; i < yLength; i++) {
						for(int j = 0; j < xLength; j++) {
							out.print(totalMaze[i][j].content);
						}
						out.println();
					}

			out.close();//close file
		} catch(FileNotFoundException e) {
		}
		
		
}

	public static boolean isSafe(Node current, char color) { 
		if (current.content != '_') {
			return false;
		}
		
		int checker = 0;
		int count = 0;
		for(Node n : current.friends) {
			boolean ch = true;
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
			
			
			if (n.content == color) {
				checker++;
				count++;
			}
			for (Node e : n.friends) {
				if (e == current) {
					continue;
				}
				if (e.content == n.content) {
					checker++;
					count++;
				}
				if (e.content == '_') {
					checker++;
				}
			}
			if (n.start && (checker == 0 || count > 1)) {
				return false;
			}
			if (!n.start && (checker < 2 || count > 2)) {
				return false;
			}
			checker = 0;
			count = 0;
			
			
		}
		
		for (Node e : current.friends) {
			if (e.content == color) {
				checker++;
				count++;
			}
			if (e.content == '_') {
				checker++;
			}
		}
		if (current.start && (checker == 0 || count > 1)) {
			return false;
		}
		if (!current.start && (checker < 2 || count > 2)) {
			return false;
		}
		checker = 0;
		count = 0;
		
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

	public static boolean backTracking(Tree board) { 
		Node current = null;
	    boolean isEmpty = true; 
	    //need to also check starts and ends are connected
	    for (Node n : board.nodes) { 
	            if (n.content == '_')  { 
	            	current = n;
	                // we still have some remaining missing values
	                isEmpty = false;  
	                break; 
	            }
	        if (!isEmpty) break; 
	    }

	    
	    // no empty space left 
	    if (isEmpty) return true; 

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
