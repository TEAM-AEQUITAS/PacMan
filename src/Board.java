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

	private boolean dying = false;

	private final int pacAnimDelay = 2;
	private final int pacmanAnimCount = 4;

	private int pacAnimCount = pacAnimDelay;
	private int pacAnimDir = 1;
	protected int pacmanLivesLeft;
	private int level;
	private Ghost[] ghosts;
	Maze maze = new Maze();

	private final short levelData[] = {
			19, 26, 26, 18, 26, 26, 26, 18, 26, 26, 26, 18, 26, 26, 22, 
			21,  0,  0, 21,  0,  0,  0, 21,  0,  0,  0, 21,  0,  0, 21, 
			21,  0, 19, 16, 18, 18, 18, 16, 18, 18, 18, 16, 22,  0, 21,
            21,  0, 17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 20,  0, 21, 
            21,  0, 17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21, 
            21,  0, 17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21, 
            21,  0, 17, 16, 24, 24, 28,  0, 25, 24, 24, 16, 20,  0, 21, 
            17, 26, 16, 20,  0,  0,  0,  0,  0,  0,  0, 17, 16, 26, 20, 
            21,  0, 17, 16, 18, 18, 22,  0, 19, 18, 18, 16, 20,  0, 21, 
            21,  0, 17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21,
			21,  0, 17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21, 
			21,  0, 17, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20,  0, 21, 
			21,  0, 25, 16, 24, 24, 24, 16, 24, 24, 24, 16, 28,  0, 21, 
			21,  0,  0, 21,  0,  0,  0, 21,  0,  0,  0, 21,  0,  0, 21, 
			25, 26, 26, 24, 26, 26, 26, 24, 26, 26, 26, 24, 26, 26, 28 };
			
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

		screenData = new short[maze.getNumberOfBlocks() * maze.getNumberOfBlocks()];
		maze.initMaze();
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

			movePacman(maze);
			drawPacman(graphics);
			moveGhosts(graphics);
			checkMaze();
		}
	}

	private void showIntroScreen(Graphics2D g2d) {

		g2d.setColor(new Color(0, 32, 48));
		g2d.fillRect(50, maze.getScreenSize() / 2 - 30,  maze.getScreenSize() - 100, 50);
		g2d.setColor(Color.white);
		g2d.drawRect(50,  maze.getScreenSize() / 2 - 30,  maze.getScreenSize() - 100, 50);

		String s = "Press ENTER to start.";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		g2d.setColor(Color.white);
		g2d.setFont(small);
		g2d.drawString(s, ( maze.getScreenSize() - metr.stringWidth(s)) / 2,
				maze.getScreenSize() / 2);
	}

	private void drawScore(Graphics2D g) {

		int i;
		String s;

		g.setFont(smallFont);
		g.setColor(new Color(96, 128, 255));
		s = "Score: " + score;
		g.drawString(s,  maze.getScreenSize() / 2 + 96,  maze.getScreenSize() + 16);

		for (i = 0; i < pacmanLivesLeft; i++) {
			g.drawImage(pacman3left, i * 28 + 8,  maze.getScreenSize() + 1, this);
		}
	}

	private void checkMaze() {

		if (maze.checkNoDots(screenData)) {

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
			ghost.move(screenData, maze.getBlockSize(), g2d, pacmanX/maze.getBlockSize(), pacmanY/maze.getBlockSize());
			//drawGhost(g2d, ghost.ghostX + 1, ghost.ghostY + 1);
			if (pacmanX > (ghost.ghostX - 15) && pacmanX < (ghost.ghostX + 15)
					&& pacmanY > (ghost.ghostY - 15)
					&& pacmanY < (ghost.ghostY + 15) && adapter.isPlaying) {

				dying = true;
			}
		}
	}


	public void SetMovement(int dimensionX, int dimensionY) {
		this.reqDimensionX = dimensionX;
		this.reqDimensionY = dimensionY;
	}

	public void initGame() {

		pacmanLivesLeft = 3;
		
		score = 0;

		initLevel();

		currentSpeed = 3;
	}

	private void initLevel() {
		int i;
		for (i = 0; i < maze.getNumberOfBlocks()* maze.getNumberOfBlocks(); i++) {
			screenData[i] = levelData[i];
		}

		continueLevel();
	}

	private void initGhosts() {
		
		short i;
		int dx = 1;
		level = 1;
		
		ghosts = new Ghost[] { 
			      new Ghost(0, 0, level), 
			      new Ghost(14 * maze.getBlockSize(), 14 * maze.getBlockSize(), level),
			      new Ghost(14 * maze.getBlockSize(), 0, level),
			      new Ghost(0, 14 * maze.getBlockSize(), level)
			};
		
		for (i = 0; i < ghosts.length; i++) {	
			ghosts[i].ghostDY = 0;
			ghosts[i].ghostDX = dx;
		}
	}
	
	private void continueLevel() {

	

		initGhosts();

		pacmanX = 7 * maze.getBlockSize();
		pacmanY = 11 * maze.getBlockSize();
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

		maze.drawMaze(g2d,screenData);
		drawScore(g2d);
		doAnim();

		if (adapter.isPlaying) {
			playGame(g2d);
		} else {
			showIntroScreen(g2d);
		}
	
		Toolkit.getDefaultToolkit().sync();
		g2d.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		repaint();
	}
}
