import java.awt.EventQueue;
import javax.swing.JFrame;

public class MainGame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainGame() {
        
        initUI();
    }
    
    private void initUI() {
        
        add(new Board());
        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(470, 510);
        setLocationRelativeTo(null);
        setVisible(true);        
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainGame ex = new MainGame();
                ex.setVisible(true);
            }
        });
    }
}
