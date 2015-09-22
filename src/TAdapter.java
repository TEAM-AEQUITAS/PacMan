import java.awt.Event;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

public class TAdapter extends KeyAdapter {

	public boolean isPlaying;
	private Board board;

	public TAdapter(Board board) {
		this.board = board;
	}

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if (isPlaying) {
			if (key == KeyEvent.VK_LEFT) {
				board.SetMovement(-1, 0);

			} else if (key == KeyEvent.VK_RIGHT) {
				board.SetMovement(1, 0);

			} else if (key == KeyEvent.VK_UP) {
				board.SetMovement(0, -1);

			} else if (key == KeyEvent.VK_DOWN) {
				board.SetMovement(0, 1);

			} else if (key == KeyEvent.VK_ESCAPE && board.timer.isRunning()) {
				isPlaying = false;

			} else if (key == KeyEvent.VK_PAUSE) {
				if (board.timer.isRunning()) {
					board.timer.stop();
				} else {
					board.timer.start();
				}
			}
		} else {
			if (key == KeyEvent.VK_ENTER) {
				isPlaying = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == Event.LEFT || key == Event.RIGHT || key == Event.UP
				|| key == Event.DOWN) {
			board.SetMovement(0, 0);
		}
	}
}