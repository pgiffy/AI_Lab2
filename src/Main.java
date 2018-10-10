import java.util.*;
import java.io.*;

public class Main {
	
	public static boolean isSafe(char[][] board, int row, int col, char color) { 
		if(board[row][col] == '_') {
			return false;
		}
		char[] next = new char[4];
		next[0] = board[row+1][col];
		next[1] = board[row-1][col];
		next[2] = board[row][col-1];
		next[3] = board[row][col+1];
		Arrays.sort(next);
		boolean check = true;
		for(char c : next) {
			if(c == board[row][col]) check = false;
		}
		if(check) return false;
		
		boolean check2 = true;
		for(int i  = 0; i < next.length-1; i++) if(next[i] == next[i+1]) check2 = true;

		if(check2) return false;

		// if there is no clash, it's safe 
		return true; 
	} 

	public static boolean backTracking(char[][] board, char[] letters) { 
	    int row = -1; 
	    int col = -1; 
	    boolean isEmpty = true; 
	    for (int i = 0; i < letters.length; i++) 
	    { 
	        for (int j = 0; j < letters.length; j++)  
	        { 
	            if (board[i][j] == '_')  
	            { 
	                row = i; 
	                col = j; 
	                  
	                // we still have some remaining 
	                // missing values in Sudoku 
	                isEmpty = false;  
	                break; 
	            } 
	        } 
	        if (!isEmpty) 
	        { 
	            break; 
	        } 
	    } 
	  
	    // no empty space left 
	    if (isEmpty)  
	    { 
	        return true; 
	    } 
	  
	   //enter letters
	    
	    for(char c : letters) {
	    	if(isSafe(board,row,col,c)) {
	    		board[row][col] = c;
	    		if(backTracking(board, letters)) {
	    			return true;
	    		}else {
	    			board[row][col] = '_';
	    		}
	    	}
	    }
	    
	    
	    return false; 
	} 

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
					char[][] totalMaze = new char[yLength][xLength];
					while(scan.hasNext()) {
						String currentLine = scan.nextLine();
						perLineIter = 0;
						for(char con : currentLine.toCharArray()) {
							totalMaze[lineIter][perLineIter] = con;
							idIter++;
							perLineIter++;
						}
						lineIter++;
						//totalMaze has nodes of all 
					}
					int iter = 0;
					char[] leet = new char[10];
					for(int i = 0; i < yLength; i++) {
						for(int j = 0; j < xLength; j++) {
							if(!Arrays.asList(leet).contains(totalMaze[i][j]) && totalMaze[i][j] != '_') {
								leet[iter] = totalMaze[i][j];
								iter++;
							}
						}	
					}
					int counter = 0;
					for(char c : leet) {
						if(c != 0) counter++;
					}
					char[] leeeet = new char[counter];
					for(int i = 0; i < counter; i++) {
						leeeet[i] = leet[i];
					}
					backTracking(totalMaze, leeeet);
					

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

}
