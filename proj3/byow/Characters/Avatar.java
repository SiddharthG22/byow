package byow.Characters;

import byow.Core.World;
import byow.Core.WorldCreation;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;

public class Avatar extends Sprites implements Serializable {

    public int bulletX;
    public int bulletY;
    public int ammo;
    public int powerUp;

    public boolean shotFired;
    public ArrayList<Item> inventory;
    public ArrayList<Key> keyList;

    public double shootingTime = 0.1;
    public double lastTimeShot;
    public String shootingDirection;

    public Avatar(World world) {
        this.x = WorldCreation.WIDTH / 2;
        this.y = WorldCreation.HEIGHT / 2;
        this.ammo = 100;
        this.powerUp = 0;
        this.speed = 1.0;
        this.health = 100;
        this.damage = 2;
//        this.nearestSprite = null;
        this.inventory = new ArrayList<>();
        this.keyList = new ArrayList<>();
        this.worldAvatar = this;
        this.shotFired = false;

        this.world = world;
        this.tileType = Tileset.AVATAR;
        setLocation();
    }

    public void pewpew(String direction) {
        if (this.shotFired || this.ammo <= 0) {
            return;
        }
        this.shotFired = true;
        this.ammo -= 1;
        this.shootingDirection = direction;
        this.lastTimeShot = world.sw.elapsedTime();
        this.bulletX = x;
        this.bulletY = y;
    }

    public boolean timeToShoot(double time) {
        return time - lastTimeShot > shootingTime;
    }


    @Override
    public void setLocation() {
        TETile[][] worldArray = this.world.getWorldArray();
        worldArray[x][y] = this.tileType;
    }

    @Override
    public void move(String direction) {
        TETile[][] worldArray = this.world.getWorldArray();
        worldArray[x][y] = Tileset.FLOOR;
        switch (direction) {
            case "up" -> {
                if (worldArray[x][y + 1].getTileType() == TETile.TileType.FLOOR) {
                    y++;
                }
            }
            case "down" -> {
                if (worldArray[x][y - 1].getTileType() == TETile.TileType.FLOOR) {
                    y--;
                }
            }
            case "left" -> {
                if (worldArray[x - 1][y].getTileType() == TETile.TileType.FLOOR) {
                    x--;
                }
            }
            case "right" -> {
                if (worldArray[x + 1][y].getTileType() == TETile.TileType.FLOOR) {
                    x++;
                }
            }
        }
        worldArray[x][y] = this.getTile();
    }

    @Override
    public void dealDamage(int d) {

    }

    @Override
    public void loseHealth(int d) { // change avatar appearance when attacked (see tileset)
//        this.tileType = Tileset.INJURED_AVATAR;
//        // Update the avatar's health and last time shot
//        this.lastTimeShot = this.world.sw.elapsedTime();
        this.health -= d;
//        // Update the avatar's location to show the injured avatar
//        setLocation();
//
//        // Pause the game briefly (1 second) to display the injured avatar
//        lastTimeShot = this.world.sw.elapsedTime();
//
//        // Revert the avatar's tile type back to the original after the pause
//        this.tileType = Tileset.AVATAR;
//        setLocation();
    }


}
