package Enemies;

import Builders.FrameBuilder;
import Engine.Config;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.Enemy;
import Level.MapEntity;
import Level.Player;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;
import java.util.Random;
import java.io.File;
import java.util.HashMap;

// This class is for the black bug enemy
// enemy behaves like a Mario goomba -- walks forward until it hits a solid map tile, and then turns around
// if it ends up in the air from walking off a cliff, it will fall down until it hits the ground again, and then will continue walking
public class SpawnableEnemy extends Enemy {

    private float gravity = .5f;
    private float movementSpeed = .5f;
    private Direction startFacingDirection;
    private Direction facingDirection;
    private AirGroundState airGroundState;

    public static String getRandomSprite(boolean isShooting) {
        String folderName;
        if (isShooting) {
            folderName = "ShootingEnemies";
        } else {
            folderName = "RegularEnemies";
        }

        File folder = new File(Config.RESOURCES_PATH + folderName);

        if (folder.isDirectory()) {
            // Get all the children (files and subdirectories)
            File[] children = folder.listFiles();

            if (children != null && children.length > 0) {
                int numChildren = children.length;

                Random random = new Random();
                int randomIndex = random.nextInt(numChildren);

                File randomChild = children[randomIndex];

                return folderName + File.separator + randomChild.getName();
            } else {
                System.out.println("The folder is empty.");
                return null;
            }
        } else {
            System.out.println("This is not a directory.");
            return null;
        }
    }

    public SpawnableEnemy(Point location, Direction facingDirection, boolean isShooting) {
        super(location.x, location.y-5, new Frame(ImageLoader.load(getRandomSprite(isShooting)), ImageEffect.NONE, 2.0f, new Rectangle(1, 1, 22, 14)), isShooting);
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "WALK_RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "WALK_LEFT";
        }
        airGroundState = AirGroundState.GROUND;
    }

    @Override
    public void update(Player player) {
        float moveAmountX = 0;
        float moveAmountY = 0;

        // // add gravity (if in air, this will cause bug to fall)
        // moveAmountY += gravity;

        // // if on ground, walk forward based on facing direction
        // if (airGroundState == AirGroundState.GROUND) {
        //     if (facingDirection == Direction.RIGHT) {
        //         moveAmountX += movementSpeed;
        //     } else {
        //         moveAmountX -= movementSpeed;
        //     }
        // }

        // move bug
        moveYHandleCollision(moveAmountY);
        moveXHandleCollision(moveAmountX);

        super.update(player);
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction,  MapEntity entityCollidedWith) {
        // if bug has collided into something while walking forward,
        // it turns around (changes facing direction)
        if (hasCollided) {
            if (direction == Direction.RIGHT) {
                facingDirection = Direction.LEFT;
                currentAnimationName = "WALK_LEFT";
            } else {
                facingDirection = Direction.RIGHT;
                currentAnimationName = "WALK_RIGHT";
            }
        }
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if bug is colliding with the ground, change its air ground state to GROUND
        // if it is not colliding with the ground, it means that it's currently in the air, so its air ground state is changed to AIR
        if (direction == Direction.DOWN) {
            if (hasCollided) {
                airGroundState = AirGroundState.GROUND;
            } else {
                airGroundState = AirGroundState.AIR;
            }
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(2)
                            .withBounds(6, 6, 12, 7)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(2)
                            .withBounds(6, 6, 12, 7)
                            .build()
            });

            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 6, 12, 7)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 6, 12, 7)
                            .build()
            });
        }};
    }
}
