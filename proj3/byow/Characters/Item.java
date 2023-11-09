package byow.Characters;

import byow.Core.World;
import byow.TileEngine.TETile;

import java.io.Serializable;

public abstract class Item implements Serializable {

    public int x;
    public int y;
    public World world;
    public String type;
    public TETile tileType;

    public abstract void useItem();

    public abstract void buffItem();

    public abstract void unbuffItem();

}
