package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import Level.PlatformIndexes;

import java.util.ArrayList;

// This class represents a "common" tileset of standard tiles defined in the CommonTileset.png file
public class CommonTileset extends Tileset {

    public CommonTileset() {
        super(ImageLoader.load("CommonUpdatedTileset.png"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // sky
        Frame skyFrame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder skyTile = new MapTileBuilder(skyFrame);

        mapTiles.add(skyTile);

        // top water
        Frame topWaterFrame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder topWaterTile = new MapTileBuilder(topWaterFrame);

        mapTiles.add(topWaterTile);

        // water
        Frame waterFrame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder waterTile = new MapTileBuilder(waterFrame)
                .withTileType(TileType.WATER);

        mapTiles.add(waterTile);

        // dirt
        Frame dirtFrame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder dirtTile = new MapTileBuilder(dirtFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(dirtTile);

        // dirt left top
        Frame dirtLeftFrame = new FrameBuilder(getSubImage(0, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder dirtLeftTile = new MapTileBuilder(dirtLeftFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(dirtLeftTile);

        // dirt right top
        Frame dirtRightFrame = new FrameBuilder(getSubImage(0, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder dirtRightTile = new MapTileBuilder(dirtRightFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(dirtRightTile);

        // left palm tree
        Frame leftPalmFrame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftPalmTile = new MapTileBuilder(leftPalmFrame);

        mapTiles.add(leftPalmTile);

        // right palm tree
        Frame rightPalmFrame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightPalmTile = new MapTileBuilder(rightPalmFrame);

        mapTiles.add(rightPalmTile);

        // right palm tree trunk
        Frame rightPalmTrunkFrame = new FrameBuilder(getSubImage(1, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightPalmTrunkTile = new MapTileBuilder(rightPalmTrunkFrame);

        mapTiles.add(rightPalmTrunkTile);

        // left palm tree trunk
        Frame leftPalmTrunkFrame = new FrameBuilder(getSubImage(1, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftPalmTrunkTile = new MapTileBuilder(leftPalmTrunkFrame);

        mapTiles.add(leftPalmTrunkTile);

        // breaking middle wood platform
        Frame breakingWoodFrame = new FrameBuilder(getSubImage(2, 1))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .build();

        MapTileBuilder breakingWoodTile = new MapTileBuilder(breakingWoodFrame)
                .withTileType(TileType.BREAKAWAY);

        PlatformIndexes.addPlatform(breakingWoodTile, 25);
        mapTiles.add(breakingWoodTile);

        // cloud
        Frame cloudFrame = new FrameBuilder(getSubImage(1, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder cloudTile = new MapTileBuilder(cloudFrame);

        PlatformIndexes.addPlatform(cloudTile, 15);
        mapTiles.add(cloudTile);

        // middle wood platform
        Frame midWoodFrame = new FrameBuilder(getSubImage(1, 5))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .build();

        MapTileBuilder midWoodTile = new MapTileBuilder(midWoodFrame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);

        PlatformIndexes.addPlatform(midWoodTile, 60);
        mapTiles.add(midWoodTile);

        // middle metal platform
        Frame middleMetalPlatformFrame = new FrameBuilder(getSubImage(2, 0))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .build();

        MapTileBuilder middleMetalPlatformTile = new MapTileBuilder(middleMetalPlatformFrame)
                .withTileType(TileType.NOT_PASSABLE);

        PlatformIndexes.addPlatform(middleMetalPlatformTile, 20);
        mapTiles.add(middleMetalPlatformTile);
        return mapTiles;
    }
}