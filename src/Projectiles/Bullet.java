package Projectiles;

import GameObject.SpriteSheet;
import Level.Projectile;
import Utils.Point;

public class Bullet extends Projectile {
    protected float projectileSpeed;
    protected float fireRate;

    public Bullet(Point point, float damage, float projectileSpeed, float fireRate, SpriteSheet spriteSheet, String startingAnimation) {
        super(damage, point.x, point.y, spriteSheet, startingAnimation);
        this.projectileSpeed = projectileSpeed;
        this.fireRate = fireRate;
    }

    
}
