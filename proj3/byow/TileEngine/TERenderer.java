package byow.TileEngine;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }
        StdDraw.show();
    }
}


//    public TETile[][] displayWorld(World world) {
//        TETile[][] rawWorld = world.getWorldArray();
//        int avaX = world.getAvatar().getX();
//        int avaY = world.getAvatar().getY();
//        TETile[][] displayWorld = new TETile[WIDTH][HEIGHT];
//
//        for (int x = 0; x < WIDTH; x++) {
//            for (int y = 0; y < HEIGHT; y++) {
//                int worldX = avaX - WIDTH / 2 + x;
//                int worldY = avaY - HEIGHT / 2 + y;
//                if (worldX >= 0 && worldX < rawWorld.length && worldY >= 0 && worldY < rawWorld[0].length) {
//                    if (!lineOfSightEnabled || Math.abs(avaX - worldX) + Math.abs(avaY - worldY) <= LINE_OF_SIGHT_DISTANCE) {
//                        displayWorld[x][y] = rawWorld[worldX][worldY];
//                    } else {
//                        displayWorld[x][y] = Tileset.NOTHING;
//                    }
//                } else {
//                    displayWorld[x][y] = Tileset.NOTHING;
//                }
//            }
//        }
//        for (Mob m : world.getMobs()) {
//            m.timeToEnrage();
//            if (m.timeToMove(world.sw.elapsedTime())) {
//                if (m.getRage()) {
//                    m.move(m.getAvatarDirection());
//                } else {
//                    m.move("");
//                }
//            }
//            if (m.getRage()) {
//                m.dealDamage(m.getDamage());
//            }
//        }
//        if (world.getAvatar().shotFired) {
//            if (world.getAvatar().timeToShoot(world.sw.elapsedTime())) {
//                TETile[][] worldArr = world.getWorldArray();
//                int xC = world.getAvatar().bulletX;
//                int yC = world.getAvatar().bulletY;
//                switch (world.getAvatar().shootingDirection) {
//                    case "up" -> {
//                        if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY + 1] == Tileset.FLOOR) {
//                            world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY + 1] = Tileset.BULLET;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                            world.getAvatar().bulletY++;
//                            world.getAvatar().lastTimeShot = world.sw.elapsedTime();
//                        } else if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY + 1].getTileType() == TETile.TileType.MOB) {
//                            world.getAvatar().shotFired = false;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                            for (int i = 0; i < world.getMobs().size(); i++) {
//                                if (world.getMobs().get(i).getX() == world.getAvatar().bulletX && world.getMobs().get(i).getY() == world.getAvatar().bulletY + 1) {
//                                    world.getMobs().get(i).loseHealth(world.getAvatar().getDamage());
//                                    if (world.getMobs().get(i).getHealth() <= 0) {
//                                        world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY + 1] = Tileset.FLOOR;
//                                        world.getMobs().remove(i);
//                                        break;
//                                    }
//                                }
//                            }
//                        } else {
//                            world.getAvatar().shotFired = false;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                        }
//                    }
//                    case "down" -> {
//                        if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY - 1] == Tileset.FLOOR) {
//                            world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY - 1] = Tileset.BULLET;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                            world.getAvatar().bulletY--;
//                            world.getAvatar().lastTimeShot = world.sw.elapsedTime();
//                        } else if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY - 1].getTileType() == TETile.TileType.MOB) {
//                            world.getAvatar().shotFired = false;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                            for (int i = 0; i < world.getMobs().size(); i++) {
//                                if (world.getMobs().get(i).getX() == world.getAvatar().bulletX && world.getMobs().get(i).getY() == world.getAvatar().bulletY - 1) {
//                                    world.getMobs().get(i).loseHealth(world.getAvatar().getDamage());
//                                    if (world.getMobs().get(i).getHealth() <= 0) {
//                                        world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY - 1] = Tileset.FLOOR;
//                                        world.getMobs().remove(i);
//                                        break;
//                                    }
//                                }
//                            }
//                        } else {
//                            world.getAvatar().shotFired = false;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                        }
//                    }
//                    case "left" -> {
//                        if (world.getWorldArray()[world.getAvatar().bulletX - 1][world.getAvatar().bulletY] == Tileset.FLOOR) {
//                            world.getWorldArray()[world.getAvatar().bulletX - 1][world.getAvatar().bulletY] = Tileset.BULLET;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                            world.getAvatar().bulletX--;
//                            world.getAvatar().lastTimeShot = world.sw.elapsedTime();
//                        } else if (world.getWorldArray()[world.getAvatar().bulletX - 1][world.getAvatar().bulletY].getTileType() == TETile.TileType.MOB) {
//                            world.getAvatar().shotFired = false;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                            for (int i = 0; i < world.getMobs().size(); i++) {
//                                if (world.getMobs().get(i).getX() == world.getAvatar().bulletX - 1 && world.getMobs().get(i).getY() == world.getAvatar().bulletY) {
//                                    world.getMobs().get(i).loseHealth(world.getAvatar().getDamage());
//                                    if (world.getMobs().get(i).getHealth() <= 0) {
//                                        world.getWorldArray()[world.getAvatar().bulletX - 1][world.getAvatar().bulletY] = Tileset.FLOOR;
//                                        world.getMobs().remove(i);
//                                        break;
//                                    }
//                                }
//                            }
//                        } else {
//                            world.getAvatar().shotFired = false;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                        }
//                    }
//                    case "right" -> {
//                        if (world.getWorldArray()[world.getAvatar().bulletX + 1][world.getAvatar().bulletY] == Tileset.FLOOR) {
//                            world.getWorldArray()[world.getAvatar().bulletX + 1][world.getAvatar().bulletY] = Tileset.BULLET;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                            world.getAvatar().bulletX++;
//                            world.getAvatar().lastTimeShot = world.sw.elapsedTime();
//                        } else if (world.getWorldArray()[world.getAvatar().bulletX + 1][world.getAvatar().bulletY].getTileType() == TETile.TileType.MOB) {
//                            world.getAvatar().shotFired = false;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                            for (int i = 0; i < world.getMobs().size(); i++) {
//                                if (world.getMobs().get(i).getX() == world.getAvatar().bulletX + 1 && world.getMobs().get(i).getY() == world.getAvatar().bulletY) {
//                                    world.getMobs().get(i).loseHealth(world.getAvatar().getDamage());
//                                    if (world.getMobs().get(i).getHealth() <= 0) {
//                                        world.getWorldArray()[world.getAvatar().bulletX + 1][world.getAvatar().bulletY] = Tileset.FLOOR;
//                                        world.getMobs().remove(i);
//                                        break;
//                                    }
//                                }
//                            }
//                        } else {
//                            world.getAvatar().shotFired = false;
//                            if (world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] != Tileset.AVATAR) {
//                                world.getWorldArray()[world.getAvatar().bulletX][world.getAvatar().bulletY] = Tileset.FLOOR;
//                            }
//                        }
//                    }
//                    default -> {
//                        // nothing here
//                    }
//                }
//            }
//        }
