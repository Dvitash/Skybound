package EnhancedMapTiles;

import Engine.GraphicsHandler;
import GameObject.Rectangle;
import Level.Pickup;
import Level.Player;
import Level.TileType;
import Utils.Point;

import java.awt.image.BufferedImage;

public class JumpBoost extends Pickup {
    private boolean isCollected = false;

    protected static final long jumpBoostDuration = 5000;
    protected static final float jumpBoostModifier = 1.5f;

    public JumpBoost(BufferedImage image, Point startLocation, TileType tileType, float scale, Rectangle bounds, String pickupName) {
        super(image, startLocation, tileType, scale, bounds, pickupName);
        this.initialize();
    }

    @Override
    protected void execute(Player player) {
        super.execute(player);

        float originalJumpHeight = player.jumpHeight;
        player.jumpHeight = originalJumpHeight * jumpBoostModifier;

        Pickup.SetActive(this);
        JumpBoost currentInstance = this;

        // after the duration, set it back to normal
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        player.jumpHeight = originalJumpHeight;
                        Pickup.SetInactive(currentInstance);
                    }
                },
                jumpBoostDuration
        );
    }
}