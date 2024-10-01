package Level;

import java.util.HashMap;

import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Utils.Direction;

public class Projectile extends MapEntity {

    protected float damage;

    public Projectile(float damage, float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(x, y, spriteSheet, startingAnimation);
        this.damage = damage;
    }

    public Projectile(float damage, float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
        this.damage = damage;
    }

    public Projectile(float damage, float x, float y, Frame[] frames) {
        super(x, y, frames);
        this.damage = damage;
    }

    public Projectile(float damage, float x, float y, Frame frame) {
        super(x, y, frame);
        this.damage = damage;
    }

    public Projectile(float damage, float x, float y) {
        super(x, y);
        this.damage = damage;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void update(Player player) {
        super.update();
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        if (entityCollidedWith instanceof Enemy) { // damage enemy here

        }
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        if (entityCollidedWith instanceof Enemy) { // damage enemy here
            
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}