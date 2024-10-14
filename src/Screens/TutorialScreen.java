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

    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();

    public TutorialScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        moveLeft = new SpriteFont("MOVE LEFT: A OR <-", 200, 100, "Arial", 30, new Color(49, 207, 240));
        moveLeft.setOutlineColor(Color.black);
        moveLeft.setOutlineThickness(3);

        moveRight = new SpriteFont("MOVE RIGHT: D OR ->", 200, 200, "Arial", 30, new Color(49, 207, 240));
        moveRight.setOutlineColor(Color.black);
        moveRight.setOutlineThickness(3);

        crouch = new SpriteFont("CROUCH: S OR V", 200, 300, "Arial", 30, new Color(49, 207, 240));
        crouch.setOutlineColor(Color.black);
        crouch.setOutlineThickness(3);

        shoot = new SpriteFont("SHOOT: MOUSE CLICK", 200, 400, "Arial", 30, new Color(49, 207, 240));
        shoot.setOutlineColor(Color.black);
        shoot.setOutlineThickness(3);

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
    }
}
