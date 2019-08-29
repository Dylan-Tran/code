
public class board {
	String[][] state;
	String PlayerName;
	
	public board() {
		this.state = new String[3][3]; 
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				this.state[i][j] = "EMPTY";
			}
		}
	}
	
	public board(String name) {
		this.state = new String[3][3];
		this.PlayerName = name;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				this.state[i][j] = "EMPTY";
			}
		}
	}
	
	public void printState() {
		System.out.println();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				System.out.print(this.state[i][j] + " ");
			}
			System.out.println();
			System.out.println();
		}
	}
	
	public void update(int row, int col, String player) {
		this.state[row][col] = player;
	}
	
	public int checkRows() {
		int checkBot = 0;
		int checkPlayer = 0;
		
		//Check the rows
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				
				if(this.state[i][j].equals(this.PlayerName)) {
					checkPlayer++;
				}
				
				if(this.state[i][j].equals("BOT")) {
					checkBot++;
				}
			}
			if(checkPlayer == 3) {
				return 0; 
			}
			
			if(checkBot == 3) {
				return 1; 
			}
			
			checkBot = 0;
			checkPlayer = 0;
			
		}
		
		return -1;
	}
	
	public int checkColumns() {
		int checkBot = 0;
		int checkPlayer = 0;
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				
				if(this.state[j][i].equals(PlayerName)) {
					checkPlayer++;
				}
				
				if(this.state[j][i].equals("BOT")) {
					checkBot++;
				}
				
			}
			if(checkPlayer == 3) {
				return 0; 
			}
			
			if(checkBot == 3) {
				return 1; 
			}
			
			checkBot = 0;
			checkPlayer = 0;
			
		}
		return -1;
	}
	
	public int checkDiagonals() {
		int checkBot = 0;
		int checkPlayer = 0;
	
		//top left to bottom right
		for(int i = 0; i < 3; i++) {
			if(this.state[i][i].equals(PlayerName)) {
				checkPlayer++;
			}
			
			if(this.state[i][i].equals("BOT")) {
				checkBot++;
			}
				
		}
		
		if(checkPlayer == 3) {
			return 0; 
		}
		
		if(checkBot == 3) {
			return 1; 
		}
			
		checkBot = 0;
		checkPlayer = 0;
		
		//top right to bottom left
		for(int i = 2; i >= 0; i--) {
			if(this.state[i][Math.abs(i-2)].equals(PlayerName)) {
				checkPlayer++;
			}
			
			if(this.state[i][i].equals("BOT")) {
				checkBot++;
			}
				
		}
		
		if(checkPlayer == 3) {
			return 0; 
		}
		
		if(checkBot == 3) {
			return 1; 
		}		
		return -1;
	}
	
	
	public String winner() {
		if(this.checkRows() != -1) {
			if(this.checkRows() == 0) {
				return this.PlayerName;
			}
			if(this.checkRows() == 1) {
				return "Bot";
			}
		}
		
		if(this.checkColumns() != -1) {
			if(this.checkColumns() == 0) {
				return this.PlayerName;
			}
			if(this.checkColumns() == 1) {
				return "Bot";
			}
		}
		
		if(this.checkDiagonals() != -1) {
			if(this.checkDiagonals() == 0) {
				return this.PlayerName;
			}
			if(this.checkDiagonals() == 1) {
				return "Bot";
			}
		}
		return "none";
	}
	

	public void move(int inputRow, int inputColumn) {
		if(inputRow >= 3 || inputRow < 0) {
			System.out.println("Invaild Move");
			return;
		}
		if(inputColumn >= 3 || inputColumn < 0) {
			System.out.println("Invaild Move");
			return;
		}
		
		if(!(this.state[inputRow][inputColumn].equals("EMPTY"))) {
			System.out.println("Invaild Move");
			return;
		}
		
		update(inputRow, inputColumn, this.PlayerName);
		
		if(!this.winner().equals("none")) {
			System.out.println(this.winner() + " has won the game");
			return;
		}
		
		//AI moves
		boolean[][] spots = new boolean[3][3];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(this.state[i][j].equals("EMPTY")) {
					spots[i][j] = true;
				} else {
					spots[i][j] = false;
				}
			}
		}		
		boolean track = true;
		int[] AI = new int[2];
		while(track) {
			AI[0] = (int) Math.round(Math.random()*2);
			AI[1] = (int) Math.round(Math.random()*2);
			if(spots[AI[0]][AI[1]]) {
				track = false;
			}
		}
	
		update(AI[0], AI[1], "BOT");
		if(!this.winner().equals("none")) {
			System.out.println(this.winner() + " has won the game");
			return;
		}
		this.printState();
	}
}
