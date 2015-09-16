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
	private Dimension d;
    private final Font smallfont = new Font("Helvetica", Font.BOLD, 14);

    private Image ii;
    private final Color dotcolor = new Color(192, 192, 0);
    private Color mazecolor;

    private boolean ingame = false;
    private boolean dying = false;

    private final int screenSize = numberOfBlocks * blockSize;
    private final int pacanimdelay = 2;
    private final int pacmananimcount = 4;
    private final int maxghosts = 12;

    private int pacanimcount = pacanimdelay;
    private int pacanimdir = 1;
    private int nrofghosts = 6;
    protected int pacsleft;
    private int[] dx, dy;
    private int[] ghostx, ghosty, ghostdx, ghostdy, ghostspeed;

    private Image ghost;

    private final short leveldata[] = {
        19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21, 0, 0, 0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
        17, 18, 18, 18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 24, 20,
        25, 16, 16, 16, 24, 24, 28, 0, 25, 24, 24, 16, 20, 0, 21,
        1, 17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 21,
        1, 17, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 21,
        1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
        9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28
    };

    private final int validspeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxspeed = 6;

    private int currentspeed = 2;
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
        mazecolor = new Color(5, 100, 5);
        d = new Dimension(600, 600);
        ghostx = new int[maxghosts];
        ghostdx = new int[maxghosts];
        ghosty = new int[maxghosts];
        ghostdy = new int[maxghosts];
        ghostspeed = new int[maxghosts];
        dx = new int[4];
        dy = new int[4];
        
        timer = new Timer(40, this);
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        initGame();
    }

    private void doAnim() {

        pacanimcount--;

        if (pacanimcount <= 0) {
            pacanimcount = pacanimdelay;
            pacmanAnimpos = pacmanAnimpos + pacanimdir;

            if (pacmanAnimpos == (pacmananimcount - 1) || pacmanAnimpos == 0) {
                pacanimdir = -pacanimdir;
            }
        }
    }

    private void playGame(Graphics2D g2d) {

        if (dying) {

            death();

        } else {

            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
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
        g2d.drawString(s, (screenSize - metr.stringWidth(s)) / 2, screenSize / 2);
    }

    private void drawScore(Graphics2D g) {

        int i;
        String s;

        g.setFont(smallfont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + score;
        g.drawString(s, screenSize / 2 + 96, screenSize + 16);

        for (i = 0; i < pacsleft; i++) {
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

            if (nrofghosts < maxghosts) {
                nrofghosts++;
            }

            if (currentspeed < maxspeed) {
                currentspeed++;
            }

            initLevel();
        }
    }

    private void death() {

        pacsleft--;

        if (pacsleft == 0) {
            ingame = false;
        }

        continueLevel();
    }

    private void moveGhosts(Graphics2D g2d) {

        short i;
        int pos;
        int count;

        for (i = 0; i < nrofghosts; i++) {
            if (ghostx[i] % blockSize == 0 && ghosty[i] % blockSize == 0) {
                pos = ghostx[i] / blockSize + numberOfBlocks * (int) (ghosty[i] / blockSize);

                count = 0;

                if ((screenData[pos] & 1) == 0 && ghostdx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && ghostdy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghostdx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghostdy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screenData[pos] & 15) == 15) {
                        ghostdx[i] = 0;
                        ghostdy[i] = 0;
                    } else {
                        ghostdx[i] = -ghostdx[i];
                        ghostdy[i] = -ghostdy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghostdx[i] = dx[count];
                    ghostdy[i] = dy[count];
                }

            }

            ghostx[i] = ghostx[i] + (ghostdx[i] * ghostspeed[i]);
            ghosty[i] = ghosty[i] + (ghostdy[i] * ghostspeed[i]);
            drawGhost(g2d, ghostx[i] + 1, ghosty[i] + 1);

            if (pacmanX > (ghostx[i] - 12) && pacmanX < (ghostx[i] + 12)
                    && pacmanY > (ghosty[i] - 12) && pacmanY < (ghosty[i] + 12)
                    && ingame) {

                dying = true;
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y) {

        g2d.drawImage(ghost, x, y, this);
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < screenSize; y += blockSize) {
            for (x = 0; x < screenSize; x += blockSize) {

                g2d.setColor(mazecolor);
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
                    g2d.setColor(dotcolor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }

                i++;
            }
        }
    }

    public void StartGame(){
    	this.ingame = true;
    }
    
    public void StopGame(){
    	this.ingame = false;
    }
    
    public void SetMovement(int dx, int dy) {
    	this.reqDimensionX = dx;
    	this.reqDimensionY = dy;
    }
    
    public void initGame() {

        pacsleft = 3;
        score = 0;
        initLevel();
        nrofghosts = 4;
        currentspeed = 3;
    }

    private void initLevel() {

        int i;
        for (i = 0; i < numberOfBlocks * numberOfBlocks; i++) {
            screenData[i] = leveldata[i];
        }

        continueLevel();
    }

    private void continueLevel() {

        short i;
        int dx = 1;
        int random;

        for (i = 0; i < nrofghosts; i++) {

            ghosty[i] = 4 * blockSize;
            ghostx[i] = 4 * blockSize;
            ghostdy[i] = 0;
            ghostdx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentspeed + 1));

            if (random > currentspeed) {
                random = currentspeed;
            }

            ghostspeed[i] = validspeeds[random];
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

        ghost = new ImageIcon("images/ghost.gif").getImage();
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
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);
        doAnim();

        if (ingame) {
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
