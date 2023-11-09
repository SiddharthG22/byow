package byow.Characters;

import byow.Core.RandomUtils;
import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Mob extends Sprites implements Serializable {

    private double lastTimeMoved;
    private int attackRange;
    private double lastTimeAttacked;
    private double attackFrequency;
    private int rageRadius;
    private boolean rage = false;
    private final TETile naturalTile;
    private final Random rand = new Random();
    private final String[] DIRECTIONS = {"up", "down", "left", "right"};

    private final double chanceT1 = 0.012;
    private final double chanceT2 = 0.016;
    private final double chanceT3 = 0.02;

    public Mob(World world) {
        this.naturalTile = generateMobAppearance();
        this.tileType = this.naturalTile;
        this.tileType = generateMobAppearance();
        if (this.getTile().equals(Tileset.MOUNTAIN)) {
            this.speed = 0.3;
            this.health = 6;
            this.damage = 2;
            this.attackRange = 1;
            this.attackFrequency = 0.2;
            this.rageRadius = 5;
            this.type = "Hostile";
        } else if (this.tileType.equals(Tileset.FLOWER)) {
            this.speed = 0.5;
            this.health = 8;
            this.damage = 3;
            this.attackRange = 1;
            this.attackFrequency = 0.3;
            this.rageRadius = 6;
            this.type = "Neutral";
        } else if (this.tileType.equals(Tileset.GRASS)) {
            this.speed = 0.7;
            this.health = 10;
            this.damage = 5;
            this.attackRange = 2;
            this.attackFrequency = 0.5;
            this.rageRadius = 7;
            this.type = "Passive";
        }
        this.world = world;
        this.worldAvatar = world.getAvatar();
        this.x = -1;
        this.y = -1;
    }

    public boolean getRage() {
        return this.rage;
    }

    public void getBuffed() {
        if (this.tileType.equals(Tileset.MOUNTAIN)) {
            this.speed = 0.2;
            this.health = 8;
            this.damage = 3;
            this.attackRange = 1;
            this.attackFrequency = 0.2;
            this.rageRadius = 20;
            this.type = "Hella Hostile";
        } else if (this.tileType.equals(Tileset.FLOWER)) {
            this.speed = 0.35;
            this.health = 10;
            this.damage = 4;
            this.attackRange = 1;
            this.attackFrequency = 0.3;
            this.rageRadius = 30;
            this.type = "Not so Neutral";
        } else if (this.tileType.equals(Tileset.GRASS)) {
            this.speed = 0.5;
            this.health = 10;
            this.damage = 5;
            this.attackRange = 2;
            this.attackFrequency = 0.5;
            this.rageRadius = 40;
            this.type = "Not so Passive";
        }
    }

    public void getUnbuffed() {
        if (this.tileType.equals(Tileset.ENRAGED_MOB3)) {
            this.speed = 0.3;
            this.health = 6;
            this.damage = 2;
            this.attackRange = 1;
            this.attackFrequency = 0.2;
            this.rageRadius = 5;
            this.type = "Hostile";
        } else if (this.tileType.equals(Tileset.ENRAGED_MOB2)) {
            this.speed = 0.5;
            this.health = 8;
            this.damage = 3;
            this.attackRange = 1;
            this.attackFrequency = 0.3;
            this.rageRadius = 6;
            this.type = "Neutral";
        } else if (this.tileType.equals(Tileset.ENRAGED_MOB1)) {
            this.speed = 0.7;
            this.health = 10;
            this.damage = 5;
            this.attackRange = 2;
            this.attackFrequency = 0.5;
            this.rageRadius = 7;
            this.type = "Passive";
        }
    }

    public String getAvatarDirection() {
        HashMap<ArrayList<Integer>, String> coordsToDirection = new HashMap<>();
        if (world.getWorldArray()[x][y + 1].getTileType() == TETile.TileType.FLOOR || world.getWorldArray()[x][y + 1].getTileType() == world.getAvatar().getTile().getTileType()) {
            ArrayList<Integer> adjTileCoords = new ArrayList<>();
            adjTileCoords.add(x);
            adjTileCoords.add(y + 1);
            coordsToDirection.put(adjTileCoords, "up");
        }
        if (world.getWorldArray()[x][y - 1].getTileType() == TETile.TileType.FLOOR || world.getWorldArray()[x][y - 1].getTileType() == world.getAvatar().getTile().getTileType()) {
            ArrayList<Integer> adjTileCoords = new ArrayList<>();
            adjTileCoords.add(x);
            adjTileCoords.add(y - 1);
            coordsToDirection.put(adjTileCoords, "down");
        }
        if (world.getWorldArray()[x + 1][y].getTileType() == TETile.TileType.FLOOR || world.getWorldArray()[x + 1][y].getTileType() == world.getAvatar().getTile().getTileType()) {
            ArrayList<Integer> adjTileCoords = new ArrayList<>();
            adjTileCoords.add(x + 1);
            adjTileCoords.add(y);
            coordsToDirection.put(adjTileCoords, "right");
        }
        if (world.getWorldArray()[x - 1][y].getTileType() == TETile.TileType.FLOOR || world.getWorldArray()[x - 1][y].getTileType() == world.getAvatar().getTile().getTileType()) {
            ArrayList<Integer> adjTileCoords = new ArrayList<>();
            adjTileCoords.add(x - 1);
            adjTileCoords.add(y);
            coordsToDirection.put(adjTileCoords, "left");
        }
        if (coordsToDirection.isEmpty()) {
            return "up";
        } else {
            ArrayList<Integer> min = coordsToDirection.keySet().stream().findAny().get(); // randomly gets alist from hashmap
            for (ArrayList<Integer> lst : coordsToDirection.keySet()) {
                if (distanceToAvatar(world.getAvatar(), lst.get(0), lst.get(1)) < distanceToAvatar(world.getAvatar(), min.get(0), min.get(1))) {
                    min = lst;
                }
            }
            return coordsToDirection.get(min);
        }
    }

    private int distanceToAvatar(Avatar avatar) {
        int dx = Math.abs(x - avatar.x);
        int dy = Math.abs(y - avatar.y);
        return (int) (Math.sqrt((Math.pow(dx, 2) + Math.pow(dy, 2))));
    }

    private double distanceToAvatar(Avatar avatar, int x, int y) {
        int dx = Math.abs(x - avatar.x);
        int dy = Math.abs(y - avatar.y);
        return Math.sqrt((Math.pow(dx, 2) + Math.pow(dy, 2)));
    }


    public boolean timeToMove(double time) {
        if (this.rage) {
            if (time - lastTimeMoved > this.speed) {
                return true;
            }
        } else if (time - lastTimeMoved > this.speed) {
            return true;
        }
        return false;
    }

    public void timeToEnrage() {
        if (distanceToAvatar(this.world.getAvatar()) <= this.rageRadius) {
            this.rage = true;
            if (this.tileType.equals(Tileset.MOUNTAIN)) {
                this.tileType = Tileset.ENRAGED_MOB3;
            } else if (this.tileType.equals(Tileset.FLOWER)) {
                this.tileType = Tileset.ENRAGED_MOB2;
            } else if (this.tileType.equals(Tileset.GRASS)) {
                this.tileType = Tileset.ENRAGED_MOB1;
            }
        } else {
            this.rage = false;
            this.tileType = this.naturalTile;
        }
    }


    public TETile generateMobAppearance() {
        int tileNum = rand.nextInt(3);
        return switch (tileNum) {
            case 0 -> Tileset.MOUNTAIN;
            case 1 -> Tileset.FLOWER;
            case 2 -> Tileset.GRASS;
            default -> Tileset.NOTHING;
        };
//        return Tileset.MOUNTAIN;
    }

    @Override
    public void setLocation() {
        this.world.spawnMob(this);
    }

    @Override
    public void move(String d) {
        TETile[][] worldArray = this.world.getWorldArray();
        boolean moved = false;
        if (d.equals("")) {
            int randomDir = RandomUtils.uniform(rand, DIRECTIONS.length);
            d = DIRECTIONS[randomDir];
        }
        int prevX = this.x;
        int prevY = this.y;
        switch (d) {
            case "up" -> {
                if (worldArray[x][y + 1].getTileType() == TETile.TileType.FLOOR) {
                    y++;
                    moved = true;
                }
            }
            case "down" -> {
                if (worldArray[x][y - 1].getTileType() == TETile.TileType.FLOOR) {
                    y--;
                    moved = true;
                }
            }
            case "left" -> {
                if (worldArray[x - 1][y].getTileType() == TETile.TileType.FLOOR) {
                    x--;
                    moved = true;
                }
            }
            case "right" -> {
                if (worldArray[x + 1][y].getTileType() == TETile.TileType.FLOOR) {
                    x++;
                    moved = true;
                }
            }
        }
        worldArray[x][y] = this.getTile();
        if (moved) {
            worldArray[prevX][prevY] = Tileset.FLOOR;
        }
        lastTimeMoved = this.world.sw.elapsedTime();
    }

    @Override
    public void dealDamage(int damage) {
        if (worldAvatar != null) {
            Avatar avatar = (Avatar) this.worldAvatar;
            if (Math.abs(this.x - avatar.x) <= this.attackRange && Math.abs(this.y - avatar.y) <= this.attackRange && this.world.sw.elapsedTime() - lastTimeAttacked > this.attackFrequency) {
                avatar.loseHealth(damage);
                this.lastTimeAttacked = this.world.sw.elapsedTime();
            }
        }
    }

    @Override
    public void loseHealth(int damage) {
        this.health -= damage;
    }

}
