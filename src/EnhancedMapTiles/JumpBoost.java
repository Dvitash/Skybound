package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import GameObject.Rectangle;
import Level.EnhancedMapTile;
import Level.MapEntityStatus;
import Level.Player;
import Level.TileType;
import Utils.Point;

import java.awt.image.BufferedImage;

public class JumpBoost extends EnhancedMapTile {
    private boolean isCollected = false;

    public JumpBoost(BufferedImage image, Point startLocation, TileType tileType, float scale, Rectangle bounds) {
        super(startLocation.x, startLocation.y, new FrameBuilder(image).withBounds(bounds).withScale(scale).build(), tileType);
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void update(Player player) {
        super.update(player);

        if (intersects(player) && !isCollected) {
            // Set the flag to true to indicate that the tile has been collected
            isCollected = true;
            player.jumpBoost();
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Only draw the tile if it hasn't been collected yet
        if (!isCollected) {
            super.draw(graphicsHandler);
        }
    }
}