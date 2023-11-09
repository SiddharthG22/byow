package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import org.antlr.v4.runtime.misc.Utils;

import java.io.File;
import java.util.Random;

import static org.antlr.v4.runtime.misc.Utils.join;

public class WorldCreation {

    public static final File CWD = new File(System.getProperty("user.dir"));

    public static final File WORLD_DIR = RandomUtils.join(CWD, ".world");
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;

    public static long SEED = 123130;

    public static final int MAX_ROOM_SIZE = 12;

    public static final int MAX_HALLWAY_LENGTH = 7;

    public static final int ChanceOfHallway = 45;

    public static final TETile WALL_TILE = Tileset.WALL;
    public static final TETile FLOOR_TILE = Tileset.FLOOR;

    public static final TETile NOTHING = Tileset.NOTHING;

    private static Random RANDOM = new Random(SEED);

    public static void main (String[] args) {
        testNumRooms(100);

    }

    public static World loadWorld() {
        File newFile = RandomUtils.join(WorldCreation.CWD, "game.txt");
        if (!newFile.exists()) {
            System.exit(0);
        }
        World world = RandomUtils.readObject(newFile, World.class);
        return world;
    }

    public static void testNumRooms(int iterations) {
        //tests the number of floor tiles per map on average
        int[] floor_array = new int[iterations];
        for (int i = 0; i < iterations; i++) {
            TETile[][] world = new TETile[WIDTH][HEIGHT];
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    world[x][y] = Tileset.NOTHING;
                }
            }
            drawRoom(world, 10, 10, WIDTH/2 + 4, HEIGHT/2, "left");
            int floorCount = 0;
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    if (world[x][y].getTileType() == TETile.TileType.FLOOR) {
                        floorCount ++;
                    }
                }
            }
            floor_array[i] = floorCount;
        }
        int sum = 0;
        for (int i = 0; i < iterations; i++) {
            sum += floor_array[i];
        }
        int avg = sum/iterations;
        System.out.print(avg);
    }

    public static World createWorld(long seed) {
        SEED = seed;
        RANDOM = new Random(SEED);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        drawRoom(world, 10, 10, WIDTH/2 + 4, HEIGHT/2, "left");
        return new World(world, seed);
    }

    public static boolean drawRoom(TETile[][] world, int width, int height, int x, int y, String direction) {
        //the x, y parameters (location) is the starting point from which the room is drawn
        switch (direction) {
            case "up":
                x = x - RANDOM.nextInt(width - 2) - 1;
                break;
            case "down":
                x = x - RANDOM.nextInt(width - 2) - 1;
                y = y - height + 1;
                break;
            case "left":
                y = y - RANDOM.nextInt(height - 2) - 1;
                x = x - width + 1;
                break;
            case "right":
                y = y - RANDOM.nextInt(height - 2) - 1;
                break;
        }
        int finalX = x;
        int finalY = y;

        //check for validity (no obstructions and within boundaries of world)
        if (!isValidRoom(world, width, height, x, y)) {
            return false;
        }

        //draw floor
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }

        // draw perimeter
        int i = x;
        int j = y;
        while (i < x + width) {
            world[i][j] = Tileset.WALL;
            i++;
        }
        i--;
        while (j < y + height) {
            world[i][j] = Tileset.WALL;
            j++;
        }
        j--;
        while (i >= x) {
            world[i][j] = Tileset.WALL;
            i--;
        }
        i++;
        while (j >= y) {
            world[i][j] = Tileset.WALL;
            j--;
        }
        chainRoom(world, width, height, finalX, finalY);
        return true;

    }

    public static boolean isValidRoom(TETile[][] world, int width, int height, int x, int y) {
        if (x >= 0 && y >= 0 && x + width < world.length && y + height < world[0].length) {
            //room exists in a space contains within the frame and is at least 3 by 3
            int obstructions = 0;
            for (int i = x; i < x + width; i++) {
                for (int j = y; j < y + height; j++) {
                    // only 3 tiles can be filled (these are the tiles from the hallway)
                    if (world[i][j].getTileType() != TETile.TileType.VOID) {
                        obstructions ++;
                    }
                }
            }
            if (obstructions <= 3) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean drawHallway(TETile[][] world, int length, int x, int y, String direction) {
        if (!isValidHallway(world, length, x, y, direction)) {
            return false;
        }
        switch (direction) {
            case "up":
                for (int i = y; i < y + length; i++) {
                    world[x - 1][i] = Tileset.WALL;
                    world[x][i] = Tileset.FLOOR;
                    world[x + 1][i] = Tileset.WALL;
                }
                break;

            case "down":
                for (int i = y; i > y - length; i--) {
                    world[x - 1][i] = Tileset.WALL;
                    world[x][i] = Tileset.FLOOR;
                    world[x + 1][i] = Tileset.WALL;
                }
                break;


            case "left":
                for (int i = x; i > x - length; i--) {
                    world[i][y - 1] = Tileset.WALL;
                    world[i][y] = Tileset.FLOOR;
                    world[i][y + 1] = Tileset.WALL;
                }
                break;

            case "right":
                for (int i = x; i < x + length; i++) {
                    world[i][y - 1] = Tileset.WALL;
                    world[i][y] = Tileset.FLOOR;
                    world[i][y + 1] = Tileset.WALL;
                }
                break;
        }
        switch (direction){
            case "up":
                chainHallway(world, x, y + length - 1, direction);
                break;
            case "down":
                chainHallway(world, x, y - length + 1, direction);
                break;
            case "left":
                chainHallway(world, x - length + 1, y, direction);
                break;
            case "right":
                chainHallway(world, x + length - 1, y, direction);
                break;
        }
        return true;
    }

    public static boolean isValidHallway(TETile[][] world, int length, int x, int y, String direction) {
        switch (direction) {
            case "up":
                if (x >= 1 && y >= 1 && x < world.length - 1 && y + length < world[0].length - 1 && length > 1) {
                    for (int i = y + 1; i < y + length; i++) {
                        if (world[x - 1][i].getTileType() != TETile.TileType.VOID || world[x][i].getTileType() != TETile.TileType.VOID || world[x + 1][i].getTileType() != TETile.TileType.VOID) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            case "down":
                if (x >= 1 && y - length >= 1 && x < world.length - 1 && y < world[0].length - 1  && length > 1) {
                    for (int i = y - 1; i > y - length; i--) {
                        if (world[x - 1][i].getTileType() != TETile.TileType.VOID || world[x][i].getTileType() != TETile.TileType.VOID || world[x + 1][i].getTileType() != TETile.TileType.VOID) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            case "left":
                if (x - length >= 1 && y >= 1 && x < world.length - 1 && y < world[0].length - 1) {
                    for (int i = x - 1; i > x - length; i--) {
                        if (world[i][y - 1].getTileType() != TETile.TileType.VOID || world[i][y].getTileType() != TETile.TileType.VOID || world[i][y + 1].getTileType() != TETile.TileType.VOID) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;

            case "right":
                if (x >= 1 && y >= 1 && x + length < world.length - 1 && y < world[0].length - 1  && length > 1) {
                    for (int i = x + 1; i < x + length; i++) {
                        if (world[i][y - 1].getTileType() != TETile.TileType.VOID || world[i][y].getTileType() != TETile.TileType.VOID || world[i][y + 1].getTileType() != TETile.TileType.VOID) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
        }

        return false;
    }

    public static boolean drawRandomRoom(TETile[][] world, int x, int y, String direction) {
        int width = RANDOM.nextInt(MAX_ROOM_SIZE);
        int height = RANDOM.nextInt(MAX_ROOM_SIZE);
        if (width < 3) {
            width = 3;
        }
        if (height < 3) {
            height = 3;
        }
        if (!drawRoom(world, width, height, x, y, direction)) {
            return false;
        }
        return true;
    }

    public static void drawRandomHallway(TETile[][] world, int x, int y, String direction) {
        int length = RANDOM.nextInt(MAX_HALLWAY_LENGTH);
        if (length < 2) {
            length = 2;
        }
        drawHallway(world, length, x, y, direction);

    }

    public static void chainRoom(TETile[][] world, int width, int height, int x, int y) {
        //potentially make a call (or calls) to draw random hallway

        //traverse through perimeter
        int i = x + 1;
        int j = y;
        while (i < x + width - 1) {
            if (RANDOM.nextInt(100) < ChanceOfHallway) {
                drawRandomHallway(world, i, j, "down");
                break;
            }
            i++;
        }
        i = x + width - 1;
        j = y + 1;
        while (j < y + height - 1) {
            if (RANDOM.nextInt(100) < ChanceOfHallway) {
                drawRandomHallway(world, i, j, "right");
                break;
            }
            j++;
        }
        j = y + height - 1;
        i = x + width - 2;
        while (i >= x + 1) {
            if (RANDOM.nextInt(100) < ChanceOfHallway) {
                drawRandomHallway(world, i, j, "up");
                break;
            }
            i--;
        }
        i = x;
        j = y + height - 2;
        while (j >= y + 1) {
            if (RANDOM.nextInt(100) < ChanceOfHallway) {
                drawRandomHallway(world, i, j, "left");
                break;
            }
            j--;
        }

    }

    public static void chainHallway(TETile[][] world, int x, int y, String direction) {
        //potentially make a call to draw random room
        if (drawRandomRoom(world, x, y, direction)) {
            world[x][y] = Tileset.FLOOR;
        } else {
            world[x][y] = Tileset.WALL;
        }

    }
}
