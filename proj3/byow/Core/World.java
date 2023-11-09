package byow.Core;

import byow.Characters.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static byow.Core.Engine.WIDTH;

public class World implements Serializable {

    private TETile[][] worldArray;
    public StopWatch sw;
    public double lastTimeRaged;

    private final double chanceKey = 0.012;
    private final double MAX_KEYS = 24;
    private final int MAX_MOBS = 80;

    public long prevTime;
    public boolean enraged = false;
    public Character gameMode;

    public long seed;
    public Random rand;

    private final Avatar avatar;
    private final ArrayList<Mob> mobs;
    private final ArrayList<Item> items;
    private final ArrayList<Key> keys;

    private final HashMap<String, ArrayList<Mob>> allMobs;

    public World(TETile[][] worldArray, long seed) {
        this.worldArray = worldArray;
        this.seed = seed;
        this.rand = new Random(seed);
        this.avatar = new Avatar(this);
        this.sw = new StopWatch();
        this.gameMode = 'e';

        this.mobs = new ArrayList<>();
        this.items = new ArrayList<>();
        this.keys = new ArrayList<>();
        this.allMobs = new HashMap<>();

        for (int i = 0; i < MAX_MOBS; i++) {
            Mob m = new Mob(this);
            spawnMob(m);
            if (m.x != -1 && m.y != -1) {
                this.mobs.add(m);
            }
        }
        spawnKeys();
        spawnItems();
    }

    public TETile[][] getWorldArray() {
        return this.worldArray;
    }

    public Avatar getAvatar() {
        return this.avatar;
    }

    public ArrayList<Mob> getMobs() {
        return this.mobs;
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    public ArrayList<Key> getKeys() {
        return this.keys;
    }

    public void buffStuff() {
        this.lastTimeRaged = this.sw.elapsedTime();
        this.enraged = true;
        for (Mob m : this.mobs) {
            m.getBuffed();
        }
        for (Item i : this.items) {
            i.buffItem();
        }
    }

    public void unbuffStuff() {
        this.enraged = false;
        for (Mob m : this.mobs) {
            m.getUnbuffed();
        }
        for (Item i : this.items) {
            i.unbuffItem();
        }
    }

    public void spawnMob(Mob m) {
        TETile[][] worldArray = this.getWorldArray();
        int quadrant = rand.nextInt(4); // randomly get a quadrant
        switch (quadrant) {
            case 0 -> {
                int randX = rand.nextInt(0, worldArray.length / 2 - 5);
                int randY = rand.nextInt(0, worldArray[0].length / 2 - 5);
                if (worldArray[randX][randY].getTileType() == TETile.TileType.FLOOR) {
                    m.tileType = m.generateMobAppearance();
                    m.x = randX;
                    m.y = randY;
                    worldArray[randX][randY] = m.getTile();
                }
            }
            case 1 -> {
                int randX = rand.nextInt(worldArray.length / 2 + 5, worldArray.length);
                int randY = rand.nextInt(0, worldArray[0].length / 2 - 5);
                if (worldArray[randX][randY].getTileType() == TETile.TileType.FLOOR) {
                    m.x = randX;
                    m.y = randY;
                    worldArray[randX][randY] = m.getTile();
                }
            }
            case 2 -> {
                int randX = rand.nextInt(0, worldArray.length / 2 - 5);
                int randY = rand.nextInt(worldArray[0].length / 2 + 5, worldArray[0].length);
                if (worldArray[randX][randY].getTileType() == TETile.TileType.FLOOR) {
                    m.x = randX;
                    m.y = randY;
                    worldArray[randX][randY] = m.getTile();
                }
            }
            case 3 -> {
                int randX = rand.nextInt(worldArray.length / 2 + 5, worldArray.length);
                int randY = rand.nextInt(worldArray[0].length / 2 + 5, worldArray[0].length);
                if (worldArray[randX][randY].getTileType() == TETile.TileType.FLOOR) {
                    m.tileType = m.generateMobAppearance();
                    m.x = randX;
                    m.y = randY;
                    worldArray[randX][randY] = m.getTile();
                }
            }
            default -> {
                // nothing happens here
            }
        }
    }


    public void spawnKeys() {
        int keys = 0;
        for (int x = 0; x < worldArray.length / 2; x++) {
            for (int y = 0; y < worldArray[0].length / 2; y++) {
                if (worldArray[x][y].getTileType() == TETile.TileType.FLOOR) {
                    if (keys < MAX_KEYS / 8 && rand.nextDouble() < chanceKey) {
                        Item k = new Key(x, y, this);
                        this.items.add(k);
                        this.keys.add((Key) k);
                        worldArray[x][y] = k.tileType;
                        keys++;
                    }
                }
            }
        }
        keys = 0;
        for (int x = worldArray.length - 1; x >= worldArray.length / 2; x--) {
            for (int y = 0; y < worldArray[0].length / 2; y++) {
                if (worldArray[x][y].getTileType() == TETile.TileType.FLOOR) {
                    if (keys < MAX_KEYS / 8 && rand.nextDouble() < chanceKey) {
                        Item k = new Key(x, y, this);
                        this.items.add(k);
                        this.keys.add((Key) k);
                        worldArray[x][y] = k.tileType;
                        keys++;
                    }
                }
            }
        }
        keys = 0;
        for (int x = worldArray.length - 1; x >= worldArray.length / 2; x--) {
            for (int y = worldArray[0].length - 1; y >= worldArray[0].length / 2; y--) {
                if (worldArray[x][y].getTileType() == TETile.TileType.FLOOR) {
                    if (keys < MAX_KEYS / 8 && rand.nextDouble() < chanceKey) {
                        Item k = new Key(x, y, this);
                        this.items.add(k);
                        this.keys.add((Key) k);
                        worldArray[x][y] = k.tileType;
                        keys++;
                    }
                }
            }
        }
        keys = 0;
        for (int x = 0; x < worldArray.length / 2; x++) {
            for (int y = worldArray[0].length - 1; y >= worldArray[0].length / 2; y--) {
                if (worldArray[x][y].getTileType() == TETile.TileType.FLOOR) {
                    if (keys < MAX_KEYS / 8 && rand.nextDouble() < chanceKey) {
                        Item k = new Key(x, y, this);
                        this.items.add(k);
                        this.keys.add((Key) k);
                        worldArray[x][y] = k.tileType;
                        keys++;
                    }
                }
            }
        }
    }

    public void spawnItems() {
        int keys = 0;
        for (int x = 0; x < worldArray.length / 2; x++) {
            for (int y = 0; y < worldArray[0].length / 2; y++) {
                if (worldArray[x][y].getTileType() == TETile.TileType.FLOOR) {
                    if (keys < MAX_KEYS / 4 && rand.nextDouble() < chanceKey) {
                        int randTileType = rand.nextInt(3);
                        switch (randTileType) {
                            case 0 -> {
                                HealthPod k = new HealthPod(x, y, this);
                                this.items.add(k);
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                            case 1 -> {
                                Ammunition k = new Ammunition(x, y, this);
                                this.items.add(k);
//                                this.getAvatar().ammo += k.rounds;
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                            case 2 -> {
                                PowerUps k = new PowerUps(x, y, this);
                                this.items.add(k);
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                        }
                        break;
                    }
                }
            }
        }
        keys = 0;
        for (int x = worldArray.length - 1; x >= worldArray.length / 2; x--) {
            for (int y = 0; y < worldArray[0].length / 2; y++) {
                if (worldArray[x][y].getTileType() == TETile.TileType.FLOOR) {
                    if (keys < MAX_KEYS / 4 && rand.nextDouble() < chanceKey) {
                        int randTileType = rand.nextInt(3);
                        switch (randTileType) {
                            case 0 -> {
                                HealthPod k = new HealthPod(x, y, this);
                                this.items.add(k);
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                            case 1 -> {
                                Ammunition k = new Ammunition(x, y, this);
                                this.items.add(k);
//                                this.getAvatar().ammo += k.rounds;
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                            case 2 -> {
                                PowerUps k = new PowerUps(x, y, this);
                                this.items.add(k);
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                        }
                        break;
                    }
                }
            }
        }
        keys = 0;
        for (int x = worldArray.length - 1; x >= worldArray.length / 2; x--) {
            for (int y = worldArray[0].length - 1; y >= worldArray[0].length / 2; y--) {
                if (worldArray[x][y].getTileType() == TETile.TileType.FLOOR) {
                    if (keys < MAX_KEYS / 4 && rand.nextDouble() < chanceKey) {
                        int randTileType = rand.nextInt(3);
                        switch (randTileType) {
                            case 0 -> {
                                Item k = new HealthPod(x, y, this);
                                this.items.add(k);
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                            case 1 -> {
                                Ammunition k = new Ammunition(x, y, this);
                                this.items.add(k);
//                                this.getAvatar().ammo += k.rounds;
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                            case 2 -> {
                                Item k = new PowerUps(x, y, this);
                                this.items.add(k);
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                        }
                    }
                }
            }
        }
        keys = 0;
        for (int x = 0; x < worldArray.length / 2; x++) {
            for (int y = worldArray[0].length - 1; y >= worldArray[0].length / 2; y--) {
                if (worldArray[x][y].getTileType() == TETile.TileType.FLOOR) {
                    if (keys < MAX_KEYS / 4 && rand.nextDouble() < chanceKey) {
                        int randTileType = rand.nextInt(3);
                        switch (randTileType) {
                            case 0 -> {
                                Item k = new HealthPod(x, y, this);
                                this.items.add(k);
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                            case 1 -> {
                                Ammunition k = new Ammunition(x, y, this);
                                this.items.add(k);
//                                this.getAvatar().ammo += k.rounds;
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                            case 2 -> {
                                Item k = new PowerUps(x, y, this);
                                this.items.add(k);
                                worldArray[x][y] = k.tileType;
                                keys++;
                            }
                        }
                    }
                }
            }
        }
    }

    public TETile[][] disp(TETile[][] displayWorld, int w, int h) {
        int magicNumber = 10;
        int f2 = 2;

        displayWorld[w - 4][h-2] = Tileset.NUMBER_TILES[((this.getAvatar().health % (int) Math.pow(magicNumber, 3) - this.getAvatar().health % (int) Math.pow(magicNumber, 2)) / (int) Math.pow(magicNumber, 2))];
        displayWorld[w - 3][h-2] = Tileset.NUMBER_TILES[((this.getAvatar().health % (int) Math.pow(magicNumber, 2) - this.getAvatar().health % (int) Math.pow(magicNumber, 1)) / (int) Math.pow(magicNumber, 1))];
        displayWorld[w - 2][h-2] = Tileset.NUMBER_TILES[Math.abs((this.getAvatar().health % magicNumber))];
        for (int i = 0; i < this.getAvatar().getHealth() / 5; i++) {
            displayWorld[w + i][h-2] = Tileset.AVATAR_HEALTH;
        }
        displayWorld[f2][h-2] = Tileset.AVATAR_AMMO;
        displayWorld[f2 + 2][h-2] = Tileset.NUMBER_TILES[((this.getAvatar().ammo % (int) Math.pow(magicNumber, 3) - this.getAvatar().ammo % (int) Math.pow(magicNumber, 2)) / (int) Math.pow(magicNumber, 2))];
        displayWorld[f2 + 3][h-2] = Tileset.NUMBER_TILES[((this.getAvatar().ammo % (int) Math.pow(magicNumber, 2) - this.getAvatar().ammo % (int) Math.pow(magicNumber, 1)) / (int) Math.pow(magicNumber, 1))];
        displayWorld[f2 + 4][h-2] = Tileset.NUMBER_TILES[(this.getAvatar().ammo % magicNumber)];

        displayWorld[WIDTH - 5][h-2] = Tileset.GAME_KEYS;
        displayWorld[WIDTH - 3][h-2] = Tileset.NUMBER_TILES[(this.getKeys().size() / (int) Math.pow(magicNumber, 1))];
        displayWorld[WIDTH - 2][h-2] = Tileset.NUMBER_TILES[(this.getKeys().size() % (int) Math.pow(magicNumber, 1))];

        displayWorld[WIDTH - 7][2] = Tileset.GAME_TIME;
        displayWorld[WIDTH - 5][2] = Tileset.NUMBER_TILES[(int) ((this.sw.elapsedTime() % (int) Math.pow(magicNumber, 4) - this.sw.elapsedTime() % (int) Math.pow(magicNumber, 3)) / (int) Math.pow(magicNumber, 3))];
        displayWorld[WIDTH - 4][2] = Tileset.NUMBER_TILES[(int) ((this.sw.elapsedTime() % (int) Math.pow(magicNumber, 3) - this.sw.elapsedTime() % (int) Math.pow(magicNumber, 2)) / (int) Math.pow(magicNumber, 2))];
        displayWorld[WIDTH - 3][2] = Tileset.NUMBER_TILES[(int) ((this.sw.elapsedTime() % (int) Math.pow(magicNumber, 2) - this.sw.elapsedTime() % (int) Math.pow(magicNumber, 1)) / (int) Math.pow(magicNumber, 1))];
        displayWorld[WIDTH - 2][2] = Tileset.NUMBER_TILES[(int) (this.sw.elapsedTime() % (int) Math.pow(magicNumber, 1))];

        displayWorld[2][2] = Tileset.QUESTION;
        return displayWorld;
    }



    public void saveWorld() {
//        this.itemsInfo.put("itemInfo", items);
//        this.keysInfo.put("keyInfo", keys);
        this.allMobs.put("mobInfo", mobs);
        this.prevTime = (long) this.sw.elapsedTime();
        File newFile = RandomUtils.join(WorldCreation.CWD, "game.txt");
        RandomUtils.writeObject(newFile, this);
    }

}
