import java.util.Scanner;

public class play {
	public static void main(String[] args) {
		System.out.println("Enter player name:");
		Scanner in = new Scanner(System.in);
		
		String name = in.next();
		
		board gameboard = new board(name);
		
		
		gameboard.printState();
		
		while(gameboard.winner().equals("none")) {
			System.out.println("Enter the row");
			int row = in.nextInt();
			
			System.out.println("Enter the column");
			int column = in.nextInt();
			System.out.println("---------------------------------------------");	
			gameboard.move(row, column);
			System.out.println("---------------------------------------------");
		}
		
		gameboard.printState();
	}
}
