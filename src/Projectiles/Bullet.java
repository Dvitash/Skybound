package Projectiles;

import GameObject.SpriteSheet;
import Level.Player;
import Level.Projectile;
import Utils.Point;

public class Bullet extends Projectile {
    protected float projectileSpeed;
    protected float fireRate;

    public Bullet(Point point, float damage, float projectileSpeed, float fireRate, Point movementVector, SpriteSheet spriteSheet, String startingAnimation) {
        super(damage, point.x, point.y, movementVector, spriteSheet, startingAnimation);
        this.projectileSpeed = projectileSpeed;
        this.fireRate = fireRate;
    }

    @Override
    public void update(Player player) {
        super.update();

        // do collision check
        this.moveYHandleCollision(this.movementVector.y * this.projectileSpeed);
        this.moveXHandleCollision(this.movementVector.x * this.projectileSpeed);
    }
}
