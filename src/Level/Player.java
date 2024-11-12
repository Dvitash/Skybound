package Level;

import Engine.ImageLoader;
import Engine.Key;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import Engine.Mouse;
import Engine.KeyLocker;
import Engine.Keyboard;
import EnhancedMapTiles.Spring;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Projectiles.Bullet;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Player extends GameObject {
    // values that affect player movement
    // these should be set in a subclass
    public boolean isInvincible = false; // for invincibility
    public float walkSpeed = 0; // for speedBoost
    protected float gravity = 0;
    public float jumpHeight = 0; // for jumpHeight
    protected float jumpDegrade = 0;
    protected float terminalVelocityY = 0;
    protected float momentumYIncrease = 0;
    protected float dashDegrade = 0;
    protected boolean pressedBeforeLand = false;
    protected float dashCooldown = 1f;
    protected boolean crouch = false;

    protected int score = 0;

    protected int money = 0;
    protected int scoreBuffer;

    public boolean magnetActive = false;
    public boolean jetpackActive = false;

    protected float jetpackMax = 75;
    protected float jetpackMomentum = 0;
    protected float jetpackMomentumIncrease = 1.5f;

    // values used to handle player movement
    protected float jumpForce = 0;
    protected float momentumY = 0;
    protected float momentumX = 0;
    protected float moveAmountX, moveAmountY;
    protected float lastAmountMovedX, lastAmountMovedY;

    // values used to keep track of player's current state
    protected PlayerState playerState;
    protected PlayerState previousPlayerState;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;
    protected AirGroundState previousAirGroundState;
    protected LevelState levelState;

    // Variables for speed boost
    protected long speedBoostEndTime = 0;
    protected static final long speedBoostDuration = 5000;
    protected float speedBoost = 4.5f;
    protected boolean speedBoostActive;

    // Variables for health
    protected int hearts = 3;
    private boolean isHit = false;
    private long hitTimer = 0;
    private static final long hitCooldown = 1000; 

    // classes that listen to player events can be added to this list
    protected ArrayList<PlayerListener> listeners = new ArrayList<>();

    // define keys
    protected KeyLocker keyLocker = new KeyLocker();
    protected Key JUMP_KEY = Key.UP;
    protected Key JUMP_KEY2 = Key.W;
    protected Key MOVE_LEFT_KEY = Key.LEFT;
    protected Key MOVE_LEFT_KEY2 =Key.A;
    protected Key MOVE_RIGHT_KEY = Key.RIGHT;
    protected Key MOVE_RIGHT_KEY2 = Key.D;
    protected Key CROUCH_KEY = Key.DOWN;
    protected Key CROUCH_KEY2 = Key.S;
    protected Key SPACE = Key.SPACE;
    


     

    public Player(SpriteSheet spriteSheet, float x, float y, String startingAnimationName) {
        super(spriteSheet, x, y, startingAnimationName);
        facingDirection = Direction.RIGHT;
        airGroundState = AirGroundState.AIR;
        previousAirGroundState = airGroundState;
        playerState = PlayerState.STANDING;
        previousPlayerState = playerState;
        levelState = LevelState.RUNNING;

    }

    public int getMoney(){
        return money;
    }

    private void updateMoney(){
        if (score % 100 == 0 && score > scoreBuffer){
            money += 10;
            scoreBuffer = score + 1;
        }
    }
    

    private void SaveScore() {
        System.out.println("Score: " + score);
        try {
            File scoreFile = new File("GameSaves/scoresaves.txt");
            scoreFile.getParentFile().mkdirs();

            int oldScore = 0;
            if (scoreFile.exists()) {
                Scanner scanner = new Scanner(scoreFile);
                if (scanner.hasNextInt()) {
                    oldScore = scanner.nextInt();
                }
                scanner.close();
            }

            // only write new score if it is greater than the old score
            if (score > oldScore) {
                FileWriter writer = new FileWriter(scoreFile);
                writer.write(Integer.toString(score));
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void update() {
        updateMoney();
        moveAmountX = 0;
        moveAmountY = 0;

        // if player is currently playing through level (has not won or lost)
        if (levelState == LevelState.RUNNING) {
            applyGravity(false); 

            playerShoot();
            Dash();

            if (speedBoostEndTime > System.currentTimeMillis()){
                walkSpeed = speedBoost;
                speedBoostActive = true;
            }else{
                walkSpeed = 2.5f;
                speedBoostActive = false;
            }

            playerJumping(1f);
            // update player's state and current actions, which includes things like determining how much it should move each frame and if its walking or jumping
            do {
                previousPlayerState = playerState;
                handlePlayerState();
            } while (previousPlayerState != playerState);

            previousAirGroundState = airGroundState;

            // move player with respect to map collisions based on how much player needs to move this frame
            if (!jetpackActive) {
                lastAmountMovedX = super.moveXHandleCollision(moveAmountX);
                lastAmountMovedY = super.moveYHandleCollision(moveAmountY);

                if (jetpackMomentum > 0) {
                    jetpackMomentum = Math.max(jetpackMomentum - (jetpackMomentumIncrease * 2), 0);
                    moveUp(jetpackMomentum);
                }
            } else {
                lastAmountMovedX = 0;
                lastAmountMovedY = 0;

                jetpackMomentum = Math.min(jetpackMomentum + jetpackMomentumIncrease, jetpackMax);
                
                moveRight(moveAmountX);
                moveUp(jetpackMomentum);
            }

            handlePlayerAnimation();

            updateLockedKeys();

            score = Math.round(map.GetTotalMovement() / 20);

            if (getY() > map.getEndBoundY()) {
                levelState = LevelState.PLAYER_DEAD;
                SaveScore();
            }

            if (magnetActive) {
                Point playerPos = new Point((getX() + getX2()) / 2, (getY() + getY2()) / 2);
                for (Coin coin : map.coins) {
                    Point differenceVector = new Point(playerPos.x - ((coin.getX() + coin.getX2()) / 2), playerPos.y - ((coin.getY() + coin.getY2()) / 2));
                    Point unitVector = differenceVector.toUnit();

                    if (differenceVector.magnitude() > 500) { continue; }

                    coin.moveDown(unitVector.y * 3);
                    coin.moveRight(unitVector.x * 3);
                }
            }

            // update player's animation
            super.update();
        }

        // if player has beaten level
        else if (levelState == LevelState.LEVEL_COMPLETED) {
            updateLevelCompleted();
        }

        // if player has lost level
        else if (levelState == LevelState.PLAYER_DEAD) {
            updatePlayerDead();
        }

        if (isHit) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - hitTimer >= hitCooldown) {
                isHit = false; // Reset the hit flag after cooldown
            }
        }
    }

    private static boolean dashing = false;
    private static final KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // when key is pressed, set its keyDown state to true and its keyUp state to false
            int keyCode = e.getKeyCode();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // when key is released, set its keyDown state to false and its keyUp state to true
            int keyCode = e.getKeyCode();

            if (keyCode == 32) { // check for space key released
                dashing = false;
            }
        }
    };

    public int getScore() {
        return this.score;
    }

    // add gravity to player, which is a downward force
    protected void applyGravity(boolean inicrouch) {
        if ((inicrouch == true) && crouch == true) {
            moveAmountY += (gravity + momentumY * 0.5);
            crouch = false;
        } else {
            moveAmountY += gravity + momentumY;
        }

    }

    // based on player's current state, call appropriate player state handling method
    protected void handlePlayerState() {
        switch (playerState) {
            case STANDING:
                playerStanding();
                break;
            case WALKING:
                playerWalking();
                break;
            case CROUCHING:
                playerCrouching();
                break;
            case JUMPING:
                playerJumping(1f);
                break;
        }
    }

    // player STANDING state logic
    protected void playerStanding() {
        // if walk left or walk right key is pressed, player enters WALKING state
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY)) {
            playerState = PlayerState.WALKING;
        }

        else if(Keyboard.isKeyDown(MOVE_LEFT_KEY2) || Keyboard.isKeyDown(MOVE_RIGHT_KEY2)){
            playerState = PlayerState.WALKING;
        }

        // if jump key is pressed, player enters JUMPING state
        else if ((Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)) || (Keyboard.isKeyDown(JUMP_KEY2) && !keyLocker.isKeyLocked(JUMP_KEY2))) {
            keyLocker.lockKey(JUMP_KEY);
            keyLocker.lockKey(JUMP_KEY2);
            playerState = PlayerState.JUMPING;
        }

        // if crouch key is pressed, player enters CROUCHING state
        else if (Keyboard.isKeyDown(CROUCH_KEY) || Keyboard.isKeyDown(CROUCH_KEY2)) {
            playerState = PlayerState.CROUCHING;
        }
    }

    private boolean debounceStarted = false;
    private boolean dashDebounce = false;

    protected void Dash() {
        if (Keyboard.isKeyDown(SPACE) && !dashing && !dashDebounce) {
            dashDebounce = true;
            dashing = true;
            File soundFile = new File("Sound/dash.WAV");

            if ((Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_LEFT_KEY2)) && (Keyboard.isKeyUp(MOVE_RIGHT_KEY) || Keyboard.isKeyUp(MOVE_RIGHT_KEY2))) {
                momentumX = -15f;
                playerJumping(3f);
            } else if ((Keyboard.isKeyDown(MOVE_RIGHT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY2)) && (Keyboard.isKeyUp(MOVE_LEFT_KEY) || Keyboard.isKeyUp(MOVE_LEFT_KEY2))) {
                momentumX = 15f;
                playerJumping(3f);
            }
            playWav(soundFile);
        }

        if (Keyboard.isKeyUp(SPACE)) {
            if (dashing && dashDebounce && !debounceStarted) {
                debounceStarted = true;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        debounceStarted = false;
                        dashDebounce = false;
                    }
                }, (long) (dashCooldown * 1000));
            }

            dashing = false;
        }

        // apply dash momentum
        if (momentumX != 0) {
            if (momentumX < 0) { // moving left
                momentumX += dashDegrade;

                if (momentumX > 0) {
                    momentumX = 0;
                }

            } else { // moving right
                momentumX -= dashDegrade;

                if (momentumX < 0) {
                    momentumX = 0;
                }
            }

            moveAmountX += momentumX;
        }
    }

    private boolean shooting = false;
    protected void playerShoot() {
        if (Mouse.isMouseClicked() && !shooting) {
            shooting = true;

            int bulletY = Math.round(getY() + (getHeight() / 2));
            int bulletX = Math.round(getX() + (getWidth() / 2));

            int screenY = Math.round(getCalibratedYLocation() + (getHeight() / 2));

            Point mousePoint = Mouse.getCursorPoint();
            Point movementVector = new Point(mousePoint.x - bulletX, mousePoint.y - screenY).toUnit();

            Bullet bullet = new Bullet(new Point(bulletX, bulletY), 1f, 7.5f, 60f,
            movementVector, new SpriteSheet(ImageLoader.load("Bullet.png"), 7, 7), "DEFAULT", false);

            map.addProjectile(bullet);
            File soundFile = new File("Sound/shoot.WAV");
            playWav(soundFile); 
        }

        if (!Mouse.isMouseClicked()) {
            shooting = false;
        }
    }

    // player WALKING state logic
    protected void playerWalking() {
        // if walk left key is pressed, move player to the left
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_LEFT_KEY2)) {
            moveAmountX -= walkSpeed;
            facingDirection = Direction.LEFT;
        }

        // if walk right key is pressed, move player to the right
        else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY2)) {
            moveAmountX += walkSpeed;
            facingDirection = Direction.RIGHT;
        } else if ((Keyboard.isKeyUp(MOVE_LEFT_KEY) || Keyboard.isKeyUp(MOVE_LEFT_KEY2)) && (Keyboard.isKeyUp(MOVE_RIGHT_KEY) || Keyboard.isKeyUp(MOVE_RIGHT_KEY2))) {
            playerState = PlayerState.STANDING;
        }

        // if jump key is pressed, player enters JUMPING state
        if ((Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)) || (Keyboard.isKeyDown(JUMP_KEY2) && !keyLocker.isKeyLocked(JUMP_KEY2))) {
            playerState = PlayerState.JUMPING;
        }

        // if crouch key is pressed,
        else if (Keyboard.isKeyDown(CROUCH_KEY) || Keyboard.isKeyDown(CROUCH_KEY2)) {
            playerState = PlayerState.CROUCHING;
        }
    }

    // player CROUCHING state logic
    protected void playerCrouching() {
        // if crouch key is released, player enters STANDING state
        if (Keyboard.isKeyUp(CROUCH_KEY) && Keyboard.isKeyUp(CROUCH_KEY2)) {
            playerState = PlayerState.STANDING;
        }
    }


    // plays the audio file
    public static void playWav(File soundAudio) {
        try {
            // Use the File object directly without concatenation
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundAudio);
    
            Clip clip = AudioSystem.getClip();
    
            clip.open(audioStream);
            clip.start();
    
            System.out.println("Playing audio...");

    
        } catch (UnsupportedAudioFileException e) {
            System.out.println("The specified audio file format is not supported.");
        } catch (IOException e) {
            System.out.println("Error playing the audio file.");
        } catch (LineUnavailableException e) {
            System.out.println("Audio line is unavailable.");
        }
    }

    // player JUMPING state logic
    protected void playerJumping(float jumpAmplifier) {

        // if last frame player was on ground and this frame player is still on ground, the jump needs to be setup
        if (previousAirGroundState == AirGroundState.GROUND && airGroundState == AirGroundState.GROUND
                && (Keyboard.isKeyDown(CROUCH_KEY) == false && Keyboard.isKeyDown(CROUCH_KEY2) == false)) {

            keyLocker.lockKey(JUMP_KEY);
            // sets animation to a JUMP animation based on which way player is facing
            currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";

            keyLocker.lockKey(JUMP_KEY2);
            // sets animation to a JUMP animation based on which way player is facing
            currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";

            // player is set to be in air and then player is sent into the air
            airGroundState = AirGroundState.AIR;
            jumpForce = jumpHeight * jumpAmplifier;

            if (pressedBeforeLand) {
                if (jumpForce > 0) {
                    moveAmountY -= jumpForce;
                    jumpForce -= jumpDegrade - 2;
                    if (jumpForce < 0) {
                        jumpForce = 0;
                    }
                }
                pressedBeforeLand = false;
            } else {
                if (jumpForce > 0) {
                    moveAmountY -= jumpForce;
                    jumpForce -= jumpDegrade + 2;
                    if (jumpForce < 0) {
                        jumpForce = 0;
                    }
                }
            }
            File soundFile = new File("Sound/jump.WAV");
            playWav(soundFile); 
        }

        // if player is in air (currently in a jump) and has more jumpForce, continue sending player upwards
        else if (airGroundState == AirGroundState.AIR) {
            keyLocker.lockKey(JUMP_KEY);
            keyLocker.lockKey(JUMP_KEY2);
            if (Keyboard.isKeyDown(CROUCH_KEY)|| Keyboard.isKeyDown(CROUCH_KEY2)) {
                crouch = true;
                applyGravity(true);
                System.out.println("confiemed");
            }
            if (jumpForce > 0) {
                
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade;
                if (jumpForce < 0) {
                    jumpForce = 0;
                }
            }

            // allows you to move left and right while in the air
            if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_LEFT_KEY2)) {
                moveAmountX -= walkSpeed;
            } else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY2)) {
                moveAmountX += walkSpeed;
            }

            // if player is falling, increases momentum as player falls so it falls faster over time
            if (moveAmountY > 0) {
                increaseMomentum();
            }
        }

        // if player last frame was in air and this frame is now on ground, player enters STANDING state
        else if (previousAirGroundState == AirGroundState.AIR && airGroundState == AirGroundState.GROUND) {
            if (Keyboard.isKeyDown(JUMP_KEY) || Keyboard.isKeyDown(JUMP_KEY2)) {
                pressedBeforeLand = true;
            }
            playerState = PlayerState.STANDING;


        }
    }

    public void bounce() {
        // // Player is in the air now
        // airGroundState = AirGroundState.AIR;

        // // Set the jump force to the regular jump height
        // jumpForce = jumpHeight;

        // // Set the player state to JUMPING
        // playerState = PlayerState.JUMPING;

        // // Set the correct animation for jumping based on the player's facing direction
        // currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";

        airGroundState = AirGroundState.GROUND;
        previousAirGroundState = AirGroundState.GROUND;
        playerJumping(1f);
        System.out.println("called");
    }

    // while player is in air, this is called, and will increase momentumY by a set amount until player reaches terminal velocity
    protected void increaseMomentum() {
        momentumY += momentumYIncrease;
        if (momentumY > terminalVelocityY) {
            momentumY = terminalVelocityY;
        }
    }

    protected void updateLockedKeys() {
        if (Keyboard.isKeyUp(JUMP_KEY)) {
            keyLocker.unlockKey(JUMP_KEY);
        }
        else if(Keyboard.isKeyUp(JUMP_KEY2)){
            keyLocker.unlockKey(JUMP_KEY2);
        }
    }

    // anything extra the player should do based on interactions can be handled here
    protected void handlePlayerAnimation() {
        if (playerState == PlayerState.STANDING) {
            // sets animation to a STAND animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";

            // handles putting goggles on when standing in water
            // checks if the center of the player is currently touching a water tile
            int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
            int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
            MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
            if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_STAND_RIGHT" : "SWIM_STAND_LEFT";
            }
        } else if (playerState == PlayerState.WALKING) {
            // sets animation to a WALK animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";
        } else if (playerState == PlayerState.CROUCHING) {
            // sets animation to a CROUCH animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "CROUCH_RIGHT" : "CROUCH_LEFT";
        } else if (playerState == PlayerState.JUMPING) {
            // if player is moving upwards, set player's animation to jump. if player moving downwards, set player's animation to fall
            if (lastAmountMovedY <= 0) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
            } else {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "FALL_RIGHT" : "FALL_LEFT";
            }
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if player collides with a map tile below it, it is now on the ground
        // if player does not collide with a map tile below, it is in air
        if (direction == Direction.DOWN) {
            if (hasCollided) {
                
                momentumY = 0;
                airGroundState = AirGroundState.GROUND;

                // MapTile tile = (MapTile) entityCollidedWith;
                // if (tile != null && tile.getTileType() == TileType.SPRING) {
                //     playerJumping(1.75f);
                // }

                // check for spring platform in the same place
                if (entityCollidedWith instanceof MapTile) {
                    MapTile mapTile = (MapTile) entityCollidedWith;
                    for (EnhancedMapTile enhancedTile : map.getActiveEnhancedMapTiles()) {
                        if (enhancedTile.getX() != mapTile.getX() || enhancedTile.getY() != mapTile.getY()) {
                            continue;
                        }
    
                        if (enhancedTile instanceof Spring) {
                            playerJumping(1.75f);
                            break;
                        }
    
                    }
                } else if (entityCollidedWith instanceof Enemy) {
                    System.out.println("enemy");

                    float bottomEdge = getY() + getHeight();
                    float enemyTopEdge = entityCollidedWith.getY();

                    System.out.println(bottomEdge + " " + enemyTopEdge);

                    if (Math.abs(bottomEdge - enemyTopEdge) <= 25) {
                        playerJumping(1f);
                        entityCollidedWith.setMapEntityStatus(MapEntityStatus.REMOVED);
                        new Coin(entityCollidedWith.getLocation(), 10, map);
                    }
                }

            } else {
                playerState = PlayerState.JUMPING;
                airGroundState = AirGroundState.AIR;
            }
        }

        // if player collides with map tile upwards, it means it was jumping and then hit into a ceiling -- immediately stop upwards jump velocity
        else if (direction == Direction.UP) {
            if (hasCollided) {
                jumpForce = 0;
            }
        }
    }

    public void hurtPlayer(MapEntity mapEntity) {
        System.out.println("called");
        if (!isInvincible && !isHit) {
            if ((mapEntity instanceof Enemy || mapEntity instanceof Projectile) && hearts == 1) {
                levelState = LevelState.PLAYER_DEAD;
                hearts--;
            } else {
                hearts--;
                isHit = true;
                hitTimer = System.currentTimeMillis();
            }
        }
    }

    public void health() {

        if (hearts < 5){
            hearts++;
        }

    }

    public int getHearts(){
        return this.hearts;
    }

    // other entities can call this to tell the player they beat a level
    public void completeLevel() {
        levelState = LevelState.LEVEL_COMPLETED;
    }

    // if player has beaten level, this will be the update cycle
    public void updateLevelCompleted() {
        // if player is not on ground, player should fall until it touches the ground
        if (airGroundState != AirGroundState.GROUND && map.getCamera().containsDraw(this)) {
            currentAnimationName = "FALL_RIGHT";
            applyGravity(false);
            increaseMomentum();
            super.update();
            moveYHandleCollision(moveAmountY);
        }
        // move player to the right until it walks off screen
        else if (map.getCamera().containsDraw(this)) {
            currentAnimationName = "WALK_RIGHT";
            super.update();
            moveXHandleCollision(walkSpeed);
        } else {
            // tell all player listeners that the player has finished the level
            for (PlayerListener listener : listeners) {
                listener.onLevelCompleted();
            }
        }
    }

    // if player has died, this will be the update cycle
    public void updatePlayerDead() {
        // change player animation to DEATH
        if (!currentAnimationName.startsWith("DEATH")) {
            if (facingDirection == Direction.RIGHT) {
                currentAnimationName = "DEATH_RIGHT";
            } else {
                currentAnimationName = "DEATH_LEFT";
            }
            super.update();
        }
        // if death animation not on last frame yet, continue to play out death animation
        else if (currentFrameIndex != getCurrentAnimation().length - 1) {
            super.update();
        }
        // if death animation on last frame (it is set up not to loop back to start), player should continually fall until it goes off screen
        else if (currentFrameIndex == getCurrentAnimation().length - 1) {
            if (map.getCamera().containsDraw(this)) {
                moveY(3);
            } else {
                // tell all player listeners that the player has died in the level
                for (PlayerListener listener : listeners) {
                    listener.onDeath();
                }
            }
        }
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public AirGroundState getAirGroundState() {
        return airGroundState;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    public void setLevelState(LevelState levelState) {
        this.levelState = levelState;
    }

    public void addListener(PlayerListener listener) {
        listeners.add(listener);
    }

    // Uncomment this to have game draw player's bounds to make it easier to visualize
    /*
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        drawBounds(graphicsHandler, new Color(255, 0, 0, 100));
    }
    */
}