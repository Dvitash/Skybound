package Level;

import GameObject.Frame;
import GameObject.SpriteSheet;

import java.util.HashMap;

// This class is a base class for all enemies in the game -- all enemies should extend from it
public class Enemy extends MapEntity {

    public Enemy(float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(x, y, spriteSheet, startingAnimation);
    }

    public Enemy(float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
    }

    public Enemy(float x, float y, Frame[] frames) {
        super(x, y, frames);
    }

    public Enemy(float x, float y, Frame frame) {
        super(x, y, frame);
    }

    public Enemy(float x, float y) {
        super(x, y);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void update(Player player) {
        super.update();
        
        if (intersects(player)) {    
            float playerBottomEdge = player.getY2(); // get the bottom edge of the player
            float enemyTopEdge = y; // get the top edge of the enemy

            System.out.println("Player bottom edge: " + playerBottomEdge + " Enemy top edge: " + enemyTopEdge + " Difference: " + Math.abs(playerBottomEdge - enemyTopEdge));
    
            if (playerBottomEdge >= enemyTopEdge && player.playerState == PlayerState.JUMPING) { // check if player is falling onto the enemy
                // force player to jump
                player.bounce();

                // remove the enemy from the map
                mapEntityStatus = MapEntityStatus.REMOVED;
            } else {
                touchedPlayer(player);
             
            } 
            
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
