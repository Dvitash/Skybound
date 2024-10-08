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
    protected boolean isEnemy;
    protected Point movementVector;

    public Projectile(float damage, float x, float y, Point movementVector, SpriteSheet spriteSheet, String startingAnimation, boolean isEnemy) {
        super(x, y, spriteSheet, startingAnimation);
        this.movementVector = movementVector;
        this.isEnemy = isEnemy;
        this.damage = damage;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void update() {

        if (this.isEnemy) {
            // if (intersects(player)) {
            //     enemy.setMapEntityStatus(MapEntityStatus.REMOVED);
            //     map.removeProjectile(this);
            // }
        } else {
            for (Enemy enemy : map.getActiveEnemies()) {
                if (intersects(enemy)) {
                    enemy.setMapEntityStatus(MapEntityStatus.REMOVED);
                    map.removeProjectile(this);
                }
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