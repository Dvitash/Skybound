package Level;

import GameObject.Frame;
import GameObject.SpriteSheet;
import Projectiles.Bullet;
import Utils.Point;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import Engine.ImageLoader;
import Engine.Mouse;

import java.util.HashMap;

// This class is a base class for all enemies in the game -- all enemies should extend from it
public class Enemy extends MapEntity {

    protected boolean doesShoot = false;
    protected boolean shootingCooldown = false;

    protected Random random = new Random();

    public Enemy(float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(x, y, spriteSheet, startingAnimation);
    }

    public Enemy(float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
    }

    public Enemy(float x, float y, Frame[] frames) {
        super(x, y, frames);
    }

    public Enemy(float x, float y, Frame frame, boolean doesShoot) {
        super(x, y, frame);

        this.doesShoot = doesShoot;
    }

    public Enemy(float x, float y) {
        super(x, y);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void update(Player player) {
        // super.update();
        
        if (intersects(player)) {    
            float playerBottomEdge = player.getY2(); // get the bottom edge of the player
            float enemyTopEdge = y; // get the top edge of the enemy
    
            if (playerBottomEdge >= enemyTopEdge && player.playerState == PlayerState.JUMPING) { // check if player is falling onto the enemy
                // force player to jump
                player.bounce();

                

                touchedEnemy(null);

                // remove the enemy from the map
                mapEntityStatus = MapEntityStatus.REMOVED;

                
            } else {
                touchedPlayer(player);
                
            }
        }

        // timer to shoot a projectile at the player
        if (doesShoot && !shootingCooldown) {
            shootingCooldown = true;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    shootingCooldown = false;
                }
            }, (long) (3000 + random.nextInt(3000)));

            // shoot
            int bulletY = Math.round(getY() + (getHeight() / 2));
            int bulletX = Math.round(getX() + (getWidth() / 2));

            int screenY = Math.round(getCalibratedYLocation() + (getHeight() / 2));
            System.out.println(screenY);
            Point playerPosition = new Point(player.getCalibratedXLocation(), player.getCalibratedYLocation());

            Point movementVector = new Point(playerPosition.x - bulletX, playerPosition.y - screenY).toUnit();

            float projectileSpeed = random.nextFloat(5f, 10f);

            Bullet bullet = new Bullet(new Point(bulletX, bulletY), 1f, projectileSpeed, 60f,
            movementVector, new SpriteSheet(ImageLoader.load("Bullet.png"), 7, 7), "DEFAULT", true);

            map.addProjectile(bullet);
        }
    }

    // A subclass can override this method to specify what it does when it touches the player
    public void touchedPlayer(Player player) {
        player.hurtPlayer(this);
    }

    public void touchedEnemy(Enemy enemy){
        mapEntityStatus = MapEntityStatus.REMOVED;
        
    }
    
}
