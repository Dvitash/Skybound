package Maps;

import SpriteFont.SpriteFont;
import Tilesets.CommonTileset;
import java.io.File;
import java.util.Scanner;
import Level.*;
import java.awt.*;

import Engine.GraphicsHandler;

// Represents a test map to be used in a level
public class TestMap extends Map {

    SpriteFont scoreText;
    SpriteFont highScoreText;
    SpriteFont moneyText;

    private int xText = 600;
    SpriteFont speedBoostText;
    SpriteFont jumpBoostText;

    public TestMap() {
        super("test_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(6, 8).getLocation();

        moneyText = new SpriteFont("Money: 0", xText, 10, "Montserrat", 30, new Color(255, 255, 255));
        moneyText.setOutlineColor(Color.black);
        moneyText.setOutlineThickness(5);

        scoreText = new SpriteFont("SCORE: 0", 10, 10, "Montserrat", 30, new Color(255, 255, 255));
        scoreText.setOutlineColor(Color.black);
        scoreText.setOutlineThickness(5);

        highScoreText = new SpriteFont("HIGH SCORE: 0", 10, 45, "Montserrat", 20, new Color(200, 200, 200));
        highScoreText.setOutlineColor(Color.black);
        highScoreText.setOutlineThickness(4);

        speedBoostText = new SpriteFont("", 550, 45, "Montserrat", 20, new Color(255, 255, 0));
        speedBoostText.setOutlineColor(Color.black);
        speedBoostText.setOutlineThickness(4);

        jumpBoostText = new SpriteFont("", 561, 70, "Montserrat", 20, new Color(50, 215, 100));
        jumpBoostText.setOutlineColor(Color.black);
        jumpBoostText.setOutlineThickness(4);

        // read the score file
        try {
            // check if the file exists first
            File file = new File("GameSaves\\scoresaves.txt");
            if (file.exists()) {
                Scanner scan = new Scanner(file);
                int highScore = scan.nextInt();
                highScoreText.setText("HIGH SCORE: " + highScore);
                scan.close();
            } else {
                highScoreText.setText("HIGH SCORE: 0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Player player) {
        super.update(player);

        this.scoreText.setText("SCORE: " + Integer.toString(player.getScore()));

        int money = player.getMoney();
        int charCount = 1;

        while (money >= 10) {
            charCount += 1;
            money = money % 10;
        }

        
        this.moneyText.setX(xText - (15 * charCount));
        this.moneyText.setText("MONEY: " + player.getMoney());
    
        if (player.getJumpBoostActive() == true){
            this.jumpBoostText.setText("JUMP BOOST ACTIVE");
        }else{
            this.jumpBoostText.setText("");
        }

        if (player.getSpeedBoostActive() == true){
            this.speedBoostText.setText("SPEED BOOST ACTIVE");
        }else{
            this.speedBoostText.setText("");
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);

        highScoreText.draw(graphicsHandler);
        scoreText.draw(graphicsHandler);
        moneyText.draw(graphicsHandler);
        speedBoostText.draw(graphicsHandler);
        jumpBoostText.draw(graphicsHandler);
    }
/* 
    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        BugEnemy bugEnemy = new BugEnemy(getMapTile(3, 10).getLocation().subtractY(25), Direction.LEFT);
        enemies.add(bugEnemy);

        DinosaurEnemy dinosaurEnemy = new DinosaurEnemy(getMapTile(19, 1).getLocation().addY(2), getMapTile(22, 1).getLocation().addY(2), Direction.RIGHT);
        enemies.add(dinosaurEnemy);

        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        HorizontalMovingPlatform hmp = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(24, 6).getLocation(),
                getMapTile(27, 6).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                3,
                new Rectangle(0, 6,16,4),
                Direction.RIGHT
        );
        enhancedMapTiles.add(hmp);

        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(32, 7).getLocation());
        enhancedMapTiles.add(endLevelBox);

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        Walrus walrus = new Walrus(getMapTile(30, 10).getLocation().subtractY(13));
        npcs.add(walrus);

        return npcs;
    }
*/
}
