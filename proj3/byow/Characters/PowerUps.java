package byow.Characters;

import byow.Core.World;
import byow.TileEngine.Tileset;

import java.util.Random;

public class PowerUps extends Item {

    public double powerUp;
    public Random rand;

    public PowerUps(int x, int y, World w) {
        this.x = x;
        this.y = y;
        this.world = w;
        this.rand = new Random();
        this.powerUp = 0.01;
        this.type = "Power-up";
        this.tileType = Tileset.TREE; // change later
    }

    @Override
    public void useItem() {
        double randAttribute = rand.nextDouble(2);
        if (randAttribute == 0) {
            if (this.world.getAvatar().shootingTime > 0.05) {
                this.world.getAvatar().shootingTime -= this.powerUp;
            }
        } else {
            if (this.world.getAvatar().speed > 0.9) {
                this.world.getAvatar().speed -= this.powerUp;
            }
        }
    }
    @Override
    public void buffItem() {
        this.powerUp = 0.025;
    }
    @Override
    public void unbuffItem() {
        this.powerUp = 0.01;
    }

}
