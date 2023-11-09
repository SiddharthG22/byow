package byow.Characters;

import byow.Core.World;
import byow.TileEngine.Tileset;

public class Key extends Item {

    public Key(int x, int y, World w) {
        this.x = x;
        this.y = y;
        this.world = w;
        this.type = "Key";
        this.tileType = Tileset.SAND; // change later
    }

    @Override
    public void useItem() {
        // nothing happens
    }

    @Override
    public void buffItem() {
        // nothing happens
    }
    @Override
    public void unbuffItem() {
        // nothing happens
    }


}

