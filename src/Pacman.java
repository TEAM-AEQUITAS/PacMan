import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.util.Timer;

import graphics.Play;
import graphics.Sounds;


public class Pacman extends JPanel {

	private static final long serialVersionUID = 1L;
	static Play music;
	protected int pacmanSpeed = 6;
	protected int score;
	// Regular pacman
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
	// Invincible pacman
	protected Image pacman1Inv;
	protected Image pacman2upInv;
	protected Image pacman2leftInv;
	protected Image pacman2rightInv;
	protected Image pacman2downInv;
	protected Image pacman3upInv;
	protected Image pacman3downInv;
	protected Image pacman3leftInv;
	protected Image pacman3rightInv;
	protected Image pacman4upInv;
	protected Image pacman4downInv;
	protected Image pacman4leftInv;
	protected Image pacman4rightInv;
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
	protected int pacmanLivesLeft;
	// bonus data
	protected short[] bonusData;
	protected Timer invisibilityTimer = new Timer();
	protected boolean isInvisible;

	// Timer task to stop invisibility
	class ivincibilityRemoval extends TimerTask {

		@Override
		public void run() {
			if (isInvisible) {
				isInvisible = false;
			}
		}
	}

	protected void movePacman(Maze maze) {
		calculatePacmanPosition(maze);
		pacmanX = pacmanX + pacmanSpeed * pacmanDimensionX;
		pacmanY = pacmanY + pacmanSpeed * pacmanDimensionY;
	}

	private void calculatePacmanPosition(Maze maze) {
		int position;
		short currentData; // Current data from level data
		short bPos; // Position on bonus data array
		if (reqDimensionX == -pacmanDimensionX
				&& reqDimensionY == -pacmanDimensionY) {
			pacmanDimensionX = reqDimensionX;
			pacmanDimensionY = reqDimensionY;
			viewDimensionX = pacmanDimensionX;
			viewDimensionY = pacmanDimensionY;
		}

		if (pacmanX % maze.getBlockSize() == 0
				&& pacmanY % maze.getBlockSize() == 0) {
			position = pacmanX / maze.getBlockSize() + maze.getNumberOfBlocks()
					* (int) (pacmanY / maze.getBlockSize());
			currentData = screenData[position];
			bPos = bonusData[position];

			if ((currentData & ScreenDataConstants.dotToEat) != 0) {
				screenData[position] = (short) (currentData & ~ScreenDataConstants.dotToEat);
				score++;
				 music = new Play("src/data/sounds/boing.mp3", false, 60000);
			     music.start();
			}
			if (bPos == 1) {
				// Maximum lives is 7, to limit life gathering
				if (pacmanLivesLeft < 7) {
					music = new Play("src/data/sounds/kid_laugh.mp3", false, 60000);
				    music.start();
					pacmanLivesLeft += 1;
				}
				bonusData[position] = 0;
			} else if (bPos == 2) {
				// Pacman gets 10 seconds invisibility from ghosts
				music = new Play("src/data/sounds/freeze.mp3", false, 60000);
			    music.start();
				invisibilityTimer.schedule(new ivincibilityRemoval(), 10000);
				isInvisible = true;
				bonusData[position] = 0;
			}

			if (reqDimensionX != 0 || reqDimensionY != 0) {
				if (!((reqDimensionX == -1 && reqDimensionY == 0 && (currentData & ScreenDataConstants.leftBorder) != 0)
						|| (reqDimensionX == 1 && reqDimensionY == 0 && (currentData & ScreenDataConstants.rightBorder) != 0)
						|| (reqDimensionX == 0 && reqDimensionY == -1 && (currentData & ScreenDataConstants.topBorder) != 0) || (reqDimensionX == 0
						&& reqDimensionY == 1 && (currentData & ScreenDataConstants.bottomBorder) != 0))) {
					pacmanDimensionX = reqDimensionX;
					pacmanDimensionY = reqDimensionY;
					viewDimensionX = pacmanDimensionX;
					viewDimensionY = pacmanDimensionY;
				}
			}
			checkForStandstill(currentData);
		}
	}

	// Check if pacman currently moves
	private void checkForStandstill(short cd) {
		if ((pacmanDimensionX == -1 && pacmanDimensionY == 0 && (cd & ScreenDataConstants.leftBorder) != 0)
				|| (pacmanDimensionX == 1 && pacmanDimensionY == 0 && (cd & ScreenDataConstants.rightBorder) != 0)
				|| (pacmanDimensionX == 0 && pacmanDimensionY == -1 && (cd & ScreenDataConstants.topBorder) != 0)
				|| (pacmanDimensionX == 0 && pacmanDimensionY == 1 && (cd & ScreenDataConstants.bottomBorder) != 0)) {
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

	// Draw pacman image depending on it's position
	protected void drawPacmanOriented(Graphics2D g2d, PacmanDirections direction) {
		switch (pacmanAnimPos) {
		case 1:
			g2d.drawImage(getPacManImage(direction, 1), pacmanX + 1,
					pacmanY + 1, this);
			break;
		case 2:
			g2d.drawImage(getPacManImage(direction, 2), pacmanX + 1,
					pacmanY + 1, this);
			break;
		case 3:
			g2d.drawImage(getPacManImage(direction, 3), pacmanX + 1,
					pacmanY + 1, this);
			break;
		default:
			g2d.drawImage(getPacManImage(direction, 4), pacmanX + 1,
					pacmanY + 1, this);
			break;
		}
	}

	private Image getPacManImage(PacmanDirections direction, int position) {

		Image currentImage = pacman1;
		if (isInvisible) {
			currentImage = pacman1Inv;
		}
		switch (position) {
		case 1:
			switch (direction) {
			case UP:
				currentImage = pacman2up;
				if (isInvisible) {
					currentImage = pacman2upInv;
				}
				break;
			case DOWN:
				currentImage = pacman2down;
				if (isInvisible) {
					currentImage = pacman2downInv;
				}
				break;
			case LEFT:
				currentImage = pacman2left;
				if (isInvisible) {
					currentImage = pacman2leftInv;
				}
				break;
			case RIGHT:
				currentImage = pacman2right;
				if (isInvisible) {
					currentImage = pacman2rightInv;
				}
				break;
			}
			break;
		case 2:
			switch (direction) {
			case UP:
				currentImage = pacman3up;
				if (isInvisible) {
					currentImage = pacman3upInv;
				}
				break;
			case DOWN:
				currentImage = pacman3down;
				if (isInvisible) {
					currentImage = pacman3downInv;
				}

				break;
			case LEFT:
				currentImage = pacman3left;
				if (isInvisible) {
					currentImage = pacman3leftInv;
				}
				break;
			case RIGHT:
				currentImage = pacman3right;
				if (isInvisible) {
					currentImage = pacman3rightInv;
				}
				break;
			}
			break;
		case 3:
			switch (direction) {
			case UP:
				currentImage = pacman4up;
				if (isInvisible) {
					currentImage = pacman4upInv;
				}
				break;
			case DOWN:
				currentImage = pacman4down;
				if (isInvisible) {
					currentImage = pacman4downInv;
				}
				break;
			case LEFT:
				currentImage = pacman4left;
				if (isInvisible) {
					currentImage = pacman4leftInv;
				}
				break;
			case RIGHT:
				currentImage = pacman4right;
				if (isInvisible) {
					currentImage = pacman4rightInv;
				}
				break;
			}
			break;
		default:
			currentImage = pacman1;
			if (isInvisible) {
				currentImage = pacman1Inv;
			}
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

		// Invisible pacman (becomes blue)
		pacman1Inv = new ImageIcon("images/pacman1Inv.gif").getImage();
		pacman2upInv = new ImageIcon("images/pacman2upInv.gif").getImage();
		pacman3upInv = new ImageIcon("images/pacman3upInv.gif").getImage();
		pacman4upInv = new ImageIcon("images/pacman4upInv.gif").getImage();
		pacman2downInv = new ImageIcon("images/pacman2downInv.gif").getImage();
		pacman3downInv = new ImageIcon("images/pacman3downInv.gif").getImage();
		pacman4downInv = new ImageIcon("images/pacman4downInv.gif").getImage();
		pacman2leftInv = new ImageIcon("images/pacman2leftInv.gif").getImage();
		pacman3leftInv = new ImageIcon("images/pacman3leftInv.gif").getImage();
		pacman4leftInv = new ImageIcon("images/pacman4leftInv.gif").getImage();
		pacman2rightInv = new ImageIcon("images/pacman2rightInv.gif")
				.getImage();
		pacman3rightInv = new ImageIcon("images/pacman3rightInv.gif")
				.getImage();
		pacman4rightInv = new ImageIcon("images/pacman4rightInv.gif")
				.getImage();

	}

}
