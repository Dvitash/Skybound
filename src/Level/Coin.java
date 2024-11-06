package Level;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.Rectangle;
import Utils.Point;
import java.util.Random;
import Engine.ImageLoader;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


// This class is a base class for all enemies in the game -- all enemies should extend from it
public class Coin extends MapEntity {

    protected int coinAmount = 0;
    private boolean collected = false;

    public Coin(Point location, int coinAmount, Map map) {
        super(location.x, location.y, new Frame(ImageLoader.load("Coin.png"), ImageEffect.NONE, 2.0f, new Rectangle(0, 0, 15, 15)));
        this.initialize();

        this.coinAmount = coinAmount;

        map.coins.add(this);
        this.setMap(map);
    }

    @Override
    public void initialize() {
        super.initialize();
    }


    // plays the audio file
    public static void playWav(File soundAudio) {
        try {
            // Use the File object directly without concatenation
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundAudio);
    
            Clip clip = AudioSystem.getClip();
    
            clip.open(audioStream);
            clip.start();
    
            System.out.println("Playing audio...");

    
        } catch (UnsupportedAudioFileException e) {
            System.out.println("The specified audio file format is not supported.");
        } catch (IOException e) {
            System.out.println("Error playing the audio file.");
        } catch (LineUnavailableException e) {
            System.out.println("Audio line is unavailable.");
        }
    }

    public void update(Player player) {
        if (intersects(player) && !collected) {    
            collected = true;

            player.money += this.coinAmount;
            setMapEntityStatus(MapEntityStatus.REMOVED);
            File soundFile = new File("Sound/coin.wav");
            playWav(soundFile);
        }
    }
}

