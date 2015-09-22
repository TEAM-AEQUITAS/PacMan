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
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Dimension dimension;
	private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);

	private boolean dying = false;

	private final int pacAnimDelay = 2;
	private final int pacmanAnimCount = 4;

	private int pacAnimCount = pacAnimDelay;
	private int pacAnimDir = 1;
	private int level;
	private Ghost[] ghosts;
	private Pacman pacman;
	Maze maze = new Maze();

	private final short levelData[] = { 19, 26, 26, 18, 26, 26, 26, 18, 26, 26,
			26, 18, 26, 26, 22, 21, 0, 0, 21, 0, 0, 0, 21, 0, 0, 0, 21, 0, 0,
			21, 21, 0, 19, 16, 18, 18, 18, 16, 18, 18, 18, 16, 22, 0, 21, 21,
			0, 17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0, 21, 21, 0, 17,
			16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21, 21, 0, 17, 16, 16,
			16, 20, 0, 17, 16, 16, 16, 20, 0, 21, 21, 0, 17, 16, 24, 24, 28, 0,
			25, 24, 24, 16, 20, 0, 21, 17, 26, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17,
			16, 26, 20, 21, 0, 17, 16, 18, 18, 22, 0, 19, 18, 18, 16, 20, 0,
			21, 21, 0, 17, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21, 21, 0,
			17, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21, 21, 0, 17, 16,
			16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21, 21, 0, 25, 16, 24, 24,
			24, 16, 24, 24, 24, 16, 28, 0, 21, 21, 0, 0, 21, 0, 0, 0, 21, 0, 0,
			0, 21, 0, 0, 21, 25, 26, 26, 24, 26, 26, 26, 24, 26, 26, 26, 24,
			26, 26, 28 };

	public Timer timer;

	private TAdapter adapter;

	public Board() {

		pacman = new Pacman();
		pacman.loadImages();
		initVariables();

		adapter = new TAdapter(this);

		addKeyListener(adapter);

		setFocusable(true);

		setBackground(Color.black);
		setDoubleBuffered(true);
	}

	private void initVariables() {

		pacman.screenData = new short[maze.getNumberOfBlocks()
				* maze.getNumberOfBlocks()];
		pacman.bonusData = new short[maze.getNumberOfBlocks()
				* maze.getNumberOfBlocks()];
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
			pacman.pacmanAnimPos = pacman.pacmanAnimPos + pacAnimDir;

			if (pacman.pacmanAnimPos == (pacmanAnimCount - 1)
					|| pacman.pacmanAnimPos == 0) {
				pacAnimDir = -pacAnimDir;
			}
		}
	}

	private void playGame(Graphics2D graphics) {

		if (dying) {

			death();

		} else {

			pacman.movePacman(maze);
			pacman.drawPacman(graphics);
			moveGhosts(graphics);
			checkMaze();
		}
	}

	private void showIntroScreen(Graphics2D graphics) {

		graphics.setColor(new Color(0, 32, 48));
		graphics.fillRect(50, maze.getScreenSize() / 2 - 30,
				maze.getScreenSize() - 100, 50);
		graphics.setColor(Color.white);
		graphics.drawRect(50, maze.getScreenSize() / 2 - 30,
				maze.getScreenSize() - 100, 50);

		String s = "Press ENTER to start.";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		graphics.setColor(Color.white);
		graphics.setFont(small);
		graphics.drawString(s, (maze.getScreenSize() - metr.stringWidth(s)) / 2,
				maze.getScreenSize() / 2);
	}

	private void drawScoreAndLevel(Graphics2D g) {

		int i;
		String scoreString;
		String levelString;

		g.setFont(smallFont);
		g.setColor(Color.BLUE);
		scoreString = "Score: " + pacman.score;
		levelString = "Level: " + level;
		g.drawString(levelString, maze.getScreenSize() / 2,
				maze.getScreenSize() + 16);
		g.drawString(scoreString, maze.getScreenSize() / 2 + 96,
				maze.getScreenSize() + 16);

		Image pacman3left = new ImageIcon("images/pacman3left.gif").getImage();
		for (i = 0; i < pacman.pacmanLivesLeft; i++) {
			g.drawImage(pacman3left, i * 28 + 8, maze.getScreenSize() + 1, this);
		}
	}

	private void checkMaze() {

		if (maze.checkNoDots(pacman.screenData)) {

			pacman.score += 50;
			level++;
			initLevel();
		}
	}

	private void death() {

		pacman.pacmanLivesLeft--;

		if (pacman.pacmanLivesLeft == 0) {
			adapter.isPlaying = false;
			initGame();

		}

		continueLevel();
	}

	private void moveGhosts(Graphics2D graphics) {

		for (short i = 0; i < ghosts.length; i++) {
			Ghost ghost = ghosts[i];
			ghost.move(pacman.screenData, maze.getBlockSize(), graphics,
					pacman.pacmanX / maze.getBlockSize(),
					pacman.pacmanY / maze.getBlockSize());

			if (pacman.pacmanX > (ghost.ghostX - 15)
					&& pacman.pacmanX < (ghost.ghostX + 15)
					&& pacman.pacmanY > (ghost.ghostY - 15)
					&& pacman.pacmanY < (ghost.ghostY + 15)
					&& adapter.isPlaying && !pacman.isInvisible) {

				dying = true;
			}
		}
	}

	public void SetMovement(int dimensionX, int dimensionY) {
		pacman.reqDimensionX = dimensionX;
		pacman.reqDimensionY = dimensionY;
	}

	public void initGame() {

		pacman.pacmanLivesLeft = 3;

		pacman.score = 0;
		level = 1;
		initLevel();

	}

	private void setBonusData() {

		pacman.bonusData = new short[maze.getNumberOfBlocks()
				* maze.getNumberOfBlocks()];
		int i;
		int extras = 0;
		int invisibility = 0;
		for (i = 0; i < maze.getNumberOfBlocks() * maze.getNumberOfBlocks(); i++) {
			Random random = new Random();
			int n = random.nextInt(100);
			if (n <= 1 && levelData[i] != 0 && extras < 3) {

				if (extras == 1 && invisibility == 0) {
					pacman.bonusData[i] = 2;
					// One invisibility dot
					invisibility = 1;
				} else {
					pacman.bonusData[i] = 1;
				}
				extras++;
			} else {
				pacman.bonusData[i] = 0;
			}
		}
	}

	private void initLevel() {
		int i;
		for (i = 0; i < maze.getNumberOfBlocks() * maze.getNumberOfBlocks(); i++) {
			pacman.screenData[i] = levelData[i];
		}

		setBonusData();
		continueLevel();
	}

	private void initGhosts() {

		short i;
		int dx = 1;

		ghosts = new Ghost[] {
				new Ghost(0, 0, level),
				new Ghost(14 * maze.getBlockSize(), 14 * maze.getBlockSize(),level), 
				new Ghost(14 * maze.getBlockSize(), 0, level),
				new Ghost(0, 14 * maze.getBlockSize(), level) };

		for (i = 0; i < ghosts.length; i++) {
			ghosts[i].ghostDY = 0;
			ghosts[i].ghostDX = dx;
		}
	}

	private void continueLevel() {

		initGhosts();
		pacman.pacmanX = 7 * maze.getBlockSize();
		pacman.pacmanY = 11 * maze.getBlockSize();
		pacman.pacmanDimensionX = 0;
		pacman.pacmanDimensionY = 0;
		pacman.reqDimensionX = 0;
		pacman.reqDimensionY = 0;
		pacman.viewDimensionX = -1;
		pacman.viewDimensionY = 0;
		dying = false;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	private void doDrawing(Graphics g) {

		Graphics2D graphics = (Graphics2D) g;

		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, dimension.width, dimension.height);

		maze.drawMaze(graphics, pacman.screenData, pacman.bonusData);
		drawScoreAndLevel(graphics);
		doAnim();

		if (adapter.isPlaying) {
			playGame(graphics);
		} else {
			showIntroScreen(graphics);
		}

		Toolkit.getDefaultToolkit().sync();
		graphics.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		repaint();
	}
}
