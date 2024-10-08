package Level;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Utils.Direction;
import Utils.Point;

public class Projectile extends MapEntity {

    protected float damage;
    protected Point movementVector;

    public Projectile(float damage, float x, float y, Point movementVector, SpriteSheet spriteSheet, String startingAnimation) {
        super(x, y, spriteSheet, startingAnimation);
        this.movementVector = movementVector;
        this.damage = damage;
    }

    public Projectile(float damage, float x, float y, Point movementVector, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
        this.movementVector = movementVector;
        this.damage = damage;
    }

    public Projectile(float damage, float x, float y, Point movementVector, Frame[] frames) {
        super(x, y, frames);
        this.movementVector = movementVector;
        this.damage = damage;
    }

    public Projectile(float damage, float x, float y, Point movementVector, Frame frame) {
        super(x, y, frame);
        this.movementVector = movementVector;
        this.damage = damage;
    }

    public Projectile(float damage, float x, float y, Point movementVector) {
        super(x, y);
        this.movementVector = movementVector;
        this.damage = damage;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void update(Player player) {
        super.update();

        for (Enemy enemy : map.getActiveEnemies()) {
            if (intersects(enemy)) {
                enemy.setMapEntityStatus(MapEntityStatus.REMOVED);
                map.removeProjectile(this);
            }
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        super.onEndCollisionCheckX(hasCollided, direction, entityCollidedWith);

        if (hasCollided && !(entityCollidedWith instanceof Enemy)) {
            map.removeProjectile(this);
        }
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        super.onEndCollisionCheckY(hasCollided, direction, entityCollidedWith);

        if (hasCollided && !(entityCollidedWith instanceof Enemy)) {
            map.removeProjectile(this);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build()
            });
        }};
    }
}