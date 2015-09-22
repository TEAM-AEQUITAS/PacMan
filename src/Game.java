import javax.swing.JFrame;

public class Game extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    public Game() {

    }

    private void initUI() {
        add(new Board());
        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(470, 510);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void run() {
        initUI();
    }
}
