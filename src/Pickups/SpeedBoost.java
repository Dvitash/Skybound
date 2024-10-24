package Pickups;

import Engine.GraphicsHandler;
import GameObject.Rectangle;
import Level.Pickup;
import Level.Player;
import Level.TileType;
import Utils.Point;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SpeedBoost extends Pickup {
    protected static final long Duration = 5000;
    protected static final float Modifier = 2f;

    protected double weight;

    public SpeedBoost(BufferedImage image, Point startLocation, TileType tileType, float scale, Rectangle bounds, String pickupName, double weight) {
        super(image, startLocation, tileType, scale, bounds, pickupName, weight);

        this.weight = weight;

        super.initialize(this);
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

    @Override
    public void execute(Player player) {
        File soundFile = new File("Sound\\powerUp.WAV");
        playWav(soundFile);
        float originalWalkSpeed = player.walkSpeed;
        player.walkSpeed = originalWalkSpeed * Modifier;

        Pickup.SetActive(this);
        SpeedBoost currentInstance = this;

        // after the duration, set it back to normal
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        player.walkSpeed = originalWalkSpeed;
                        Pickup.SetInactive(currentInstance);
                    }
                },
                Duration
        );
    }
}