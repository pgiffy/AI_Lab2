import java.util.*;
import java.io.*;

public class Main {

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
					Gird grid = new Gird();
					for(int y = 0; y < yLength; y++) {
						for(int x = 0; x < xLength; x++) {
							Node current = totalMaze[y][x];
							try {
								//checking all cardinal directions since cant move diagonal
								if((totalMaze[y+1][x].content == '_' || (totalMaze[y+1][x].content >= 'A' && totalMaze[y+1][x].content <= 'Z')) && totalMaze[y+1][x] != null) {
									current.friends.add(totalMaze[y+1][x]);
									grid.addNode(totalMaze[y+1][x]);
								}
								if((totalMaze[y][x+1].content == '_' || (totalMaze[y][x+1].content >= 'A' && totalMaze[y][x+1].content <= 'Z')) && totalMaze[y][x+1] != null) {
									current.friends.add(totalMaze[y][x-1]);
									grid.addNode(totalMaze[y][x+1]);
								}
								if((totalMaze[y-1][x].content == '_' || (totalMaze[y-1][x].content >= 'A' && totalMaze[y-1][x].content <= 'Z')) && totalMaze[y-1][x] != null) {
									current.friends.add(totalMaze[y-1][x]);
									grid.addNode(totalMaze[y-1][x]);
								}
								if((totalMaze[y][x-1].content == '_' || (totalMaze[y][x-1].content >= 'A' && totalMaze[y][x-1].content <= 'Z')) && totalMaze[y][x-1] != null) {
									current.friends.add(totalMaze[y][x-1]);
									grid.addNode(totalMaze[y][x-1]);
								}
							}catch(ArrayIndexOutOfBoundsException e) {

							}
						}

					}
					
					
					//algorithm here
					
					
					for(int i = 0; i < yLength; i++) {
						for(int j = 0; j < xLength; j++) {
							out.print(totalMaze[i][j].content);
						}
						out.println();
					}

				
			
			out.close();//close file
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}
		
		
}

}
