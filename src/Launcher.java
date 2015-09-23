import graphics.Play;
import graphics.Sounds;


public class Launcher {
	
	static Play music;
	public static void main(String[] args) {
        
		Game pacman = new Game();
        pacman.run();
		Sounds.playAndRepeat(("src/data/sounds/music.mp3"), 25000);
    }
}
