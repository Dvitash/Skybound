package Projectiles;

import GameObject.SpriteSheet;
import Level.Player;
import Level.Projectile;
import Utils.Point;

public class Bullet extends Projectile {
    protected float projectileSpeed;
    protected float fireRate;

    public Bullet(Point point, float damage, float projectileSpeed, float fireRate, Point movementVector, SpriteSheet spriteSheet, String startingAnimation, boolean isEnemy) {
        super(damage, point.x, point.y, movementVector, spriteSheet, startingAnimation, isEnemy);
        this.projectileSpeed = projectileSpeed;
        this.fireRate = fireRate;
    }

    @Override
    public void update(Player player) {
        super.update(player);

        // do collision check
        this.moveYHandleCollision(this.movementVector.y * this.projectileSpeed);
        this.moveXHandleCollision(this.movementVector.x * this.projectileSpeed);
    }
}
