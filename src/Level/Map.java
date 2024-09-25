package Level;

import Engine.Config;
import Engine.GraphicsHandler;
import Engine.ScreenManager;
import Utils.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

/*
    This class is for defining a map that is used for a specific level
    The map class handles/manages a lot of different things, including:
    1. tile map -- the map tiles that make up the map
    2. entities in the map -- this includes enemies, enhanced map tiles, and npcs
    3. the map's camera, which does a lot of work itself in the Camera class
    4. adjusting camera location based off of player location
    5. calculating which tile a game object is currently on based on its x and y location
*/

public abstract class Map {
    // the tile map (map tiles that make up the entire map image)
    // protected MapTile[] mapTiles;

    protected HashMap<Point, MapTile> mapTiles;

    // width and height of the map in terms of the number of tiles width-wise and   height-wise
    protected int width;
    protected int height;

    // the tileset this map uses for its map tiles
    protected Tileset tileset;

    // camera class that handles the viewable part of the map that is seen by the player of a game during a level
    protected Camera camera;

    // location player should start on when this map is first loaded
    protected Point playerStartPosition;

    // the location of the "mid point" of the screen
    // this is what tells the game that the player has reached the center of the screen, therefore the camera should move instead of the player
    // this goes into creating that "map scrolling" effect
    protected int xMidPoint, yMidPoint;

    // in pixels, this basically creates a rectangle defining how big the map is
    // startX and Y will always be 0, endX and Y is the number of tiles multiplied by the number of pixels each tile takes up
    protected int startBoundX, startBoundY, endBoundX, endBoundY;

    // the name of the map text file that has the tile map information
    protected String mapFileName;

    // lists to hold map entities that are a part of the map
    protected ArrayList<Enemy> enemies;
    protected ArrayList<EnhancedMapTile> enhancedMapTiles;
    protected ArrayList<NPC> npcs;

    // if set to false, camera will not move as player moves
    protected boolean adjustCamera = true;

    protected float platformLevel = 0;
    protected Random random;
    protected int minY;

    // map tiles in map that are animated
    protected ArrayList<MapTile> animatedMapTiles;

    public Map(String mapFileName, Tileset tileset) {
        this.mapFileName = mapFileName;
        this.tileset = tileset;
        setupMap();
        this.startBoundX = 0;
        this.startBoundY = 0;
        this.endBoundX = width * tileset.getScaledSpriteWidth();
        this.endBoundY = height * tileset.getScaledSpriteHeight();
        this.xMidPoint = ScreenManager.getScreenWidth() / 2;
        this.yMidPoint = (ScreenManager.getScreenHeight() / 2);
        this.playerStartPosition = new Point(0, 0);
        this.random = new Random();

        this.minY = height * tileset.getScaledSpriteHeight();
    }

    // sets up map by reading in the map file to create the tile map
    // loads in enemies, enhanced map tiles, and npcs
    // and instantiates a Camera
    public void setupMap() {
        this.animatedMapTiles = new ArrayList<>();

        loadMapFile();

        this.enemies = loadEnemies();
        for (Enemy enemy: this.enemies) {
            enemy.setMap(this);
        }

        this.enhancedMapTiles = loadEnhancedMapTiles();
        for (EnhancedMapTile enhancedMapTile: this.enhancedMapTiles) {
            enhancedMapTile.setMap(this);
        }

        this.npcs = loadNPCs();
        for (NPC npc: this.npcs) {
            npc.setMap(this);
        }

        this.camera = new Camera(0, 0, tileset.getScaledSpriteWidth(), tileset.getScaledSpriteHeight(), this);
    }

    // reads in a map file to create the map's tilemap
    private void loadMapFile() {
        Scanner fileInput;
        try {
            // open map file that is located in the MAP_FILES_PATH directory
            fileInput = new Scanner(new File(Config.MAP_FILES_PATH + this.mapFileName));
        } catch(FileNotFoundException ex) {
            // if map file does not exist, create a new one for this map (the map editor uses this)
            System.out.println("Map file " + Config.MAP_FILES_PATH + this.mapFileName + " not found! Creating empty map file...");

            try {
                createEmptyMapFile();
                fileInput = new Scanner(new File(Config.MAP_FILES_PATH + this.mapFileName));
            } catch(IOException ex2) {
                ex2.printStackTrace();
                System.out.println("Failed to create an empty map file!");
                throw new RuntimeException();
            }
        }

        // read in map width and height from the first line of map file
        this.width = fileInput.nextInt();
        this.height = fileInput.nextInt();

        // define array size for map tiles, which is width * height (this is a standard array, NOT a 2D array)
        // this.mapTiles = new MapTile[this.height * this.width];
        this.mapTiles = new HashMap<>();
        fileInput.nextLine();

        // read in each tile index from the map file, use the defined tileset to get the associated MapTile to that tileset, and place it in the array
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int tileIndex = fileInput.nextInt();
                int xLocation = j * tileset.getScaledSpriteWidth();
                int yLocation = i * tileset.getScaledSpriteHeight();
                MapTile tile = tileset.getTile(tileIndex).build(xLocation, yLocation);
                setMapTile(j, i, tile);
    
                if (tile.isAnimated()) {
                    animatedMapTiles.add(tile);
                }
            }
        }

        fileInput.close();
    }

    // creates an empty map file for this map if one does not exist
    // defaults the map dimensions to 0x0
    private void createEmptyMapFile() throws IOException {
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(Config.MAP_FILES_PATH + this.mapFileName);
        fileWriter.write("0 0\n");
        fileWriter.close();
    }

    // gets player start position based on player start tile (basically the start tile's position on the map)
    public Point getPlayerStartPosition() {
        return playerStartPosition;
    }

    // get position on the map based on a specfic tile index
    public Point getPositionByTileIndex(int xIndex, int yIndex) {
        MapTile tile = getMapTile(xIndex, yIndex);
        return new Point(tile.getX(), tile.getY());
    }

    public Tileset getTileset() {
        return tileset;
    }

    public String getMapFileName() {
        return mapFileName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidthPixels() {
        return width * tileset.getScaledSpriteWidth();
    }

    public int getHeightPixels() {
        return height * tileset.getScaledSpriteHeight();
    }

    public HashMap<Point, MapTile> getMapTiles() {
        return mapTiles;
    }

    public void setMapTiles(HashMap<Point, MapTile> mapTiles) {
        this.mapTiles = mapTiles;
    }

    // get specific map tile from tile map
    public MapTile getMapTile(int x, int y) {
        Point point = new Point(x, y);

        return mapTiles.get(point);
    }

    // set specific map tile from tile map to a new map tile
    public void setMapTile(int x, int y, MapTile tile) {
        Point point = new Point(x, y);
    
        MapTile oldMapTile = mapTiles.get(point);
        animatedMapTiles.remove(oldMapTile);

        mapTiles.put(point, tile);

        if (tile.isAnimated()) {
            animatedMapTiles.add(tile);
        }

        tile.setMap(this);
    }

    // returns a tile based on a position in the map
    public MapTile getTileByPosition(float xPosition, float yPosition) {
        Point tileIndex = getTileIndexByPosition(xPosition, yPosition);
        return mapTiles.get(new Point(Math.round(tileIndex.x), Math.round(tileIndex.y)));
    }
    
    public Point getTileIndexByPosition(float xPosition, float yPosition) {
        int xIndex = Math.round(xPosition / tileset.getScaledSpriteWidth());
        int yIndex = Math.round(yPosition / tileset.getScaledSpriteHeight());
        return new Point(xIndex, yIndex);
    }

    // list of enemies defined to be a part of the map, should be overridden in a subclass
    protected ArrayList<Enemy> loadEnemies() {
        return new ArrayList<>();
    }

    // list of enhanced map tiles defined to be a part of the map, should be overridden in a subclass
    protected ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        return new ArrayList<>();
    }

    // list of npcs defined to be a part of the map, should be overridden in a subclass
    protected ArrayList<NPC> loadNPCs() {
        return new ArrayList<>();
    }

    public Camera getCamera() {
        return camera;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
    public ArrayList<EnhancedMapTile> getEnhancedMapTiles() {
        return enhancedMapTiles;
    }
    public ArrayList<NPC> getNPCs() {
        return npcs;
    }

    public ArrayList<MapTile> getAnimatedMapTiles() {
        return animatedMapTiles;
    }

    // returns all active enemies (enemies that are a part of the current update cycle) -- this changes every frame by the Camera class
    public ArrayList<Enemy> getActiveEnemies() {
        return camera.getActiveEnemies();
    }

    // returns all active enhanced map tiles (enhanced map tiles that are a part of the current update cycle) -- this changes every frame by the Camera class
    public ArrayList<EnhancedMapTile> getActiveEnhancedMapTiles() {
        return camera.getActiveEnhancedMapTiles();
    }

    // returns all active npcs (npcs that are a part of the current update cycle) -- this changes every frame by the Camera class
    public ArrayList<NPC> getActiveNPCs() {
        return camera.getActiveNPCs();
    }

    // add an enemy to the map's list of enemies
    public void addEnemy(Enemy enemy) {
        enemy.setMap(this);
        this.enemies.add(enemy);
    }

    // add an enhanced map tile to the map's list of enhanced map tiles
    public void addEnhancedMapTile(EnhancedMapTile enhancedMapTile) {
        enhancedMapTile.setMap(this);
        this.enhancedMapTiles.add(enhancedMapTile);
    }

    // add an npc to the map's list of npcs
    public void addNPC(NPC npc) {
        npc.setMap(this);
        this.npcs.add(npc);
    }

    public void setAdjustCamera(boolean adjustCamera) {
        this.adjustCamera = adjustCamera;
    }

    private int lowestYGenerated = Integer.MAX_VALUE;
    public void GeneratePlatforms(int startPosY) {
        int scaledStartPos = startPosY / tileset.getScaledSpriteHeight();
        
        for (int y = scaledStartPos; y > scaledStartPos - 16; y--) {
            if (y >= lowestYGenerated) { continue; }
            lowestYGenerated = y;
    
            int yLocation = y * tileset.getScaledSpriteHeight();
    
            if (yLocation > startPosY) { continue; }
    
            for (int x = 0; x < width; x++) {
                int xLocation = x * tileset.getScaledSpriteWidth();
    
                MapTile tileAtPosition = getMapTile(x, y);
                if (tileAtPosition == null || tileAtPosition.getTileIndex() != 5) {
                    // place air if not placed already
                    if (getMapTile(x, y) == null) {
                        MapTile airTile = tileset.getTile(0).build(xLocation, yLocation);
                        setMapTile(x, y, airTile);
                    }
    
                    boolean hasNeighbor = false;
    
                    // check neighbors (up, down, left, right)
                    MapTile upTile = getMapTile(x, y - 1);
                    MapTile downTile = getMapTile(x, y + 1);
                    MapTile leftTile = getMapTile(x - 1, y);
                    MapTile rightTile = getMapTile(x + 1, y);
    
                    if ((upTile != null && upTile.getTileIndex() == 12) ||
                        (downTile != null && downTile.getTileIndex() == 12) ||
                        (leftTile != null && leftTile.getTileIndex() == 12) ||
                        (rightTile != null && rightTile.getTileIndex() == 12)) {
                        hasNeighbor = true;
                    }
    
                    double chance = this.random.nextDouble();
                    if (chance < 0.15 && !hasNeighbor) { // default chance of 15%
                        MapTile platform = tileset.getTile(12).build(xLocation, yLocation);
                        setMapTile(x, y, platform);
                    }
                }
            }
        }
    }
    
    public void update(Player player) {
        if (adjustCamera) {
            adjustMovementY(player);
        }

        camera.update(player);
    }

    public boolean UpdateMapTileBounds() {
        float cameraY = camera.getEndBoundY();
        boolean deleted = false;
    
        Iterator<MapTile> iterator = mapTiles.values().iterator();
    
        while (iterator.hasNext()) {
            MapTile tile = iterator.next();
            if (tile.getY() > cameraY) {
                iterator.remove();
                deleted = true;
            }
        }
    
        return deleted;
    }

    // based on the player's current Y position (which in a level can potentially be updated each frame),
    // adjust the player's and camera's positions accordingly in order to properly create the map "scrolling" effect
    private float previousPlayerY = -1;
    private void adjustMovementY(Player player) {
        float playerY = (player.getY() + (player.getHeight() / 2));

        yMidPoint = (int) camera.getY() + (ScreenManager.getScreenHeight() / 2);
        GeneratePlatforms(yMidPoint); 

        // // if player goes past center screen (below) and there is more map to show below, push player back to center and move camera upward
        // if (playerY > yMidPoint && camera.getEndBoundY() < endBoundY && !cameraReachedMaxHeight) {
        //     float yMidPointDifference = yMidPoint - playerY;
        //     // camera.moveY(-yMidPointDifference);  
        //     // MoveMap(yMidPointDifference);
    
        //     // if camera moved past the bottom of the map as a result from the move above, move camera upwards and push player downwards
        //     if (camera.getEndBoundY() > endBoundY) {
        //         float cameraDifference = camera.getEndBoundY() - endBoundY;
        //         // camera.  moveY(-cameraDifference);

        //         // MoveMap(cameraDifference);
        //     }
        // }
        
        // if player goes above center of the screen, move map downwards
        if (playerY < yMidPoint) {
            float yMidPointDifference;
            
            if (previousPlayerY == -1) {
                previousPlayerY = playerY;
            }
    
            yMidPointDifference = previousPlayerY - playerY;
            previousPlayerY = playerY;

            if (yMidPointDifference > 0) {
                camera.moveUp(yMidPointDifference);
            }

            if (UpdateMapTileBounds()) {
                GeneratePlatforms(yMidPoint);
            }
        }
    }

    public void reset() {
        setupMap();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        camera.draw(graphicsHandler);
    }

    public int getEndBoundX() { return endBoundX; }
    public int getEndBoundY() { return endBoundY; }
}
