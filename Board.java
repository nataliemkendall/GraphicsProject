import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
	
	class CvBoard extends Canvas {
		float x0, y0, rWidth = 10.0F, rHeight = 10.0F, pixelSize;
		int centerX, centerY, maxX, maxY;	
		
		void drawGrid(Graphics g){
			int left = iX(-rWidth/2);
			int right = iX(rWidth/2);
			int top = iY(rHeight/2);
			int bottom = iY(-rHeight/2);
			   
			int dY = bottom-top; int dX = right-left;
			int boxWidth = dX/10; int boxHeight = dY/10;
			int numBoxes = 8;	//Board is 8x8
			int circleWidth = (boxWidth*2)/3; int circleHeight = (boxHeight*2)/3;

			int leftStart = left + boxWidth; int topStart = top + boxHeight;	//Leave some whitespace around the board
			int leftCircleStart = leftStart + boxWidth/6; int topCircleStart = topStart + boxHeight/6;
			
			for (int i = 0; i < numBoxes; i++) {
				for (int j = 0; j < numBoxes; j++) {
					g.drawRect(leftStart+(boxWidth*j), topStart+(boxHeight*i), boxWidth, boxHeight);
					if (j % 2 == 0 && i % 2 != 0 || j % 2 != 0 && i % 2 == 0) {
						g.setColor(Color.BLACK);
						g.fillRect(leftStart+(boxWidth*j), topStart+1+(boxHeight*i), boxWidth, boxHeight-1);
					}
				}
			}
			
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < numBoxes; j++) {
					if (j % 2 == 0 && i % 2 != 0 || j % 2 != 0 && i % 2 == 0) {
						g.setColor(Color.BLACK);
						g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*i), circleWidth, circleHeight);
						g.setColor(Color.RED);
						g.fillOval(leftCircleStart+1+(boxWidth*j), topCircleStart+1+(boxHeight*i), circleWidth-1, circleHeight-1);
					}
						
					else {
						g.setColor(Color.BLACK);
						g.drawOval(leftCircleStart+(boxWidth*j), topCircleStart+(boxHeight*(7-i)), circleWidth, circleHeight);
						g.setColor(Color.WHITE);
						g.fillOval(leftCircleStart+1+(boxWidth*j), topCircleStart+1+(boxHeight*(7-i)), circleWidth-1, circleHeight-1);
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
		}
	}
}
