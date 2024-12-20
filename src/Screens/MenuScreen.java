package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.image.BufferedImage;

import java.awt.*;

// This is the class for the main menu screen
public class MenuScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont playGame;
    protected SpriteFont credits;
    protected SpriteFont tutorial;
    protected SpriteFont skins;
    protected BufferedImage background;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    public MenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        playGame = new SpriteFont("PLAY GAME", 200, 123, "Arial", 30, new Color(49, 207, 240));
        playGame.setOutlineColor(Color.black);
        playGame.setOutlineThickness(3);

        tutorial = new SpriteFont("TUTORIAL", 200, 223, "Arial", 30, new Color(49, 207, 240));
        tutorial.setOutlineColor(Color.black);
        tutorial.setOutlineThickness(3);

        credits = new SpriteFont("CREDITS", 200, 323, "Arial", 30, new Color(49, 207, 240));
        credits.setOutlineColor(Color.black);
        credits.setOutlineThickness(3);

        skins = new SpriteFont("SKINS", 200, 423, "Arial", 30, new Color(49, 207, 240));
        skins.setOutlineColor(Color.black);
        skins.setOutlineThickness(3);


        background = ImageLoader.load("Skybound_Title.jpg");
        keyPressTimer = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);
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


    public void update() {
        File soundFile = new File("Sound/select.WAV");
        // File music = new File("Sound/music.WAV");

        // update background map (to play tile animations)
        //background.update(null);

        // if down or up is pressed, change menu item "hovered" over (blue square in front of text will move along with currentMenuItemHovered changing)
        if (Keyboard.isKeyDown(Key.DOWN) &&  keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered++;
            playWav(soundFile);
        } else if (Keyboard.isKeyDown(Key.UP) &&  keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered--;
            playWav(soundFile);
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // if down is pressed on last menu item or up is pressed on first menu item, "loop" the selection back around to the beginning/end
        if (currentMenuItemHovered > 3) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 3;
        }

        // sets location for blue square in front of text (pointerLocation) and also sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHovered == 0) {
            playGame.setColor(new Color(255, 215, 0));
            tutorial.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            skins.setColor(new Color(49, 207, 240));
            pointerLocationX = 170;
            pointerLocationY = 130;
        } else if (currentMenuItemHovered == 1) {
            playGame.setColor(new Color(49, 207, 240));
            tutorial.setColor(new Color(255, 215, 0));
            credits.setColor(new Color(49, 207, 240));
            skins.setColor(new Color(49, 207, 240));
            pointerLocationX = 170;
            pointerLocationY = 230;
        } else if (currentMenuItemHovered == 2) {
            playGame.setColor(new Color(49, 207, 240));
            tutorial.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(255, 215, 0));
            skins.setColor(new Color(49, 207, 240));
            pointerLocationX = 170;
            pointerLocationY = 330;
        } else if (currentMenuItemHovered == 3){
            playGame.setColor(new Color(49, 207, 240));
            tutorial.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            skins.setColor(new Color(255, 215, 0));
            pointerLocationX = 170;
            pointerLocationY = 430;
        }

        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            if (menuItemSelected == 0) {
                screenCoordinator.setGameState(GameState.LEVEL);
                playWav(soundFile);
                // playWav(music);
            } else if (menuItemSelected == 1) {
                screenCoordinator.setGameState(GameState.TUTORIAL);
                playWav(soundFile);
            } else if (menuItemSelected == 2) {
                screenCoordinator.setGameState(GameState.CREDITS);
                playWav(soundFile);
            } else if (menuItemSelected == 3) {
                screenCoordinator.setGameState(GameState.SKINS);
                playWav(soundFile);
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        //background.draw(graphicsHandler);
        graphicsHandler.drawImage(background, 0, 0, 800, 700);
        playGame.draw(graphicsHandler);
        tutorial.draw(graphicsHandler);
        credits.draw(graphicsHandler);
        skins.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
    }
}
