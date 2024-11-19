package Maps;

import SpriteFont.SpriteFont;
import Tilesets.CommonTileset;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import Level.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import Engine.GraphicsHandler;

// Represents a test map to be used in a level
public class TestMap extends Map {

    SpriteFont scoreText;
    SpriteFont highScoreText;
    SpriteFont moneyText;
    SpriteFont dashVisual; 


    private int xText = 600;

    ArrayList<SpriteFont> powerupTexts;

    private BufferedImage heartIcon;
    private int playerHealth;

    public TestMap() {
        super("test_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(6, 8).getLocation();

        moneyText = new SpriteFont("Money: 0", xText, 10, "Montserrat", 30, new Color(255, 255, 255));
        moneyText.setOutlineColor(Color.black);
        moneyText.setOutlineThickness(5);

        dashVisual = new SpriteFont("Dash", xText / 2 + 40, 10, "Montserrat", 30, new Color(255, 255, 255));
        dashVisual.setOutlineColor(Color.black);
        dashVisual.setOutlineThickness(5);


        scoreText = new SpriteFont("SCORE: 0", 10, 10, "Montserrat", 30, new Color(255, 255, 255));
        scoreText.setOutlineColor(Color.black);
        scoreText.setOutlineThickness(5);

        highScoreText = new SpriteFont("HIGH SCORE: 0", 10, 45, "Montserrat", 20, new Color(200, 200, 200));
        highScoreText.setOutlineColor(Color.black);
        highScoreText.setOutlineThickness(4);

        heartIcon = tileset.getSubImage(3, 3);

        // read the score file
        try {
            // check if the file exists first
            File file = new File("GameSaves/scoresaves.txt");
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

        PickupLoader.initialize(tileset);

        this.powerupTexts = new ArrayList<SpriteFont>();
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

        if (Player.getDash() == false){
            dashVisual.setColor(Color.white);
        } else {
            dashVisual.setColor(Color.black);
        }


        ArrayList<Pickup> activePickups = Pickup.GetActivePickups();

        ArrayList<SpriteFont> newPowerupTexts = new ArrayList<>();

        if (activePickups.size() > 0) {
            int index = 0;
            int startX = 550;
            int startY = 45;
            int spacing = 25;

            for (Pickup pickup : activePickups) {
                int yPosition = startY + (spacing * index);

                SpriteFont text = new SpriteFont(
                    pickup.getName() + " ACTIVE",
                    startX,
                    yPosition,
                    "Montserrat",
                    20,
                    new Color(255, 255, 255)
                );
                text.setOutlineColor(Color.black);
                text.setOutlineThickness(4);

                newPowerupTexts.add(text);
                index++;
            }
        }

        this.powerupTexts = newPowerupTexts;
        playerHealth = player.getHearts();
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);

        highScoreText.draw(graphicsHandler);
        scoreText.draw(graphicsHandler);
        moneyText.draw(graphicsHandler);
        dashVisual.draw(graphicsHandler);

        if (heartIcon != null) {
            int heartXStart = -5;
            int heartY = 73;
            int spacing = 20;

            for (int i = 0; i < playerHealth; i++) {
                int heartX = heartXStart + i * (heartIcon.getWidth() + spacing);
                graphicsHandler.drawImage(heartIcon, heartX, heartY, 55, 55);
            }
        }

        for (SpriteFont text : this.powerupTexts) {
            text.draw(graphicsHandler);
        }
    }
}
