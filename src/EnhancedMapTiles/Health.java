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

public class Health extends EnhancedMapTile {
    public Health(BufferedImage image, Point startLocation, TileType tileType, float scale, Rectangle bounds) {
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

    // Check for collision with the player
    if (intersects(player)) {
        // Removes the tile
        setMapEntityStatus(MapEntityStatus.REMOVED); 
        player.health();
    }
}

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

}
