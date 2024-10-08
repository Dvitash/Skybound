package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player;

import java.util.HashMap;

// This is the class for the Cat player character
// basically just sets some values for physics and then defines animations
public class Skyler extends Player {

    public Skyler(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Skyler.png"), 16, 16), x, y, "STAND_RIGHT");
        gravity = .5f;
        terminalVelocityY = 6f;
        jumpHeight = 14.5f;
        jumpDegrade = .5f;
        walkSpeed = 2.3f;
        momentumYIncrease = .775f;
        dashDegrade = 1f;

        
    }

    public void update() {
        super.update();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        // drawBounds(graphicsHandler, new Color(255, 0, 0, 170));
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0))
                            .withScale(3)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0), 14)
                            .withScale(3)
                            .withBounds(3, 3, 11, 12)
                            .build(),
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0), 14)
                            .withScale(3)
                            .withBounds(3, 3, 11, 12)
                            .build(),
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0), 14)
                            .withScale(3)
                            .withBounds(3, 3, 11, 12)
                            .build(),
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0), 14)
                            .withScale(3)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(3, 3, 11, 12)
                            .build(),
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(3, 3, 11, 12)
                            .build(),
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(3, 3, 11, 12)
                            .build(),
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("JUMP_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0))
                            .withScale(3)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("JUMP_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(1, 0))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("FALL_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 0))
                            .withScale(3)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("FALL_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 0))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("CROUCH_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 1))
                            .withScale(3)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("CROUCH_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 1))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("DEATH_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 0), 8)
                            .withScale(3)
                            .build(),
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 0), 8)
                            .withScale(3)
                            .build(),
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 0), -1)
                            .withScale(3)
                            .build()
            });

            put("DEATH_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 0), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build(),
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 0), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build(),
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 0), -1)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build()
            });

            put("SWIM_STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 0))
                            .withScale(3)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });

            put("SWIM_STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSpriteNoOffset(0, 0))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(3, 3, 11, 12)
                            .build()
            });
        }};
    }
}
