package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Sprite;
import Level.Map;
import Level.MapTile;
import Maps.TitleScreenMap;
import Players.Skyler;
import SpriteFont.SpriteFont;
import Tilesets.CommonTileset;
import Utils.Colors;

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
public class SkinsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont red;
    protected SpriteFont yellow;
    protected SpriteFont green;
    protected SpriteFont pink;
    protected BufferedImage background;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();
    protected Sprite skyler_red;
    protected Sprite skyler_green;
    protected Sprite skyler_yellow;
    protected Sprite skyler_turq;
    protected static String color = "Red";


    public SkinsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        
        red = new SpriteFont("Red", 200, 123, "Arial", 30, new Color(49, 207, 240));
        red.setOutlineColor(Color.black);
        red.setOutlineThickness(3);
        skyler_red = new Sprite(ImageLoader.loadSubImage("Skyler_Red.png", Colors.MAGENTA, 0, 0, 16, 16));
        skyler_red.setScale(5);
        skyler_red.setLocation(400, 100);

        green = new SpriteFont("Green", 200, 223, "Arial", 30, new Color(49, 207, 240));
        green.setOutlineColor(Color.black);
        green.setOutlineThickness(3);
        skyler_green = new Sprite(ImageLoader.loadSubImage("Skyler_Green.png", Colors.MAGENTA, 0, 0, 16, 16));
        skyler_green.setScale(5);
        skyler_green.setLocation(400, 200);

        yellow = new SpriteFont("Yellow", 200, 323, "Arial", 30, new Color(49, 207, 240));
        yellow.setOutlineColor(Color.black);
        yellow.setOutlineThickness(3);
        skyler_yellow = new Sprite(ImageLoader.loadSubImage("Skyler_Yellow.png", Colors.MAGENTA, 0, 0, 16, 16));
        skyler_yellow.setScale(5);
        skyler_yellow.setLocation(400, 300);

        pink = new SpriteFont("Turquoise", 200, 423, "Arial", 30, new Color(49, 207, 240));
        pink.setOutlineColor(Color.black);
        pink.setOutlineThickness(3);
        skyler_turq = new Sprite(ImageLoader.loadSubImage("Skyler_Turq.png", Colors.MAGENTA, 0, 0, 16, 16));
        skyler_turq.setScale(5);
        skyler_turq.setLocation(400, 400);


        background = ImageLoader.load("Skybound_Title.jpg");
        
        
        keyPressTimer = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);
    }

    public static String getColor(){
        return color;
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
        // File music = new File("C:/Users/zakar/OneDrive/Desktop/SER225/Skybound/Sound/music.WAV");
        // update background map (to play tile animations)
        //background.update(null);

        if (Keyboard.isKeyDown(Key.ESC)){
            screenCoordinator.setGameState(GameState.MENU);
            playWav(soundFile);
        }

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
            red.setColor(new Color(255, 215, 0));
            green.setColor(new Color(49, 207, 240));
            yellow.setColor(new Color(49, 207, 240));
            pink.setColor(new Color(49, 207, 240));
            pointerLocationX = 170;
            pointerLocationY = 130;
        } else if (currentMenuItemHovered == 1) {
            red.setColor(new Color(49, 207, 240));
            green.setColor(new Color(255, 215, 0));
            yellow.setColor(new Color(49, 207, 240));
            pink.setColor(new Color(49, 207, 240));
            pointerLocationX = 170;
            pointerLocationY = 230;
        } else if (currentMenuItemHovered == 2) {
            red.setColor(new Color(49, 207, 240));
            green.setColor(new Color(49, 207, 240));
            yellow.setColor(new Color(255, 215, 0));
            pink.setColor(new Color(49, 207, 240));
            pointerLocationX = 170;
            pointerLocationY = 330;
        } else if (currentMenuItemHovered == 3){
            red.setColor(new Color(49, 207, 240));
            green.setColor(new Color(49, 207, 240));
            yellow.setColor(new Color(49, 207, 240));
            pink.setColor(new Color(255, 215, 0));
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
                color = "Red";
                screenCoordinator.setGameState(GameState.MENU);
                playWav(soundFile);
                // playWav(music);
            } else if (menuItemSelected == 1) {
                color = "Green";
                screenCoordinator.setGameState(GameState.MENU);
                playWav(soundFile);
            } else if (menuItemSelected == 2) {
                color = "Yellow";
                screenCoordinator.setGameState(GameState.MENU);
                playWav(soundFile);
            } else if (menuItemSelected == 3) {
                color = "Turq";
                screenCoordinator.setGameState(GameState.MENU);
                playWav(soundFile);
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        //background.draw(graphicsHandler);
        graphicsHandler.drawImage(background, 0, 0, 800, 700);
        red.draw(graphicsHandler);
        green.draw(graphicsHandler);
        yellow.draw(graphicsHandler);
        pink.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
        skyler_red.draw(graphicsHandler);
        skyler_green.draw(graphicsHandler);
        skyler_yellow.draw(graphicsHandler);
        skyler_turq.draw(graphicsHandler);
    }
}
