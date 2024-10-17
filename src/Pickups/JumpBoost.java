package Pickups;

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

    protected double weight;

    public JumpBoost(BufferedImage image, Point startLocation, TileType tileType, float scale, Rectangle bounds, String pickupName, double weight) {
        super(image, startLocation, tileType, scale, bounds, pickupName, weight);

        this.weight = weight;

        super.initialize(this);
    }

    @Override
    public void execute(Player player) {
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