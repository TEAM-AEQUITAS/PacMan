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
	private final int validspeeds[] = { 2, 3, 4, 5, 6, 8 };
	
	public Ghost(int initialPositionX, int initialPositionY, int level)  {
		
		ghostX = initialPositionX;
		ghostY = initialPositionY;
		
		speed = validspeeds[level];	
	}

    public void move(short screenData[], int blockSize, Graphics2D g2d) {

        int position;
        int count;
        // Width and height of the game field
        int numberOfBlocks = 15;
		int dx[] = new int[4];
		int dy[] = new int[4];

        if (ghostX % blockSize == 0 && ghostY % blockSize == 0) {
            position = ghostX / blockSize + numberOfBlocks
                    * (int) (ghostY / blockSize);

            count = 0;

            if ((screenData[position] & ScreenDataConstants.leftBorder) == 0 && ghostDX != 1) {
                dx[count] = -1;
                dy[count] = 0;
                count++;
            }

            if ((screenData[position] & ScreenDataConstants.topBorder) == 0 && ghostDY != 1) {
                dx[count] = 0;
                dy[count] = -1;
                count++;
            }

            if ((screenData[position] & ScreenDataConstants.rightBorder) == 0 && ghostDX != -1) {
                dx[count] = 1;
                dy[count] = 0;
                count++;
            }

            if ((screenData[position] & ScreenDataConstants.bottomBorder) == 0 && ghostDY != -1) {
                dx[count] = 0;
                dy[count] = 1;
                count++;
            }

            if (count == 0) {

                if ((screenData[position] & ScreenDataConstants.allBorders) == 15) {
                    ghostDX = 0;
                    ghostDY = 0;
                } else {
                    ghostDX = -ghostDX;
                    ghostDY = -ghostDY;
                }

            } else {
                int selected = (int) (Math.random() * count);

				if (selected >= count)
					selected = count-1;

                ghostDX = dx[selected];
                ghostDY = dy[selected];
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
