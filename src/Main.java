import java.util.*;
import java.io.*;

public class Main {
	
	public boolean backtrack(char[][] grid, int sol){
		//if there are 0 spots left then return
		
		
		
		//there is no solution
		return false;
	}
	

	public static void main(String[] args) {
		/*Notes Section:
		 * Done by Peter Gifford and Sam Behrens
		 * 
		 * Define Flow Free as CSP : Done - Sam Check
		 * 		Variables : X sub(ij)  -- i is rox and j is column
		 * 		Domain : { Set of letters present in the given graph }
		 * 		Constraints : All spots must be only one color. (check this for wording before putting in report)
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
