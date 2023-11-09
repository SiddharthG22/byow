package byow.TileEngine;

import byow.Core.Engine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.z
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('♛', Color.black, Color.white, "avatar", TETile.TileType.MISC);
    public static final TETile WALL = new TETile(' ', new Color(216, 128, 128), Color.darkGray, "wall", TETile.TileType.WALL);
    public static final TETile FLOOR = new TETile('·', Color.white, new Color(138, 185, 241), "floor", TETile.TileType.FLOOR);
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing", TETile.TileType.VOID);
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black, "locked door", TETile.TileType.FLOOR);
    public static final TETile BULLET = new TETile('·', Color.black, FLOOR.getBackgroundColor(), "bullet", TETile.TileType.MISC);

    public static final TETile SAND = new TETile('█', new Color(255, 165, 0), Color.yellow, "key", TETile.TileType.FLOOR);
    public static final TETile WATER = new TETile('☤', Color.red, AVATAR.getBackgroundColor(), "health boost", TETile.TileType.FLOOR);
    public static final TETile UNLOCKED_DOOR = new TETile('⁍', Color.blue, AVATAR.getBackgroundColor(), "ammunition", TETile.TileType.FLOOR);
    public static final TETile TREE = new TETile('⬆', Color.green, AVATAR.getBackgroundColor(), "power up", TETile.TileType.FLOOR);

    public static final TETile GRASS = new TETile('♘', Color.cyan, Color.black, "type 1 mob", TETile.TileType.MOB);
    public static final TETile FLOWER = new TETile('✷', Color.orange, Color.black, "type 2 mob", TETile.TileType.MOB);
    public static final TETile MOUNTAIN = new TETile('⏃', Color.white, Color.black, "type 3 mob", TETile.TileType.MOB);

    public static final TETile ENRAGED_MOB1 = new TETile('♘', Color.black, Color.red, "angry type 1 mob", TETile.TileType.MOB);
    public static final TETile ENRAGED_MOB2 = new TETile('✷', Color.black, Color.red, "angry type 2 mob", TETile.TileType.MOB);
    public static final TETile ENRAGED_MOB3 = new TETile('⏃', Color.black, Color.red, "angry type 3 mob", TETile.TileType.MOB);

    public static final TETile AVATAR_HEALTH = new TETile(' ', Color.black, Color.green, "avatar health", TETile.TileType.MISC);
    public static final TETile AVATAR_AMMO = new TETile('⁍', Color.black, Color.red, "ammunition", TETile.TileType.MISC);
    public static final TETile GAME_TIME = new TETile('⏲', Color.black, Color.blue, "current time", TETile.TileType.MISC);
    public static final TETile GAME_KEYS = new TETile('⚿', Color.black, Color.yellow, "keys", TETile.TileType.MISC);
    public static final TETile QUESTION = new TETile('⛭', Color.black, Color.orange, "settings", TETile.TileType.SETTINGS);

    public static final TETile ITEM0 = new TETile('0', Color.white, Color.black, "nothing", TETile.TileType.MISC);
    public static final TETile ITEM1 = new TETile('1', Color.white, Color.black, "nothing", TETile.TileType.MISC);
    public static final TETile ITEM2 = new TETile('2', Color.white, Color.black, "nothing", TETile.TileType.MISC);
    public static final TETile ITEM3 = new TETile('3', Color.white, Color.black, "nothing", TETile.TileType.MISC);
    public static final TETile ITEM4 = new TETile('4', Color.white, Color.black, "nothing", TETile.TileType.MISC);
    public static final TETile ITEM5 = new TETile('5', Color.white, Color.black, "nothing", TETile.TileType.MISC);
    public static final TETile ITEM6 = new TETile('6', Color.white, Color.black, "nothing", TETile.TileType.MISC);
    public static final TETile ITEM7 = new TETile('7', Color.white, Color.black, "nothing", TETile.TileType.MISC);
    public static final TETile ITEM8 = new TETile('8', Color.white, Color.black, "nothing", TETile.TileType.MISC);
    public static final TETile ITEM9 = new TETile('9', Color.white, Color.black, "nothing", TETile.TileType.MISC);

    public static final TETile[] NUMBER_TILES = new TETile[]{ITEM0, ITEM1, ITEM2, ITEM3, ITEM4, ITEM5, ITEM6, ITEM7, ITEM8, ITEM9};
}


