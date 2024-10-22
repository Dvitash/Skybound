package Level;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.Rectangle;
import Utils.Point;
import java.util.Random;
import Engine.ImageLoader;


// This class is a base class for all enemies in the game -- all enemies should extend from it
public class Coin extends MapEntity {

    protected int coinAmount = 0;
    private boolean collected = false;

    public Coin(Point location, int coinAmount, Map map) {
        super(location.x, location.y, new Frame(ImageLoader.load("Coin.png"), ImageEffect.NONE, 2.0f, new Rectangle(0, 0, 15, 15)));
        this.initialize();

        this.coinAmount = coinAmount;

        map.coins.add(this);
        this.setMap(map);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void update(Player player) {
        if (intersects(player) && !collected) {    
            collected = true;

            player.money += this.coinAmount;
            setMapEntityStatus(MapEntityStatus.REMOVED);
        }
    }
}
