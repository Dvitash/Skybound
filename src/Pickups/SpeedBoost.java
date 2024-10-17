package Pickups;

import Engine.GraphicsHandler;
import GameObject.Rectangle;
import Level.Pickup;
import Level.Player;
import Level.TileType;
import Utils.Point;

import java.awt.image.BufferedImage;

public class SpeedBoost extends Pickup {
    protected static final long Duration = 5000;
    protected static final float Modifier = 2f;

    protected double weight;

    public SpeedBoost(BufferedImage image, Point startLocation, TileType tileType, float scale, Rectangle bounds, String pickupName, double weight) {
        super(image, startLocation, tileType, scale, bounds, pickupName, weight);

        this.weight = weight;

        super.initialize(this);
    }

    @Override
    public void execute(Player player) {
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