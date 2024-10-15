package Level;

import GameObject.Rectangle;
import Utils.Point;

import java.util.ArrayList;
import java.util.Random;
import Builders.FrameBuilder;
import Engine.GraphicsHandler;

import java.awt.image.BufferedImage;

// This class is a base class for all enemies in the game -- all enemies should extend from it
public class Pickup extends EnhancedMapTile {

    protected String pickupName;
    protected boolean doesShoot = false;
    protected boolean shootingCooldown = false;
    protected static ArrayList<Pickup> activePickups = new ArrayList<Pickup>();

    protected Random random = new Random();

    public Pickup(BufferedImage image, Point startLocation, TileType tileType, float scale, Rectangle bounds, String pickupName) {
        super(startLocation.x, startLocation.y, new FrameBuilder(image).withBounds(bounds).withScale(scale).build(), tileType);
        this.initialize();
        
        this.pickupName = pickupName;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    protected void execute(Player player) { }

    public void update(Player player) {
        if (intersects(player)) {
            execute(player);
            setMapEntityStatus(MapEntityStatus.REMOVED);
        }
    }

    protected static void SetActive(Pickup activePickup) {
        activePickups.add(activePickup);
    }

    protected static void SetInactive(Pickup inactivePickup) {
        activePickups.remove(inactivePickup);
    }

    public static ArrayList<Pickup> GetActivePickups() {
        return activePickups;
    }

    public String getName() {
        return this.pickupName;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
