import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Ghost extends JPanel {

	//Current position X
	public int ghostX;
	//Current position Y
	public int ghostY;
	public int ghostDX;
	public int ghostDY;
	private int speed;
	private int dx[] = new int[4];
	private int dy[] = new int[4];
	private final int validspeeds[] = { 1, 2, 3, 4, 6, 8 };
	
	public Ghost(int initX, int initY)  {
		
		ghostX = initX;
		ghostY = initY;
		
		int random = (int) (Math.random() * 5);

		if (random > 5) {
			random = 5;
		}
		speed = validspeeds[random];	
	}

	public void move(short screenData[], int blockSize, Graphics2D g2d) {

		int pos;
		int count;
		// Width and height of the game field
		int numberOfBlocks = 15;

		if (ghostX % blockSize == 0 && ghostY % blockSize == 0) {
			pos = ghostX / blockSize + numberOfBlocks
					* (int) (ghostY / blockSize);

			count = 0;
            		
			if ((screenData[pos] & 1) == 0 && ghostDX != 1) {
				dx[count] = -1;
				dy[count] = 0;
				count++;
			}

			if ((screenData[pos] & 2) == 0 && ghostDY != 1) {
				dx[count] = 0;
				dy[count] = -1;
				count++;
			}

			if ((screenData[pos] & 4) == 0 && ghostDX != -1) {
				dx[count] = 1;
				dy[count] = 0;
				count++;
			}

			if ((screenData[pos] & 8) == 0 && ghostDY != -1) {
				dx[count] = 0;
				dy[count] = 1;
				count++;
			}

			if (count == 0) {

				if ((screenData[pos] & 15) == 15) {
					ghostDX = 0;
					ghostDY = 0;
				} else {
					ghostDX = -ghostDX;
					ghostDY = -ghostDY;
				}

			} else {

				count = (int) (Math.random() * count);

				if (count > 3) {
					count = 3;
				}

				ghostDX = dx[count];
				ghostDY = dy[count];
			}
		}
			ghostX = ghostX + (ghostDX * speed);
			ghostY = ghostY + (ghostDY * speed);
			
			drawGhost(g2d, ghostX + 1, ghostY + 1);

		
	}

	private void drawGhost(Graphics2D g2d, int x, int y) {

		Image ghostImage = new ImageIcon("images/ghost.gif").getImage();
		g2d.drawImage(ghostImage, x, y, this);
	}
}
