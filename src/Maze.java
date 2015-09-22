import javax.swing.*;
import java.awt.*;

public class Maze extends JPanel {
    private final int blockSize = 30;
    private final int numberOfBlocks = 15;
    private final int screenSize = numberOfBlocks * blockSize;
    private final Color dotColor = Color.YELLOW; //new Color(192, 192, 0);
    private Color mazeColor;
    private Image invImage = new ImageIcon("images/invDot.gif").getImage();

    public int getNumberOfBlocks(){
        return numberOfBlocks;
    }
    public int getBlockSize(){
        return blockSize;
    }

    public int getScreenSize(){
        return screenSize;
    }

    Maze(){
    }

    public void drawMaze(Graphics2D graphics, short[] screenData, short[] bonusData) {

        short i = 0;
        int x, y;

        for (y = 0; y < screenSize; y += blockSize) {
            for (x = 0; x < screenSize; x += blockSize) {

            	graphics.setColor(mazeColor);
            	graphics.setStroke(new BasicStroke(2));

                if ((screenData[i] & ScreenDataConstants.leftBorder) != 0) {
                	graphics.drawLine(x, y, x, y + blockSize - 1);
                }

                if ((screenData[i] & ScreenDataConstants.topBorder) != 0) {
                	graphics.drawLine(x, y, x + blockSize - 1, y);
                }

                if ((screenData[i] & ScreenDataConstants.rightBorder) != 0) {
                	graphics.drawLine(x + blockSize - 1, y,
                            x + blockSize - 1, y + blockSize - 1);
                }

                if ((screenData[i] & ScreenDataConstants.bottomBorder) != 0) {
                	graphics.drawLine(x, y + blockSize - 1,
							x + blockSize - 1, y + blockSize - 1);
                }

                if ((screenData[i] & ScreenDataConstants.dotToEat) != 0) {
                	graphics.setColor(dotColor);
                	graphics.fillRect(x + 14, y + 14, 2, 2);
                }

                if (bonusData[i] == 1) {
                	graphics.setColor(dotColor);
                	graphics.fillOval(x + 10, y + 10, 10, 10);
                }
                if (bonusData[i] == 2) {
                	
                	graphics.drawImage(invImage, x, y, this);
                }
                        
                i++;
            }
        }
    }


    public boolean checkNoDots(short[] screenData){

        short i = 0;
        while (i < numberOfBlocks * numberOfBlocks) {

            if ((screenData[i] & ScreenDataConstants.dotToEat) != 0) {

                return false;
            }

            i++;
        }
        return true;
    }
    
    public void initMaze() {
    	
        mazeColor = Color.BLUE;
    }

}
