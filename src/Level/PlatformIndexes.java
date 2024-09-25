package Level;

import java.util.ArrayList;
import Builders.MapTileBuilder;

public class PlatformIndexes {
    public static ArrayList<MapTileBuilder> platforms = new ArrayList<>();  

    public static MapTileBuilder GetRandomPlatform() {
        return platforms.get((int)(Math.random() * platforms.size()));
    }
}
