import javax.swing.*;
import java.awt.*;

public class Pacman extends JPanel {
    protected final int blockSize = 30;
    protected final int numberOfBlocks = 15;
    private final int pacmanSpeed = 6;
    protected int pacmanAnimPos = 0;
    protected int score;
    protected Image pacman1;
    protected Image pacman2up;
    protected Image pacman2left;
    protected Image pacman2right;
    protected Image pacman2down;
    protected Image pacman3up;
    protected Image pacman3down;
    protected Image pacman3left;
    protected Image pacman3right;
    protected Image pacman4up;
    protected Image pacman4down;
    protected Image pacman4left;
    protected Image pacman4right;
    protected int pacmanX;
    protected int pacmanY;
    protected int pacmanDimensionX;
    protected int pacmanDimensionY;
    protected int reqDimensionX;
    protected int reqDimensionY;
    protected int viewDimensionX;
    protected int viewDimensionY;
    protected short[] screenData;

    protected void movePacman() {

        int position;
        short ch;

        if (reqDimensionX == -pacmanDimensionX && reqDimensionY == -pacmanDimensionY) {
            pacmanDimensionX = reqDimensionX;
            pacmanDimensionY = reqDimensionY;
            viewDimensionX = pacmanDimensionX;
            viewDimensionY = pacmanDimensionY;
        }

        if (pacmanX % blockSize == 0 && pacmanY % blockSize == 0) {
            position = pacmanX / blockSize + numberOfBlocks * (int) (pacmanY / blockSize);
            ch = screenData[position];

            if ((ch & 16) != 0) {
                screenData[position] = (short) (ch & 15);
                score++;
            }

            if (reqDimensionX != 0 || reqDimensionY != 0) {
                if (!((reqDimensionX == -1 && reqDimensionY == 0 && (ch & 1) != 0)
                        || (reqDimensionX == 1 && reqDimensionY == 0 && (ch & 4) != 0)
                        || (reqDimensionX == 0 && reqDimensionY == -1 && (ch & 2) != 0)
                        || (reqDimensionX == 0 && reqDimensionY == 1 && (ch & 8) != 0))) {
                    pacmanDimensionX = reqDimensionX;
                    pacmanDimensionY = reqDimensionY;
                    viewDimensionX = pacmanDimensionX;
                    viewDimensionY = pacmanDimensionY;
                }
            }

            // Check for standstill
            if ((pacmanDimensionX == -1 && pacmanDimensionY == 0 && (ch & 1) != 0)
                    || (pacmanDimensionX == 1 && pacmanDimensionY == 0 && (ch & 4) != 0)
                    || (pacmanDimensionX == 0 && pacmanDimensionY == -1 && (ch & 2) != 0)
                    || (pacmanDimensionX == 0 && pacmanDimensionY == 1 && (ch & 8) != 0)) {
                pacmanDimensionX = 0;
                pacmanDimensionY = 0;
            }
        }
        pacmanX = pacmanX + pacmanSpeed * pacmanDimensionX;
        pacmanY = pacmanY + pacmanSpeed * pacmanDimensionY;
    }

    protected void drawPacman(Graphics2D g2d) {

        if (viewDimensionX == -1) {
            drawPacmanLeft(g2d);
        } else if (viewDimensionX == 1) {
            drawPacmanRight(g2d);
        } else if (viewDimensionY == -1) {
            drawPacmanUp(g2d);
        } else {
            drawPacmanDown(g2d);
        }
    }

    private void drawPacmanUp(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2up, pacmanX + 1, pacmanY + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3up, pacmanX + 1, pacmanY + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4up, pacmanX + 1, pacmanY + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanX + 1, pacmanY + 1, this);
                break;
        }
    }

    private void drawPacmanDown(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2down, pacmanX + 1, pacmanY + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3down, pacmanX + 1, pacmanY + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4down, pacmanX + 1, pacmanY + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanX + 1, pacmanY + 1, this);
                break;
        }
    }

    private void drawPacmanLeft(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2left, pacmanX + 1, pacmanY + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3left, pacmanX + 1, pacmanY + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4left, pacmanX + 1, pacmanY + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanX + 1, pacmanY + 1, this);
                break;
        }
    }

    private void drawPacmanRight(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2right, pacmanX + 1, pacmanY + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3right, pacmanX + 1, pacmanY + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4right, pacmanX + 1, pacmanY + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanX + 1, pacmanY + 1, this);
                break;
        }
    }
}
