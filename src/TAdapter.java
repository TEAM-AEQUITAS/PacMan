import java.awt.Event;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

public class TAdapter extends KeyAdapter {

	private int reqdx, reqdy;
	public boolean isPlaying;	
	private Board board;
	
    public TAdapter(Board board) {
    	this.board = board;
    	reqdx = 0;
    	reqdy = 0;
    }
    
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (isPlaying) {
            if (key == KeyEvent.VK_LEFT) {
            	board.SetMovement(-1, 0);
                reqdx = -1;
                reqdy = 0;
                
            } else if (key == KeyEvent.VK_RIGHT) {
            	board.SetMovement(1, 0);
                reqdx = 1;
                reqdy = 0;
                
            } else if (key == KeyEvent.VK_UP) {
                reqdx = 0;
                reqdy = -1;
                board.SetMovement(0, -1);
                
            } else if (key == KeyEvent.VK_DOWN) {
            	board.SetMovement(0, 1);
               
            } else if (key == KeyEvent.VK_ESCAPE && board.timer.isRunning()) {
            	isPlaying = false;
            	
            } else if (key == KeyEvent.VK_PAUSE) {
                if (board.timer.isRunning()) {
                    board.timer.stop();
                } 
                else {
                    board.timer.start();
                }
            }
        } 
        else {
            if (key == KeyEvent.VK_ENTER) {
            	isPlaying = true;
            	board.StartGame();
                board.initGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == Event.LEFT || key == Event.RIGHT
                || key == Event.UP || key == Event.DOWN) {
            reqdx = 0;
            reqdy = 0;
        }
    }
}