package byow.Characters;

import byow.Core.World;
import byow.TileEngine.TETile;

import java.io.Serializable;

public abstract class Sprites implements Serializable {

    public int x;
    public int y;
    public double speed;
    public int health;
    public int damage;
    public String type;

    public World world;
    public TETile tileType;
    public Sprites worldAvatar;

    // selector functions to get attributes
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public double getSpeed() {
        return this.speed;
    }

    public int getHealth() {
        return this.health;
    }

    public int getDamage() {
        return this.damage;
    }

    public World getWorld() {
        return this.world;
    }

    public String getType() {
        return this.type;
    }

    public TETile getTile() {
        return this.tileType;
    }

    public Sprites getWorldAvatar() {
        return this.worldAvatar;
    }

    // actual methods
    public abstract void setLocation();

    public abstract void move(String direction);

    public abstract void dealDamage(int d);

    public abstract void loseHealth(int d);

}
