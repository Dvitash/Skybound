package Maps;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Sprite;
import Level.Map;
import Tilesets.CommonTileset;
import Utils.Colors;
import Utils.Point;

// Represents the map that is used as a background for the main menu and credits menu screen
public class TitleScreenMap extends Map {

    private Sprite skyler;

    public TitleScreenMap() {
        super("title_screen_map.txt", new CommonTileset());
        Point skylerLocation = getMapTile(6, 10).getLocation().addY(4);
        skyler = new Sprite(ImageLoader.loadSubImage("Skyler.png", Colors.MAGENTA, 0, 0, 16, 16));
        skyler.setScale(3);
        skyler.setLocation(skylerLocation.x, skylerLocation.y);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        skyler.draw(graphicsHandler);
    }

}
