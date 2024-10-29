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
    protected double weight = 0;
    protected boolean shootingCooldown = false;
    protected static ArrayList<Pickup> activePickups = new ArrayList<Pickup>();

    protected static Random random = new Random();

    protected static ArrayList<Pickup> pickups = new ArrayList<>();

    protected BufferedImage image;
    protected Point startLocation;
    protected TileType tileType;
    protected float scale;
    protected Rectangle bounds;

    public Pickup(BufferedImage image, Point startLocation, TileType tileType, float scale, Rectangle bounds,
            String pickupName, double weight) {
        super(startLocation.x, startLocation.y, new FrameBuilder(image).withBounds(bounds).withScale(scale).build(),
                tileType);

        this.pickupName = pickupName;
        this.weight = weight;

        this.image = image;
        this.startLocation = startLocation;
        this.tileType = tileType;
        this.scale = scale;
        this.bounds = bounds;

        this.initialize(this);
    }

    public Pickup clone(Point newLocation) {
        try {
            // Use reflection to dynamically create a new instance of the same subclass
            return this.getClass()
                       .getDeclaredConstructor(BufferedImage.class, Point.class, TileType.class, float.class, Rectangle.class, String.class, double.class)
                       .newInstance(this.image, newLocation, this.tileType, this.scale, this.bounds, this.pickupName, this.weight);
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null if the cloning fails
        }
    }
    

    public static Pickup getRandomPickup(Point location) {
        double totalWeight = 0;
        for (Pickup pickup : pickups) {
            totalWeight += pickup.weight;
        }  

        double randomValue = random.nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (Pickup pickup : pickups) {
            cumulativeWeight += pickup.weight;
            if (randomValue <= cumulativeWeight) {
                Pickup newPickup = pickup.clone(location);
                return newPickup;
            }
        }

        return null;
    }

    public void execute(Player player) { }

    public void initialize(Pickup pickupToAdd) {
        pickups.add(pickupToAdd);
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
