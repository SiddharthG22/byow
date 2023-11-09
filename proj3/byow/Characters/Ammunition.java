package byow.Characters;

import byow.Core.World;
import byow.TileEngine.Tileset;

import java.io.Serializable;

public class Ammunition extends Item implements Serializable {

    public int rounds;

    public Ammunition(int x, int y, World w) {
        this.x = x;
        this.y = y;
        this.world = w;
        this.rounds = 10;
        this.type = "Ammunition";
        this.tileType = Tileset.UNLOCKED_DOOR; // change later
    }

    @Override
    public void useItem() {
        if (this.world.getAvatar().ammo <= 100 - this.rounds) {
            this.world.getAvatar().ammo += this.rounds;
        } else {
            this.world.getAvatar().ammo = 100;
        }
    }

    @Override
    public void buffItem() {
        this.rounds = 20;
    }

    @Override
    public void unbuffItem() {
        this.rounds = 10;
    }

}
