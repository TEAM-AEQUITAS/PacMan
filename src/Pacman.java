import javax.swing.*;

import java.awt.*;

public class Pacman extends JPanel {
	
    private final int pacmanSpeed = 6;
    
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
    protected int pacmanAnimPos = 0;
	protected int pacmanLifesLeft;
	protected short[] bonusData;

    protected void movePacman(Maze maze) {
        calculatePacmanPosition(maze);
        pacmanX = pacmanX + pacmanSpeed * pacmanDimensionX;
        pacmanY = pacmanY + pacmanSpeed * pacmanDimensionY;
    }
      
    private void calculatePacmanPosition(Maze maze) {
        int position;
        short ch;
        short bPos; // Position on bonus data array
        if (reqDimensionX == -pacmanDimensionX && reqDimensionY == -pacmanDimensionY) {
            pacmanDimensionX = reqDimensionX;
            pacmanDimensionY = reqDimensionY;
            viewDimensionX = pacmanDimensionX;
            viewDimensionY = pacmanDimensionY;
        }

        if (pacmanX % maze.getBlockSize() == 0 && pacmanY % maze.getBlockSize() == 0) {
            position = pacmanX / maze.getBlockSize() + maze.getNumberOfBlocks() * (int) (pacmanY / maze.getBlockSize());
            ch = screenData[position];
            bPos = bonusData[position];

            if ((ch & ScreenDataConstants.dotToEat) != 0) {
                screenData[position] = (short) (ch & ~ScreenDataConstants.dotToEat);
                score++;
            }
            if (bPos == 1) {
            	// Maximum lives is 7, to limit life gathering
            	if (pacmanLifesLeft < 7) {
            		pacmanLifesLeft +=1;
            	}
            	bonusData[position] = 0;
            }

            if (reqDimensionX != 0 || reqDimensionY != 0) {
                if (!((reqDimensionX == -1 && reqDimensionY == 0 && (ch & ScreenDataConstants.leftBorder) != 0)
                        || (reqDimensionX == 1 && reqDimensionY == 0 && (ch & ScreenDataConstants.rightBorder) != 0)
                        || (reqDimensionX == 0 && reqDimensionY == -1 && (ch & ScreenDataConstants.topBorder) != 0)
                        || (reqDimensionX == 0 && reqDimensionY == 1 && (ch & ScreenDataConstants.bottomBorder) != 0))) {
                    pacmanDimensionX = reqDimensionX;
                    pacmanDimensionY = reqDimensionY;
                    viewDimensionX = pacmanDimensionX;
                    viewDimensionY = pacmanDimensionY;
                }
            }
            checkForStandstill(ch);
        }
    }
     
    private void checkForStandstill(short ch) {
        if ((pacmanDimensionX == -1 && pacmanDimensionY == 0 && (ch & ScreenDataConstants.leftBorder) != 0)
                || (pacmanDimensionX == 1 && pacmanDimensionY == 0 && (ch & ScreenDataConstants.rightBorder) != 0)
                || (pacmanDimensionX == 0 && pacmanDimensionY == -1 && (ch & ScreenDataConstants.topBorder) != 0)
                || (pacmanDimensionX == 0 && pacmanDimensionY == 1 && (ch & ScreenDataConstants.bottomBorder) != 0)) {
            pacmanDimensionX = 0;
            pacmanDimensionY = 0;
        }
    }

    protected void drawPacman(Graphics2D g2d) {

        if (viewDimensionX == -1) {
            drawPacmanOriented(g2d, PacmanDirections.LEFT);
        } else if (viewDimensionX == 1) {
            drawPacmanOriented(g2d, PacmanDirections.RIGHT);
        } else if (viewDimensionY == -1) {
            drawPacmanOriented(g2d, PacmanDirections.UP);
        } else {
            drawPacmanOriented(g2d, PacmanDirections.DOWN);
        }
    }

    protected void drawPacmanOriented(Graphics2D g2d, PacmanDirections direction) {
        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(getPacManImage(direction, 1), pacmanX + 1, pacmanY + 1, this);
                break;
            case 2:
                g2d.drawImage(getPacManImage(direction, 2), pacmanX + 1, pacmanY + 1, this);
                break;
            case 3:
                g2d.drawImage(getPacManImage(direction, 3), pacmanX + 1, pacmanY + 1, this);
                break;
            default:
                g2d.drawImage(getPacManImage(direction, 4), pacmanX + 1, pacmanY + 1, this);
                break;
        }
    }

    private Image getPacManImage (PacmanDirections direction , int position){

        Image currentImage = pacman1;

            switch (position) {
                case 1:
                    switch (direction){
                        case  UP:
                            currentImage = pacman2up;
                            break;
                        case  DOWN:
                            currentImage = pacman2down;
                            break;
                        case  LEFT:
                            currentImage = pacman2left;
                            break;
                        case  RIGHT:
                            currentImage = pacman2right;
                            break;
                    }
                    break;
                case 2:
                    switch (direction){
                        case  UP:
                            currentImage = pacman3up;
                        break;
                        case  DOWN:
                            currentImage = pacman3down;
                        break;
                        case  LEFT:
                            currentImage = pacman3left;
                        break;
                        case  RIGHT:
                            currentImage = pacman3right;
                        break;
                    }
                    break;
                case 3:
                    switch (direction){
                        case  UP:
                            currentImage = pacman4up;
                        break;
                        case  DOWN:
                            currentImage = pacman4down;
                        break;
                        case  LEFT:
                            currentImage = pacman4left;
                        break;
                        case  RIGHT:
                            currentImage = pacman4right;
                        break;
                    }
                    break;
                default:
                    currentImage = pacman1;
                    break;
            }
        return currentImage;
    }
    
    public void loadImages() {

		pacman1 = new ImageIcon("images/pacman1.gif").getImage();
		pacman2up = new ImageIcon("images/pacman2up.gif").getImage();
		pacman3up = new ImageIcon("images/pacman3up.gif").getImage();
		pacman4up = new ImageIcon("images/pacman4up.gif").getImage();
		pacman2down = new ImageIcon("images/pacman2down.gif").getImage();
		pacman3down = new ImageIcon("images/pacman3down.gif").getImage();
		pacman4down = new ImageIcon("images/pacman4down.gif").getImage();
		pacman2left = new ImageIcon("images/pacman2left.gif").getImage();
		pacman3left = new ImageIcon("images/pacman3left.gif").getImage();
		pacman4left = new ImageIcon("images/pacman4left.gif").getImage();
		pacman2right = new ImageIcon("images/pacman2right.gif").getImage();
		pacman3right = new ImageIcon("images/pacman3right.gif").getImage();
		pacman4right = new ImageIcon("images/pacman4right.gif").getImage();

	}
    
}
