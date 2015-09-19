import javax.swing.*;
import java.awt.*;

public class Maze {
    private final int blockSize = 30;
    private final int numberOfBlocks = 15;
    private final int screenSize = numberOfBlocks * blockSize;
    private final Color dotColor = new Color(192, 192, 0);
    private Color mazeColor;

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

    public void drawMaze(Graphics2D g2d, short[] screenData) {


        short i = 0;
        int x, y;

        for (y = 0; y < screenSize; y += blockSize) {
            for (x = 0; x < screenSize; x += blockSize) {

                g2d.setColor(mazeColor);
                g2d.setStroke(new BasicStroke(2));

                if ((screenData[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + blockSize - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + blockSize - 1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + blockSize - 1, y, x + blockSize - 1,
                            y + blockSize - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + blockSize - 1, x + blockSize - 1,
                            y + blockSize - 1);
                }

                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(dotColor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }

                i++;
            }
        }
    }


    public boolean checkNoDots(short[] screenData){
        short i = 0;
        while (i < numberOfBlocks * numberOfBlocks) {

            if ((screenData[i] & 48) != 0) {

                return false;
            }

            i++;
        }
        return true;
    }
    public void initMaze() {
        mazeColor = new Color(5, 100, 5);
    }

}
