package byow.Characters;

import byow.Core.World;
import byow.TileEngine.Tileset;

public class HealthPod extends Item {

    public int health;

    public HealthPod(int x, int y, World w) {
        this.x = x;
        this.y = y;
        this.world = w;
        this.health = 10;
        this.type = "Health";
        this.tileType = Tileset.WATER; // change later
    }

    @Override
    public void useItem() {
        if (this.world.getAvatar().health <= 100 - this.health) {
            this.world.getAvatar().health += this.health;
        } else {
            this.world.getAvatar().health = 100;
        }
    }

    @Override
    public void buffItem() {
        this.health = 20;
    }
    @Override
    public void unbuffItem() {
        this.health = 10;
    }

}
