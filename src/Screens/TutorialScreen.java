package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;

import java.awt.*;

// This is the class for the main menu screen
public class TutorialScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;

    protected SpriteFont moveLeft;
    protected SpriteFont moveRight;
    protected SpriteFont crouch;
    protected SpriteFont shoot;
    protected SpriteFont dash;
    protected SpriteFont doubleJump;
    protected SpriteFont powerUps;
    protected SpriteFont screenInverse;
    protected SpriteFont moneyExplaination;

    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();

    public TutorialScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        moveLeft = new SpriteFont("MOVE LEFT: A OR <-", 90, 100, "Arial", 12, new Color(49, 207, 240));
        moveLeft.setOutlineColor(Color.black);
        moveLeft.setOutlineThickness(3);

        moveRight = new SpriteFont("MOVE RIGHT: D OR ->", 90, 200, "Arial", 12, new Color(49, 207, 240));
        moveRight.setOutlineColor(Color.black);
        moveRight.setOutlineThickness(3);

        crouch = new SpriteFont("CROUCH: S OR V", 90, 300, "Arial", 12, new Color(49, 207, 240));
        crouch.setOutlineColor(Color.black);
        crouch.setOutlineThickness(3);

        shoot = new SpriteFont("SHOOT: MOUSE CLICK", 90, 400, "Arial", 12, new Color(49, 207, 240));
        shoot.setOutlineColor(Color.black);
        shoot.setOutlineThickness(3);

        dash = new SpriteFont("PRESS SPACE TO DASH IN DIRECTION YOU ARE FACING", 410, 300, "Arial", 12, new Color(49, 207, 240));
        dash.setOutlineColor(Color.black);
        dash.setOutlineThickness(3);

        doubleJump = new SpriteFont("PRESS ^ OR W FOR A HIGHER JUMP", 475, 200, "Arial", 12, new Color(49, 207, 240));
        doubleJump.setOutlineColor(Color.black);
        doubleJump.setOutlineThickness(3);

        powerUps = new SpriteFont("POWERUPS LAST 5 SECONDS", 490, 100, "Arial", 12, new Color(49, 207, 240));
        powerUps.setOutlineColor(Color.black);
        powerUps.setOutlineThickness(3);

        moneyExplaination = new SpriteFont("COLLECTED COINS GET ADDED TO SCORE, 1 COIN COUNTS FOR 2", 375, 400, "Arial", 12, new Color(49, 207, 240));
        moneyExplaination.setOutlineColor(Color.black);
        moneyExplaination.setOutlineThickness(3);

        screenInverse = new SpriteFont("YOU CAN GO THROUGH THE SIDE OF THE SCREEN AND COME OUT THE OTHER", 150, 500, "Arial", 12, new Color(49, 207, 240));
        screenInverse.setOutlineColor(Color.black);
        screenInverse.setOutlineThickness(3);

        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        background.update(null);

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        moveLeft.draw(graphicsHandler);
        moveRight.draw(graphicsHandler);
        crouch.draw(graphicsHandler);
        shoot.draw(graphicsHandler);
        dash.draw(graphicsHandler);
        doubleJump.draw(graphicsHandler);
        powerUps.draw(graphicsHandler);
        screenInverse.draw(graphicsHandler);
        moneyExplaination.draw(graphicsHandler);
    }
}
