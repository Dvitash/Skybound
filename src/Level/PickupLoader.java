package Level;

import GameObject.Rectangle;
import Pickups.JumpBoost;
import Pickups.Magnet;
import Pickups.SpeedBoost;
import Utils.Point;


public class PickupLoader {
    public static void initialize(Tileset tileset) {
        SpeedBoost speedBoost = new SpeedBoost(
                tileset.getSubImage(2, 4),
                new Point(0, 0),
                TileType.PASSABLE,
                tileset.getTileScale(),
                new Rectangle(4, 1, 8, 5),
                "Speed Boost", 8);

        JumpBoost jumpBoost = new JumpBoost(
                tileset.getSubImage(2, 5),
                new Point(0, 0),
                TileType.PASSABLE,
                tileset.getTileScale(),
                new Rectangle(4, 1, 8, 5),
                "Jump Boost", 5);

        Magnet magnet = new Magnet(
                tileset.getSubImage(3, 4),
                new Point(0, 0),
                TileType.PASSABLE,
                tileset.getTileScale(),
                new Rectangle(4, 1, 8, 5),
                "Magnet", 3);
    }
}
