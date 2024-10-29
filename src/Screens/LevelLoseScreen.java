package Screens;

import java.io.File;
import java.util.Scanner;
import Engine.*;
import SpriteFont.SpriteFont;
import java.awt.image.BufferedImage;
import java.awt.*;

// This is the class for the level lose screen
public class LevelLoseScreen extends Screen {
    protected BufferedImage background;
    protected SpriteFont score;
    protected SpriteFont highScoreText;
    protected SpriteFont loseMessage;
    protected SpriteFont instructions;
    protected SpriteFont instructions2;
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;

    public LevelLoseScreen(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
        initialize();
    }

    @Override
    public void initialize() {
        loseMessage = new SpriteFont("You lose!", 340, 219, "Arial", 30, Color.black);
        score = new SpriteFont("Score: ", 350, 259, "Arial", 30,Color.black);
        highScoreText = new SpriteFont("HighScore: ", 310, 299, "Arial", 30, Color.black);
        instructions = new SpriteFont("Press Space to try again", 310, 329,"Arial", 20, Color.black);
        instructions2 = new SpriteFont("Escape to go back to the main menu", 240, 359, "Arial", 20, Color.black);
        keyLocker.lockKey(Key.SPACE);
        keyLocker.lockKey(Key.ESC);
        background = ImageLoader.load("LoseScreen.jpg");
    }

    public void update(int playerScore) {
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        score.setText("Score: " + playerScore);

        try {
            // check if the file exists first
            File file = new File("GameSaves\\scoresaves.txt");
            if (file.exists()) {
                Scanner scan = new Scanner(file);
                int highScore = scan.nextInt();
                highScoreText.setText("Highscore: " + highScore);
                scan.close();
            } else {
                highScoreText.setText("Highscore: 0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if space is pressed, reset level. if escape is pressed, go back to main menu
        if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)) {
            playLevelScreen.resetLevel();
        } else if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            playLevelScreen.goBackToMenu();
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawImage(background, 0, -25, 800, 755);
        //graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        loseMessage.draw(graphicsHandler);
        score.draw(graphicsHandler);
        highScoreText.draw(graphicsHandler);
        instructions.draw(graphicsHandler);
        instructions2.draw(graphicsHandler);
    }

    @Override
    public void update() {
    }
}
