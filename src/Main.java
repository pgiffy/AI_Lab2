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
		 * Backtracking search : Not Done
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
					while(scan.hasNextLine()) {
						xLength = scan.nextLine().length();
						yLength++;
					}
					int size = yLength*xLength;
					int idIter = 0;
					int perLineIter = 0;
					int lineIter = 0;

					scan = new Scanner(gridFile);//reseting scanner to the top
					Node[][] totalMaze = new Node[yLength][xLength];
					while(scan.hasNext()) {
						String currentLine = scan.nextLine();
						perLineIter = 0;
						for(char con : currentLine.toCharArray()) {
							totalMaze[lineIter][perLineIter] = new Node(idIter, con, perLineIter, lineIter);
							idIter++;
							perLineIter++;
						}
						lineIter++;
						//totalMaze has nodes of all 
					}
					
					//generation of the "tree" should be called network or graph
					Tree maze = new Tree();
					for(int y = 0; y < yLength; y++) {
						for(int x = 0; x < xLength; x++) {
							Node current = totalMaze[y][x];
							try {
								//checking all cardinal directions since cant move diagonal
								if(totalMaze[y+1][x] != null) {
									current.friends.add(totalMaze[y+1][x]);
									maze.addNode(totalMaze[y+1][x]);
								}
								if(totalMaze[y][x+1] != null) {
									current.friends.add(totalMaze[y][x+1]);
									maze.addNode(totalMaze[y][x+1]);
								}
								if(totalMaze[y-1][x] != null) {
									current.friends.add(totalMaze[y-1][x]);
									maze.addNode(totalMaze[y-1][x]);
								}
								if(totalMaze[y][x-1] != null) {
									current.friends.add(totalMaze[y][x-1]);
									maze.addNode(totalMaze[y][x-1]);
								}
							}catch(ArrayIndexOutOfBoundsException e) {
							}
						}
					}
					String temp = "";
					ArrayList<Character> tempL = new ArrayList<>();
					for(Node n : maze.nodes) {
						if(!tempL.contains(n.content)) {
							tempL.add(n.content);
						}
					}
					for(Character c : tempL) {
						temp += c;
					}
					char[] letters = temp.toCharArray();
					
					if (backTracking(maze, letters)) 
				    { 
				        System.out.println("cool");
				    }  
				    else
				    { 
				        System.out.println("No solution"); 
				    } 

					for(int i = 0; i < yLength; i++) {
						for(int j = 0; j < xLength; j++) {
							out.print(totalMaze[i][j]);
						}
						out.println();
					}

				
			
			out.close();//close file
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}
		
		
}
	public static boolean isSafe(Node current, char color) { 
		if(current.content != '_') {
			return false;
		}
		boolean check = true;
		for(Node n : current.friends) {
			if(n.content == color) { 
				check = false;
			}
		}
		if(check) return false;
		
//		boolean check2 = true;
//		for(int i  = 0; i < next.length-1; i++) if(next[i] == next[i+1]) check2 = true;
//
//		if(check2) return false;

		// if there is no clash, it's safe 
		return true; 
	} 

	public static boolean backTracking(Tree board, char[] letters) { 
		Node current = null;
	    boolean isEmpty = true; 
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
	    
	    for(char c : letters) {
	    	if(isSafe(current, c)) {
	    		current.content = c;
	    		if(backTracking(board, letters)) {
	    			return true;
	    		}else {
	    			current.content = '_';
	    		}
	    	}
	    }
	    
	    
	    return false; 
	} 
}
