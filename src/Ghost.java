import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Ghost extends JPanel {

	private static final long serialVersionUID = 1L;
	//Current position X
	public int ghostX;
	//Current position Y
	public int ghostY;
	public int ghostDX;
	public int ghostDY;
	private int speed;
	private final int validSpeeds[] = {1, 2, 3, 4, 5, 6, 8 };
	
	public Ghost(int initialPositionX, int initialPositionY, int level)  {
		
		ghostX = initialPositionX;
		ghostY = initialPositionY;
		
		// ghost speed according to level
		if (level > validSpeeds.length) {
			speed = validSpeeds[validSpeeds.length - 1];
		}
		else {
			speed = validSpeeds[level];	
		}
	}

    public void move(short screenData[], int blockSize, Graphics2D g2d, int pacmanX, int pacmanY) {

        int position;
        // Width and height of the game field
        int numberOfBlocks = 15;
        int myX = ghostX / blockSize;
        int myY = ghostY / blockSize;
        PossibleMoves moves = new PossibleMoves(myX, myY, pacmanX, pacmanY);

        if (ghostX % blockSize == 0 && ghostY % blockSize == 0) {
            position = myX + numberOfBlocks * myY;

            if ((screenData[position] & ScreenDataConstants.leftBorder) == 0)
                moves.addMove(-1, 0);

            if ((screenData[position] & ScreenDataConstants.topBorder) == 0)
                moves.addMove(0, -1);

            if ((screenData[position] & ScreenDataConstants.rightBorder) == 0)
                moves.addMove(1, 0);

            if ((screenData[position] & ScreenDataConstants.bottomBorder) == 0)
                moves.addMove(0, 1);

            PossibleMoves.Move move = moves.chooseMove();
            ghostDX = move.getDX();
            ghostDY = move.getDY();
        }

		ghostX = ghostX + (ghostDX * speed);
		ghostY = ghostY + (ghostDY * speed);

		drawGhost(g2d, ghostX + 1, ghostY + 1);
    }

	private void drawGhost(Graphics2D g2d, int x, int y) {

		Image ghostImage = new ImageIcon("images/ghost.gif").getImage();
		g2d.drawImage(ghostImage, x, y, this);
	}

    private static class PossibleMoves
    {
        public PossibleMoves(int ghostX, int ghostY, int pacmanX, int pacmanY)
        {
            this.ghostX = ghostX;
            this.ghostY = ghostY;
            this.pacmanX = pacmanX;
            this.pacmanY = pacmanY;
        }

        public void addMove(int dx, int dy)
        {
            if (this.isPreferred(dx, dy))
                preferred.add(new Move(dx, dy));
            else
                notPreferred.add(new Move(dx, dy));
        }

        public Move chooseMove()
        {
            if (!preferred.isEmpty())
                return preferred.get(ThreadLocalRandom.current().nextInt(preferred.size()));
            else
                return notPreferred.get((ThreadLocalRandom.current().nextInt(notPreferred.size())));
        }

        private boolean isPreferred(int dx, int dy)
        {
            boolean xOK = (pacmanX-ghostX)*dx >= 0;
            boolean yOK = (pacmanY-ghostY)*dy >= 0;
            return xOK && yOK;
        }

        public static class Move
        {
            public Move(int dx, int dy) {
                this.dx = dx;
                this.dy = dy;
            }

            public int getDX() {
                return this.dx;
            }

            public int getDY() {
                return this.dy;
            }

            int dx;
            int dy;
        }

        private List<Move> preferred = new ArrayList<Move>();
        private List<Move> notPreferred = new ArrayList<Move>();

        private int ghostX;
        private int ghostY;
        private int pacmanX;
        private int pacmanY;

    }
}
