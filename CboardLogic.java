// Logical functionalities of checkers for 3-D board simulation

import java.lang.Math;
import java.util.Scanner;

public class CboardLogic {

	// global board
	static int[][] board = new int[8][8];
	static boolean[][] legalMoves = new boolean[8][8];
	static boolean gameOver = false;

	// initialize board with starting values
	// 0 is empty, 1 is player 1, 2 is player 2, 3 is player 1 king, 4 is player 2 king
	static void initBoard(){
		gameOver = false;
		for(int i = 0; i < board.length;i++) {
			for(int j = 0; j < board[i].length; j++) {
				if (i % 2 == j % 2) {
					if (i < 3)
						board[i][j] = 2;
					else if (i > 4)
						board[i][j] = 1;
					else
						board[i][j] = 0;
				} else {
					board[i][j] = 0;
				}
			}
		}
	}
	
	// reset legalMoves
	static void resetMoves(){
		for(int i = 0; i < legalMoves.length;i++) {
			for(int j = 0; j < legalMoves[i].length; j++) {
				legalMoves[i][j] = false;
			}
		}
	}

	// display board to screen
	static void displayBoard(){
		System.out.println("Displaying Board\n");
		for(int i = 0; i < board.length;i++) {
			for(int j = 0; j < board[i].length; j++) {
				if(legalMoves[i][j] == true) {
					System.out.print("M ");
				} else
					System.out.print(board[i][j] + " ");

			}
			System.out.println("\n");
		}
	}
	
	// checks for game over after player turn finished
	static void checkGameOver(){
		int play1 = 0;
		int play2 = 0;
		
		for(int i = 0; i < board.length;i++) {
			for(int j = 0; j < board[i].length; j++) {
				if(board[i][j] == 1 || board[i][j] == 3) play1++;
				if(board[i][j] == 2 || board[i][j] == 4) play2++;
			}
		}
		
		if(play1 == 0) {
			System.out.println("PLAYER 2 WON\n");
			System.out.println("GAMEOVER\n");
			gameOver = true;
		}
		if(play2 == 0) {
			System.out.println("PLAYER 1 WON\n");
			System.out.println("GAMEOVER\n");
			gameOver = true;
		}
	}
	
	// ask player to play again after game over
	static void playAgain(){
		System.out.println("Press 1 to play again");
		Scanner scan = new Scanner(System.in);
		if(1 == scan.nextInt()) startGame();
		System.out.println("Thanks for playing");
	}
	
	// checks on board
	static boolean checkGrid(int row, int column) {
		if(row < 0 || row > 7 || column < 0 || column > 7) return false;
		return true;
	}

	// check validity of piece selection
	static boolean checkSelect(int player, int row, int column) {
		// selection must be on boar
		if(row < 0 || row > 7 || column < 0 || column > 7) {
			System.out.println("Invalid Position, please choose inside the board");
			return false;
		}

		// selection must be a player's piece
		if(player != board[row][column] && player + 2 != board[row][column]) {
			System.out.println("Invalid Position, please choose one of your pieces");
			return false;
		}

		return true;
	}

	// move piece function 
	static void movePiece(int row,int column,int toRow,int toColumn, int player) {
		board[row][column] = 0;

		if(Math.abs(toRow - row) == 1 && Math.abs(toColumn - column) == 1) {
			board[toRow][toColumn] = player;
		}
		else if(toRow - row == -2 && toColumn - column == -2) {
			board[toRow + 1][toColumn + 1] = 0;
			board[toRow][toColumn] = player;
		}
		else if(toRow - row == -2 && toColumn - column == 2) {
			board[toRow + 1][toColumn - 1] = 0;
			board[toRow][toColumn] = player;
		}
		else if(toRow - row == 2 && toColumn - column == 2) {
			board[toRow - 1][toColumn - 1] = 0;
			board[toRow][toColumn] = player;
		}
		else if(toRow - row == 2 && toColumn - column == -2) {
			board[toRow - 1][toColumn + 1] = 0;
			board[toRow][toColumn] = player;
		}
		else {
			return;	
		}
		
		// turn kings 
		if(player == 1 && toRow == 0) board[toRow][toColumn] = 3;
		if(player == 2 && toRow == 7) board[toRow][toColumn] = 4;
	}

	// player makes move
	static void playerMove(int player) {

		boolean isValid = false;
		int row = 0,column = 0;

		// checks piece select is valid
		while(!isValid) {
			System.out.println("Player " + player+ " Turn");
			System.out.print("Row: ");
			Scanner scan = new Scanner(System.in);
			row = scan.nextInt();
			System.out.print("Column: ");
			scan = new Scanner(System.in);
			column = scan.nextInt();
			isValid = checkSelect(player, row, column);
		}


		if(player == 1) {
			// can jump
			if(checkGrid(row-1,column-1) & checkGrid(row-2,column-2))
				if((board[row-1][column-1] == 2 || board[row-1][column-1] == 4) & board[row-2][column - 2] == 0) legalMoves[row-2][column-2] = true;	
			if(checkGrid(row-1,column+1) & checkGrid(row-2,column+2))
				if ((board[row-1][column + 1] == 2 || board[row-1][column + 1] == 4) & board[row-2][column + 2] == 0) legalMoves[row-2][column+2] = true;

			// can move diagonally
			if(checkGrid(row-1,column-1))
				if(board[row-1][column-1] == 0) legalMoves[row-1][column-1] = true;
			if(checkGrid(row-1,column+1))
				if(board[row-1][column+1] == 0) legalMoves[row-1][column+1] = true;
		}

		if(player == 2) {
			// can jump
			if(checkGrid(row+1,column+1) & checkGrid(row+2,column+2))
				if((board[row+1][column+1] == 1 || board[row+1][column+1] == 3) & board[row+2][column+2] == 0) legalMoves[row+2][column+2] = true;
			if(checkGrid(row+1,column-1) & checkGrid(row+2,column-2))
				if ((board[row+1][column-1] == 1 || board[row+1][column-1] == 3) & board[row+2][column-2] == 0) legalMoves[row+2][column-2] = true;

			// can move diagonally
			if(checkGrid(row+1,column+1))
				if(board[row+1][column+1] == 0) legalMoves[row+1][column+1] = true;
			if(checkGrid(row+1,column-1))
				if(board[row+1][column-1] == 0) legalMoves[row+1][column-1] = true;
		}

		// if player 1 is king
		if(board[row][column] == 3) {
			// can jump
			if(checkGrid(row+1,column+1) & checkGrid(row+2,column+2))
				if((board[row+1][column+1] == 2 || board[row+1][column+1] == 4) & board[row+2][column+2] == 0) legalMoves[row+2][column+2] = true;
			if(checkGrid(row+1,column-1) & checkGrid(row+2,column-2))
				if ((board[row+1][column-1] == 2 || board[row+1][column-1] == 2) & board[row+2][column-2] == 0) legalMoves[row+2][column-2] = true;
	
			// can move diagonally
			if(checkGrid(row+1,column+1))
				if(board[row+1][column+1] == 0) legalMoves[row+1][column+1] = true;
			if(checkGrid(row+1,column-1))
				if(board[row+1][column-1] == 0) legalMoves[row+1][column-1] = true;
		}
		
		// if player 2 is king
		if(board[row][column] == 4) {
			// can jump
			if(checkGrid(row-1,column-1) & checkGrid(row-2,column-2))
				if((board[row-1][column-1] == 1 || board[row-1][column-1] == 3) & board[row-2][column - 2] == 0) legalMoves[row-2][column-2] = true;
			if(checkGrid(row-1,column+1) & checkGrid(row-2,column+2))
				if ((board[row-1][column + 1] == 1 || board[row-1][column + 1] == 3) & board[row-2][column + 2] == 0) legalMoves[row-2][column+2] = true;
	
			// can move diagonally
			if(checkGrid(row-1,column-1))
				if(board[row-1][column-1] == 0) legalMoves[row-1][column-1] = true;
			if(checkGrid(row-1,column+1))
				if(board[row-1][column+1] == 0) legalMoves[row-1][column+1] = true;
		}
		
		
		displayBoard();
		int toRow = 0;
		int toColumn = 0;
		isValid = false;
		
		// checks selected move is legal
		while(!isValid) {
			System.out.print("Choose a move\n" + "Row: ");
			Scanner scan = new Scanner(System.in);
			toRow = scan.nextInt();
			System.out.print("Column: ");
			scan = new Scanner(System.in);
			toColumn = scan.nextInt();
			if(!checkGrid(toRow,toColumn)) continue;
			if(legalMoves[toRow][toColumn] == true) {
				isValid = true;
			} else {
				System.out.println("Invalid legal Move, try again!!");
			}
		}

		// moves piece
		movePiece(row,column,toRow,toColumn,board[row][column]);

		// clean up
		resetMoves();
		System.out.println("Player "+ player +" turn finished!!");
		displayBoard(); 
		checkGameOver();
	}

	// creates game
	static void startGame() {
		initBoard();
		displayBoard();
		while(!gameOver) {
			playerMove(1);
			if(gameOver == true) break;
			playerMove(2);
		}
		playAgain();
	}


	public static void main(String[] args) {
		startGame();
	}
}