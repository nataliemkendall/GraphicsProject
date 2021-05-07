package examples;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Board extends Frame {
	public static void main(String[] args) {new Board();}

	Board(){
		super("Checkers");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		setSize(700, 700);
		add("Center", new CvBoard());
		setVisible(true);
	}
}

class CvBoard extends Canvas {
	float x0, y0, rWidth = 10.0F, rHeight = 10.0F, pixelSize;
	int centerX, centerY, maxX, maxY, xP = 1000000, yP= 1000000;
	int left, right, top, bottom;
	int dY, dX, numBoxes, boxHeight, boxWidth, circleHeight, circleWidth;
	int leftStart, topStart, leftCircleStart, topCircleStart;
	int selected, oldX = -1, oldY = -1;
	static int[][] pieces = new int[8][8];
	static boolean[][] legalMoves = new boolean[8][8];
	static boolean gameOver = false;
	static boolean selectedQ = false;
	int playerTurn = 1;
	int mouseClicks;
	Font unselectedFont = new Font("Helvetica", Font.PLAIN, 20);
	Font selectedFont = new Font("Helvetica", Font.BOLD, 22);
	boolean gameStart;

	CvBoard(){
		gameStart = false;
		initBoard();
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent click) {
				xP = click.getX();
				yP = click.getY();
				repaint();
			}
		});

	}

	// initialize board with starting values
	// 0 is empty, 1 is player 1, 2 is player 2, 3 is player 1 king, 4 is player 2 king
	static void initBoard(){
		gameOver = false;
		for(int i = 0; i < pieces.length;i++) {
			for(int j = 0; j < pieces[i].length; j++) {
				if (i % 2 != j % 2) {
					if (i < 3)
						pieces[i][j] = 2;
					else if (i > 4)
						pieces[i][j] = 1;
					else
						pieces[i][j] = 0;
				} else {
					pieces[i][j] = -1;
				}
			}
		}
	}

	// checks on board
	static boolean checkGrid(int row, int column) {
		if(row < 0 || row > 7 || column < 0 || column > 7) return false;
		return true;
	}
	
	// reset legalMoves
	static void resetMoves(){
		for(int i = 0; i < legalMoves.length;i++) {
			for(int j = 0; j < legalMoves[i].length; j++) {
				legalMoves[i][j] = false;
			}
		}
	}
	
	// add legal moves for selected piece
	static void addLegalMoves(int player, int row, int column){
		if(player == 1 || player == 3) {
			// can jump
			if(checkGrid(row-1,column-1) & checkGrid(row-2,column-2))
				if((pieces[row-1][column-1] == 2 || pieces[row-1][column-1] == 4) & pieces[row-2][column - 2] == 0) legalMoves[row-2][column-2] = true;	
			if(checkGrid(row-1,column+1) & checkGrid(row-2,column+2))
				if ((pieces[row-1][column + 1] == 2 || pieces[row-1][column + 1] == 4) & pieces[row-2][column + 2] == 0) legalMoves[row-2][column+2] = true;

			// can move diagonally
			if(checkGrid(row-1,column-1))
				if(pieces[row-1][column-1] == 0) legalMoves[row-1][column-1] = true;
			if(checkGrid(row-1,column+1))
				if(pieces[row-1][column+1] == 0) legalMoves[row-1][column+1] = true;
		}

		if(player == 2 || player == 4) {
			// can jump
			if(checkGrid(row+1,column+1) & checkGrid(row+2,column+2))
				if((pieces[row+1][column+1] == 1 || pieces[row+1][column+1] == 3) & pieces[row+2][column+2] == 0) legalMoves[row+2][column+2] = true;
			if(checkGrid(row+1,column-1) & checkGrid(row+2,column-2))
				if ((pieces[row+1][column-1] == 1 || pieces[row+1][column-1] == 3) & pieces[row+2][column-2] == 0) legalMoves[row+2][column-2] = true;

			// can move diagonally
			if(checkGrid(row+1,column+1))
				if(pieces[row+1][column+1] == 0) legalMoves[row+1][column+1] = true;
			if(checkGrid(row+1,column-1))
				if(pieces[row+1][column-1] == 0) legalMoves[row+1][column-1] = true;
		}
		if(pieces[row][column] == 3) {
			// can jump
			if(checkGrid(row+1,column+1) & checkGrid(row+2,column+2))
				if((pieces[row+1][column+1] == 2 || pieces[row+1][column+1] == 4) & pieces[row+2][column+2] == 0) legalMoves[row+2][column+2] = true;
			if(checkGrid(row+1,column-1) & checkGrid(row+2,column-2))
				if ((pieces[row+1][column-1] == 2 || pieces[row+1][column-1] == 2) & pieces[row+2][column-2] == 0) legalMoves[row+2][column-2] = true;
	
			// can move diagonally
			if(checkGrid(row+1,column+1))
				if(pieces[row+1][column+1] == 0) legalMoves[row+1][column+1] = true;
			if(checkGrid(row+1,column-1))
				if(pieces[row+1][column-1] == 0) legalMoves[row+1][column-1] = true;
		}
		
		// if player 2 is king
		if(pieces[row][column] == 4) {
			// can jump
			if(checkGrid(row-1,column-1) & checkGrid(row-2,column-2))
				if((pieces[row-1][column-1] == 1 || pieces[row-1][column-1] == 3) & pieces[row-2][column - 2] == 0) legalMoves[row-2][column-2] = true;
			if(checkGrid(row-1,column+1) & checkGrid(row-2,column+2))
				if ((pieces[row-1][column + 1] == 1 || pieces[row-1][column + 1] == 3) & pieces[row-2][column + 2] == 0) legalMoves[row-2][column+2] = true;
	
			// can move diagonally
			if(checkGrid(row-1,column-1))
				if(pieces[row-1][column-1] == 0) legalMoves[row-1][column-1] = true;
			if(checkGrid(row-1,column+1))
				if(pieces[row-1][column+1] == 0) legalMoves[row-1][column+1] = true;
		}
	}

	// move piece function 
	static void movePiece(int row,int column,int toRow,int toColumn, int player) {
		pieces[row][column] = 0;

		if(Math.abs(toRow - row) == 1 && Math.abs(toColumn - column) == 1) {
			pieces[toRow][toColumn] = player;
		}
		else if(toRow - row == -2 && toColumn - column == -2) {
			pieces[toRow + 1][toColumn + 1] = 0;
			pieces[toRow][toColumn] = player;
		}
		else if(toRow - row == -2 && toColumn - column == 2) {
			pieces[toRow + 1][toColumn - 1] = 0;
			pieces[toRow][toColumn] = player;
		}
		else if(toRow - row == 2 && toColumn - column == 2) {
			pieces[toRow - 1][toColumn - 1] = 0;
			pieces[toRow][toColumn] = player;
		}
		else if(toRow - row == 2 && toColumn - column == -2) {
			pieces[toRow - 1][toColumn + 1] = 0;
			pieces[toRow][toColumn] = player;
		}
		else {
			return;	
		}
		// turn kings 
		if(player == 1 && toRow == 0) pieces[toRow][toColumn] = 3;
		if(player == 2 && toRow == 7) pieces[toRow][toColumn] = 4;
	}

	
	// checks for game over after player turn finished
	static void checkGameOver(){
		int play1 = 0;
		int play2 = 0;
		
		for(int i = 0; i < pieces.length;i++) {
			for(int j = 0; j < pieces[i].length; j++) {
				if(pieces[i][j] == 1 || pieces[i][j] == 3) play1++;
				if(pieces[i][j] == 2 || pieces[i][j] == 4) play2++;
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
	
	boolean blackTile(int i, int j){
		if (j % 2 == 0 && i % 2 != 0 || j % 2 != 0 && i % 2 == 0)
			return true;
		else
			return false;
	}

	void drawGrid(Graphics g){
		left = iX(-rWidth/2);
		right = iX(rWidth/2);
		top = iY(rHeight/2);
		bottom = iY(-rHeight/2);

		dY = bottom-top; dX = right-left;
		boxWidth = dX/10; boxHeight = dY/10;
		numBoxes = 8;	//Board is 8x8
		circleWidth = (boxWidth*2)/3; circleHeight = (boxHeight*2)/3;

		leftStart = left + boxWidth; topStart = top + boxHeight;	//Leave some whitespace around the board
		leftCircleStart = leftStart + boxWidth/6; topCircleStart = topStart + boxHeight/6;

		//Following polygons create the 3D illusion
		int[] topBottomX = {left+boxWidth, left+boxWidth, left+boxWidth*9, left+boxWidth*9};
		int[] topYBoard = {top+((boxHeight*32)/35), top+boxHeight, top+boxHeight, top+((boxHeight*32)/35)};
		int[] leftXBoard = {left+((boxWidth*32)/35), left+boxWidth, left+boxWidth, left+((boxWidth*32)/35)};
		int[] leftRightY = {top+((boxHeight*32)/35), top+((boxHeight*32)/35), bottom-((boxHeight*32)/35), bottom-((boxHeight*32)/35)};
		int[] rightXBoard = {right-((boxWidth*32)/35), left+boxWidth*9, left+boxWidth*9, right-((boxWidth*32)/35)};
		int[] bottomYBoard = {bottom-((boxHeight*32)/35), top+boxHeight*9, top+boxHeight*9, bottom-((boxHeight*32)/35)};
		int[] rightShadowX = {right-((boxWidth*32)/35), right-((boxWidth*27)/35), right-((boxWidth*27)/35), right-((boxWidth*32)/35)};
		int[] rightShadowY = {top+((boxHeight*32)/35), top+((boxHeight*43)/35), bottom-((boxHeight*27)/35), bottom-((boxHeight*32)/35)};
		int[] bottomShadowX = {left+((boxWidth*32)/35), left+((boxWidth*43)/35), right-((boxWidth*27)/35), right-((boxWidth*32)/35)};
		int[] bottomShadowY = {bottom-((boxHeight*32)/35), bottom-((boxHeight*27)/35), bottom-((boxHeight*27)/35), bottom-((boxHeight*32)/35)};
				
		Polygon topBoard = new Polygon(topBottomX, topYBoard, 4);
		Polygon leftBoard = new Polygon(leftXBoard, leftRightY, 4);
		Polygon rightBoard = new Polygon(rightXBoard, leftRightY, 4);
		Polygon bottomBoard = new Polygon(topBottomX, bottomYBoard, 4);
		Polygon rightShadow = new Polygon(rightShadowX, rightShadowY, 4);
		Polygon bottomShadow = new Polygon(bottomShadowX, bottomShadowY, 4);
		g.setColor(new Color(102, 0, 0));
		g.drawPolygon(topBoard); g.drawPolygon(leftBoard); g.drawPolygon(rightBoard); g.drawPolygon(bottomBoard);
		g.fillPolygon(topBoard); g.fillPolygon(leftBoard); g.fillPolygon(rightBoard); g.fillPolygon(bottomBoard);
		g.setColor(Color.BLACK);
		g.drawPolygon(rightShadow); g.drawPolygon(bottomShadow); 
		g.setColor(new Color(51, 0, 0));
		g.fillPolygon(rightShadow); g.fillPolygon(bottomShadow);
				
		g.setColor(Color.BLACK);		
		for (int i = 0; i < numBoxes; i++) {
			for (int j = 0; j < numBoxes; j++) {
				g.drawRect(leftStart+(boxWidth*j), topStart+(boxHeight*i), boxWidth, boxHeight);
				if (blackTile(i, j)){
					g.setColor(Color.BLACK);
					g.fillRect(leftStart+(boxWidth*j), topStart+1+(boxHeight*i), boxWidth, boxHeight-1);
				}
			}
		}
		if (gameStart == false) {
			drawStartPieces(g);
		}
	}	

	void drawPieces(Graphics g){
		Font kingFont = new Font("Dialog", Font.BOLD, 14);
		for(int i = 0; i < 8; i++){				
			for(int j = 0; j < 8; j++){
				if(pieces[i][j] == 1){	
					g.setColor(Color.BLACK);
					g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
					g.setColor(Color.RED);
					g.fillOval(leftCircleStart+1+(boxWidth*j), topCircleStart+1+(boxHeight*i), circleWidth-1, circleHeight-1);
				}
				else if(pieces[i][j] == 2){
					g.setColor(Color.BLACK);
					g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
					g.setColor(new Color(0, 128, 255));
					g.fillOval(leftCircleStart+1+(boxWidth*j), topCircleStart+1+(boxHeight*i), circleWidth-1, circleHeight-1);
				}
				else if(pieces[i][j] == 3){	
					g.setColor(Color.BLACK);
					g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth+1, circleHeight+1);
					g.setColor(new Color(255, 51, 111));
					g.fillOval(leftCircleStart+1+(boxWidth*j), topCircleStart+1+(boxHeight*i), circleWidth-1, circleHeight-1);
					g.setColor(Color.BLACK);
					g.setFont(kingFont);
					g.drawString("KING", leftCircleStart+(boxWidth*j)+circleWidth/6, topCircleStart+(13*circleHeight)/20+(boxHeight*i));
				}
				else if(pieces[i][j] == 4){	
					g.setColor(Color.BLACK);
					g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
					g.setColor(new Color(111, 51, 255));
					g.fillOval(leftCircleStart+1+(boxWidth*j), topCircleStart+1+(boxHeight*i), circleWidth-1, circleHeight-1);
					g.setColor(Color.BLACK);
					g.setFont(kingFont);
					g.drawString("KING", leftCircleStart+1+(boxWidth*j)+circleWidth/6, topCircleStart+(13*circleHeight)/20+(boxHeight*i));
				}
				else if(legalMoves[i][j] == true){	
					g.setColor(Color.BLACK);
					g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
					int alpha = 127;
					g.setColor(new Color(255, 255, 0, alpha));
					g.fillOval(leftCircleStart+1+(boxWidth*j), topCircleStart+1+(boxHeight*i), circleWidth-1, circleHeight-1);
				}
				else if(pieces[i][j] == 0){
					g.setColor(Color.BLACK);
					g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
					g.fillOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
				}
				else{
					g.setColor(Color.WHITE);
					g.fillRect(leftStart+(boxWidth*j), topStart+1+(boxHeight*i), boxWidth, boxHeight-1);
				}
			}
		}
	}

	void drawStartPieces(Graphics g) {	
		g.setFont(unselectedFont);
		for (int i = 0; i < 8; i++) {
			g.drawString(Character.toString((char)i+65), (left+(boxWidth/2)), (top+(boxHeight*i)+((3*boxHeight)/2)));
		}
		for (int j = 0; j < 8; j++) {
			g.drawString(new String(""+j), (left+(boxWidth*j)+((3*boxWidth)/2)), (top+(4*boxHeight)/5));
		}
		drawPieces(g);
	}

	void selectPiece(Graphics g, int i, int j) {
		g.setColor(Color.BLACK);
		g.setFont(selectedFont);
		g.drawString(Character.toString((char)i+65), (left+(boxWidth/2)), (top+(boxHeight*i)+((3*boxHeight)/2)));
		g.drawString(new String(""+j), (left+(boxWidth*j)+((3*boxWidth)/2)), (top+(4*boxHeight)/5));

		g.setFont(unselectedFont);
		for (int w = 0; w < 8; w++) {
			if (w == i) {
				g.setFont(selectedFont);
				g.drawString(Character.toString((char)i+65), (left+(boxWidth/2)), (top+(boxHeight*i)+((3*boxHeight)/2)));
				g.setFont(unselectedFont);
			}
		}
		for (int y = 0; y < 8; y++) {
			if (y == j) {
				g.setFont(selectedFont);
				g.drawString(new String(""+j), (left+(boxWidth*j)+((3*boxWidth)/2)), (top+(4*boxHeight)/5));
				g.setFont(unselectedFont);
			}
		}
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				if(pieces[i][j] == 1){
					g.setColor(Color.RED.darker());
					g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
					g.fillOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);					
				}
				else if(pieces[i][j] == 2){
					g.setColor(Color.BLUE.darker());
					g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
					g.fillOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
				}
				else if(pieces[i][j] == 3){
					g.setColor(Color.RED.darker());
					g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
					g.fillOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
				}
				else if(pieces[i][j] == 4){
					g.setColor(Color.BLUE.darker());
					g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
					g.fillOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
				}
				else{
					selected = 0;
				}
			}
		}
	}

	void initgr() {
		Dimension d = getSize();
		maxX = d.width - 1; maxY = d.height - 1;
		pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
		centerX = maxX / 2; centerY = maxY / 2;
	}

	int iX(float x) {return Math.round(centerX + x / pixelSize);}
	int iY(float y) {return Math.round(centerY - y / pixelSize);}
	float fx(int x) {return (x - centerX) * pixelSize;}
	float fy(int y) {return (centerY - y) * pixelSize;}

	public void paint(Graphics g) {
		initgr();
		drawGrid(g);

		// Player 1 Turn
		if (xP != 1000000 && yP != 1000000 && playerTurn == 1) {
			System.out.println("Player 1 Turn");
			int js = (xP - leftStart)/boxWidth;
			int is = (yP - topStart)/boxHeight;
			char js1 = 'A';
			for(int i = 0; i < 8; i++){
				if(js == i && i == 1)
					js1 = 'A';
				else if(js == i && i ==2)
					js1 = 'B';
				else if(js == i && i ==3)
					js1 = 'C';
				else if(js == i && i ==4)
					js1 = 'D';
				else if(js == i && i ==5)
					js1 = 'E';
				else if(js == i && i ==6)
					js1 = 'F';
				else if(js == i && i ==7)
					js1 = 'G';
				else if(js == i && i ==8)
					js1 = 'H';
			}
			System.out.println("Coordinates " + js1 + " " + is);
			selectPiece(g, is, js);
			if(selectedQ && checkGrid(is,js) && legalMoves[is][js] == true){
				System.out.println("Activated");
				if(blackTile(is, js)){
					movePiece(oldX,oldY,is,js,pieces[oldX][oldY]);
					resetMoves();
					drawPieces(g);
					selectedQ = false;
					checkGameOver();
					playerTurn = 2;
				}
			} 
			else if(checkGrid(is,js) && (pieces[is][js] == 1 || pieces[is][js] == 3)) {
				System.out.println("Piece Selected");
				selectPiece(g, is, js);
				resetMoves();
				addLegalMoves(pieces[is][js],is,js);
				drawPieces(g);
				oldX = is;
				oldY = js;
				selectedQ = true;
			}
			else if (selected == 0 && is >=0 && is <= 7 && js >=0 && js <= 7) {
				selectPiece(g, is, js);
			}
		}


		// Player 2 Turn
		if (xP != 1000000 && yP != 1000000 && playerTurn == 2) {
			System.out.println("Player 2 Turn");
			int js = (xP - leftStart)/boxWidth;
			int is = (yP - topStart)/boxHeight;
			char js1 = 'A';
			for(int i = 0; i < 8; i++){
				if(js == i && i == 1)
					js1 = 'A';
				else if(js == i && i ==2)
					js1 = 'B';
				else if(js == i && i ==3)
					js1 = 'C';
				else if(js == i && i ==4)
					js1 = 'D';
				else if(js == i && i ==5)
					js1 = 'E';
				else if(js == i && i ==6)
					js1 = 'F';
				else if(js == i && i ==7)
					js1 = 'G';
				else if(js == i && i ==8)
					js1 = 'H';
			}
			System.out.println("Coordinates " + js1 + " " + is);
			selectPiece(g, is, js);
			if(selectedQ && checkGrid(is,js) && legalMoves[is][js] == true){
				System.out.println("Activated");
				if(blackTile(is, js)){
					movePiece(oldX,oldY,is,js,pieces[oldX][oldY]);
					resetMoves();
					drawPieces(g);
					selectedQ = false;
					checkGameOver();
					playerTurn = 1;
				}
			} 
			else if(pieces[is][js] == 2 || pieces[is][js] == 4) {
				System.out.println("Piece Selected");
				selectPiece(g, is, js);
				resetMoves();
				addLegalMoves(pieces[is][js],is,js);
				drawPieces(g);
				oldX = is;
				oldY = js;
				selectedQ = true;
			}
			else if (selected == 0 && is >=0 && is <= 7 && js >=0 && js <= 7) {
				selectPiece(g, is, js);
			}
		}
	}
}