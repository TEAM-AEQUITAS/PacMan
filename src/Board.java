import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Board extends Pacman implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Dimension dimension;
	private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);

	private Image ii;
	private final Color dotColor = new Color(192, 192, 0);
	private Color mazeColor;

	private boolean dying = false;

	private final int screenSize = numberOfBlocks * blockSize;
	private final int pacAnimDelay = 2;
	private final int pacmanAnimCount = 4;

	private int pacAnimCount = pacAnimDelay;
	private int pacAnimDir = 1;
	protected int pacmanLivesLeft;
	private Ghost[] ghosts;

	// private Image ghost;

	private final short levelData[] = {
			19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22, 
			21,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 
			21,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21,  0,  0,  0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20, 
            17, 18, 18, 18, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 20, 
            17, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 24, 20, 
            25, 16, 16, 16, 24, 24, 28,  0, 25, 24, 24, 16, 20,  0, 21, 
            1,  17, 16, 20,  0,  0,  0,  0,  0,  0,  0, 17, 20,  0, 21, 
            1,  17, 16, 16, 18, 18, 22,  0, 19, 18, 18, 16, 20,  0, 21, 
            1,  17, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21,
			1,  17, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21, 
			1,  17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20,  0, 21, 
			1,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 21, 
			1,  25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20, 
			9,   8,  8,  8,  8,  8,  8,  8,  8,  8, 25, 24, 24, 24, 28 };
			
	private final int maxSpeed = 6;

	private int currentSpeed = 2;
	public Timer timer;

	private TAdapter adapter;

	public Board() {

		loadImages();
		initVariables();

		adapter = new TAdapter(this);

		addKeyListener(adapter);

		setFocusable(true);

		setBackground(Color.black);
		setDoubleBuffered(true);
	}

	private void initVariables() {

		screenData = new short[numberOfBlocks * numberOfBlocks];
		mazeColor = new Color(5, 100, 5);
		dimension = new Dimension(600, 600);
		timer = new Timer(40, this);
		timer.start();
	}

	@Override
	public void addNotify() {
		super.addNotify();

		initGame();
	}

	private void doAnim() {

		pacAnimCount--;

		if (pacAnimCount <= 0) {
			pacAnimCount = pacAnimDelay;
			pacmanAnimPos = pacmanAnimPos + pacAnimDir;

			if (pacmanAnimPos == (pacmanAnimCount - 1) || pacmanAnimPos == 0) {
				pacAnimDir = -pacAnimDir;
			}
		}
	}

	private void playGame(Graphics2D graphics) {

		if (dying) {

			death();

		} else {

			movePacman();
			drawPacman(graphics);
			moveGhosts(graphics);
			checkMaze();
		}
	}

	private void showIntroScreen(Graphics2D g2d) {

		g2d.setColor(new Color(0, 32, 48));
		g2d.fillRect(50, screenSize / 2 - 30, screenSize - 100, 50);
		g2d.setColor(Color.white);
		g2d.drawRect(50, screenSize / 2 - 30, screenSize - 100, 50);

		String s = "Press ENTER to start.";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		g2d.setColor(Color.white);
		g2d.setFont(small);
		g2d.drawString(s, (screenSize - metr.stringWidth(s)) / 2,
				screenSize / 2);
	}

	private void drawScore(Graphics2D g) {

		int i;
		String s;

		g.setFont(smallFont);
		g.setColor(new Color(96, 128, 255));
		s = "Score: " + score;
		g.drawString(s, screenSize / 2 + 96, screenSize + 16);

		for (i = 0; i < pacmanLivesLeft; i++) {
			g.drawImage(pacman3left, i * 28 + 8, screenSize + 1, this);
		}
	}

	private void checkMaze() {

		short i = 0;
		boolean finished = true;

		while (i < numberOfBlocks * numberOfBlocks && finished) {

			if ((screenData[i] & 48) != 0) {
				finished = false;
			}

			i++;
		}

		if (finished) {

			score += 50;

			if (currentSpeed < maxSpeed) {
				currentSpeed++;
			}

			initLevel();
		}
	}

	private void death() {

		pacmanLivesLeft--;

		if (pacmanLivesLeft == 0) {
			adapter.isPlaying = false;
		}

		continueLevel();
	}

	private void moveGhosts(Graphics2D g2d) {

		for (short i = 0; i < ghosts.length; i++) {
			Ghost ghost = ghosts[i];
			ghost.move(screenData, blockSize, g2d);
			//drawGhost(g2d, ghost.ghostX + 1, ghost.ghostY + 1);
			if (pacmanX > (ghost.ghostX - 15) && pacmanX < (ghost.ghostX + 15)
					&& pacmanY > (ghost.ghostY - 15)
					&& pacmanY < (ghost.ghostY + 15) && adapter.isPlaying) {

				dying = true;
			}
		}
	}

	private void drawMaze(Graphics2D g2d) {

		short i = 0;
		int x, y;

		for (y = 0; y < screenSize; y += blockSize) {
			for (x = 0; x < screenSize; x += blockSize) {

				g2d.setColor(mazeColor);
				g2d.setStroke(new BasicStroke(2));

				// Draw wall above block
				if ((screenData[i] & 1) != 0) {
					g2d.drawLine(x, y, x, y + blockSize - 1);
				}
				// Draw wall to the right of the block
				if ((screenData[i] & 2) != 0) {
					g2d.drawLine(x, y, x + blockSize - 1, y);
				}

				if ((screenData[i] & 4) != 0) {
					g2d.drawLine(x + blockSize - 1, y, x + blockSize - 1, y
							+ blockSize - 1);
				}

				if ((screenData[i] & 8) != 0) {
					g2d.drawLine(x, y + blockSize - 1, x + blockSize - 1, y
							+ blockSize - 1);
				}

				if ((screenData[i] & 16) != 0) {
					g2d.setColor(dotColor);
					g2d.fillRect(x + 11, y + 11, 2, 2);
				}

				i++;
			}
		}
	}

	public void SetMovement(int dx, int dy) {
		this.reqDimensionX = dx;
		this.reqDimensionY = dy;
	}

	public void initGame() {

		pacmanLivesLeft = 3;
		// Array of ghosts
		ghosts = new Ghost[] { 
		      new Ghost(0, 0), 
		      new Ghost(4 * blockSize, 4 * blockSize), 
		      new Ghost(14 * blockSize, 0), 
		      new Ghost(blockSize*2, 13 * blockSize)
		};

		score = 0;

		initLevel();

		currentSpeed = 3;
	}

	private void initLevel() {

		int i;
		for (i = 0; i < numberOfBlocks * numberOfBlocks; i++) {
			screenData[i] = levelData[i];
		}

		continueLevel();
	}

	private void continueLevel() {

		short i;
		int dx = 1;


		for (i = 0; i < ghosts.length; i++) {
			//ghosts[i].ghostX = 4 * blockSize;
			//ghosts[i].ghostY = 4 * blockSize;
			ghosts[i].ghostDY = 0;
			ghosts[i].ghostDX = dx;
			dx = -dx;
		}

		pacmanX = 7 * blockSize;
		pacmanY = 11 * blockSize;
		pacmanDimensionX = 0;
		pacmanDimensionY = 0;
		reqDimensionX = 0;
		reqDimensionY = 0;
		viewDimensionX = -1;
		viewDimensionY = 0;
		dying = false;
	}

	private void loadImages() {

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

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	private void doDrawing(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, dimension.width, dimension.height);

		drawMaze(g2d);
		drawScore(g2d);
		doAnim();

		if (adapter.isPlaying) {
			playGame(g2d);
		} else {
			showIntroScreen(g2d);
		}

		g2d.drawImage(ii, 5, 5, this);
		Toolkit.getDefaultToolkit().sync();
		g2d.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		repaint();
	}
}
