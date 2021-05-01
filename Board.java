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
		int[][] pieces = 
		{{-1, 1, -1, 1, -1, 1, -1, 1},
		{1, -1, 1, -1, 1, -1, 1, -1},
		{-1, 1, -1, 1, -1, 1, -1, 1},
		{0, -1, 0 ,-1, 0, -1, 0, -1},
		{-1, 0, -1, 0, -1, 0, -1, 0},
		{2, -1, 2, -1, 2, -1, 2, -1},
		{-1, 2, -1, 2, -1, 2, -1, 2},
		{2, -1, 2, -1, 2, -1, 2, -1}};
		


		Font unselectedFont, selectedFont;
		boolean gameStart;
		
		CvBoard(){
			gameStart = false;
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent click) {
					xP = click.getX();
					yP = click.getY();
					repaint();
				}
			});
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
						g.setColor(Color.CYAN);
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
			unselectedFont = new Font("Helvetica", Font.PLAIN, 20);
			selectedFont = new Font("Helvetica", Font.BOLD, 20);
			g.setFont(unselectedFont);
			for (int i = 0; i < 8; i++) {
				g.drawString(Character.toString((char)i+65), (left+(2*boxWidth/3)), (top+(boxHeight*i)+((3*boxHeight)/2)));
			}
			for (int j = 0; j < 8; j++) {
				g.drawString(new String(""+j), (left+(boxWidth*j)+((3*boxWidth)/2)), (top+(4*boxHeight)/5));
			}
			drawPieces(g);
		}
		
		void selectPiece(Graphics g, int i, int j) {
			g.setColor(Color.BLACK);
			g.setFont(selectedFont);
			g.drawString(Character.toString((char)i+65), (left+(2*boxWidth)/3), (top+(boxHeight*i)+((3*boxHeight)/2)));
			g.drawString(new String(""+j), (left+(boxWidth*j)+((3*boxWidth)/2)), (top+(4*boxHeight)/5));
			
			g.setFont(unselectedFont);
			for (int w = 0; w < 8; w++) {
				if (w == i) {
					g.setFont(selectedFont);
					g.drawString(Character.toString((char)i+65), (left+(2*boxWidth/3)), (top+(boxHeight*i)+((3*boxHeight)/2)));
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
						selected = 1;
						oldX = i;
						oldY = j;
					}
					else if(pieces[i][j] == 2){
						g.setColor(Color.CYAN.darker());
						g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
						g.fillOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
						selected = 2;
						oldX = i;
						oldY = j;
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
			//drawPieces(g);
			if (xP != 1000000 && yP != 1000000) {
				int js = (xP - leftStart)/boxWidth;
				int is = (yP - topStart)/boxHeight;
				if(selected == 1){
					if(blackTile(is, js)){
						pieces[is][js] = 1;
						selected = 0;
						pieces[oldX][oldY] = 0;
						oldX = 0;
						oldY = 0;
						drawPieces(g);
					}
				}
				else if(selected == 2){
					if(blackTile(is, js)){
						pieces[is][js] = 2;
						selected = 0;
						pieces[oldX][oldY] = 0;
						oldX = 0;
						oldY = 0;
						drawPieces(g);
					}
				}
				else if (selected == 0 && is >=0 && is <= 7 && js >=0 && js <= 7) {
					selectPiece(g, is, js);
				}
			}
		}
	}

