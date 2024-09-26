package Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import Builders.MapTileBuilder;

public class PlatformIndexes {
    private static Map<MapTileBuilder, Integer> platforms = new HashMap<>();
    private static Random random = new Random();

    public static void addPlatform(MapTileBuilder platform, int weight) {
        platforms.put(platform, weight);
    }

    public static MapTileBuilder GetRandomPlatform() {
        int totalWeight = platforms.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight);

        for (Map.Entry<MapTileBuilder, Integer> entry : platforms.entrySet()) {
            randomValue -= entry.getValue();
            if (randomValue < 0) {
                return entry.getKey();
            }
        }

        return null;
    }
}