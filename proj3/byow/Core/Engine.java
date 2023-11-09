package byow.Core;

import byow.Characters.Item;
import byow.Characters.Key;
import byow.Characters.Mob;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class Engine {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    private boolean lineOfSightEnabled = false;
    private double lastTimeLineOfSightEnabled;
    private static final int NUMBER = 3;

    private static final int LINE_OF_SIGHT_DISTANCE = 20;
    private static final int PIXEL_SIZE = 6;

    private static final int FONT_BIG = 40;
    private static final int FONT_MEDIUM = 30;
    private static final int FONT_SMALL = 20;
    private static final int PAUSING_TIME = 50;

    private static final String PARTING_WORDS = "May fortune favor you."; // change depending on game mode
    private static final String[] LORE = {"The world has become ravaged by chaos and darkness. Only a few survivors remain. You are one of them.", "The world is now divided into uncharted territories, and only the bravest adventurers dare to explore the unknown.", "Your quest is to gather all the keys to unlock the secrets of life.", "Your destiny is now in your hands, brave adventurer. " + PARTING_WORDS};

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        drawStartingScreen();
        World newWorld = solicitStartingCommand();
        if (newWorld != null) {
            ter.initialize(WIDTH, HEIGHT);
            displayWorld(newWorld);
            solicitInteractivity(newWorld);
        } else {
            interactWithKeyboard();
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        char[] inputArr = input.toCharArray();
        World finalWorldFrame = null;
//         user inputs a seed, returns the world created by this seed.
        int currIndex = 0;
        while (currIndex < inputArr.length) {
            switch (inputArr[currIndex]) {
                case 'n', 'N':
                    if (inputArr.length <= 2) {
                        throw new IllegalArgumentException();
                    }
                    int place = 1;
                    String number = "";
                    while (inputArr[place] != 's' && inputArr[place] != 'S') {
                        int x = inputArr[place];
                        if (x >= Character.getNumericValue('1') || x <= Character.getNumericValue('9')) {
                            number += inputArr[place];
                        }
                        place += 1;
                    }
                    long seed = Long.valueOf(number);
                    currIndex = place + 1;
                    finalWorldFrame = WorldCreation.createWorld(seed);
                    break;
                case 'l', 'L':
                    finalWorldFrame = WorldCreation.loadWorld();
                    currIndex++;
                    break;
                case 'q':
                    currIndex += 2;
                    finalWorldFrame.saveWorld();
                    System.exit(0);
                    break;
                case 'w', 'W':
                    finalWorldFrame.getAvatar().move("up");
                    currIndex++;
                    break;
                case 'a', 'A':
                    finalWorldFrame.getAvatar().move("left");
                    currIndex++;
                    break;
                case 's', 'S':
                    finalWorldFrame.getAvatar().move("down");
                    currIndex++;
                    break;
                case 'd', 'D':
                    finalWorldFrame.getAvatar().move("right");
                    currIndex++;
                    break;
                default:
                    break;
            }
        }
        return finalWorldFrame.getWorldArray();
    }


    public static void main(String[] args) {
        Engine e = new Engine();
        TETile[][] w = e.interactWithInputString("n1234sw");
        TETile[][] w2 = e.interactWithInputString("n1234sw");
        System.out.println(Arrays.deepEquals(w, w2));
    }

    public World solicitStartingCommand() {
        World finalWorldFrame = null;
        String typedString = "";

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                typedString += StdDraw.nextKeyTyped();
                break;
            }
            StdDraw.pause(PAUSING_TIME);
        }

        switch (typedString) {
            case "n", "N":
                Character gm = drawGameMode();
                showLoreScreen(LORE, 0);
                long seed = solicitWorldInput(typedString);
                finalWorldFrame = WorldCreation.createWorld(seed);
                break;
            case "l", "L":
                long seed1 = solicitWorldInput(typedString);
                finalWorldFrame = WorldCreation.loadWorld();
                break;
            case "q", ":Q":
                //quit game
                System.exit(0);
                break;
            default:
                break;

        }
        return finalWorldFrame;
    }

    public long solicitWorldInput(String typedString) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Octin", Font.BOLD, FONT_BIG);
        StdDraw.setFont(fontBig);
        if (Objects.equals(typedString, "n") || Objects.equals(typedString, "N")) {
            StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, "Enter seed. When done, type 'S'.");
        } else if (Objects.equals(typedString, "l") || Objects.equals(typedString, "L")) {
            StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, "Enter seed to reload world. When done, type 'S'.");
        } else if (Objects.equals(typedString, "q") || Objects.equals(typedString, "Q")) {
            StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, "Are you sure you want to quit?");
        }
        StdDraw.show();
        Font fontSmall = new Font("Octin", Font.BOLD, FONT_MEDIUM);
        StdDraw.setFont(fontSmall);

        String input = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char x = StdDraw.nextKeyTyped();
                if (x == 'S' || x == 's') {
                    return Long.parseLong(input);
                }
                if (x >= Character.getNumericValue('1') || x <= Character.getNumericValue('9')) {
                    input += x;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.setFont(fontBig);
                    if (Objects.equals(typedString, "L") || Objects.equals(typedString, "l")) {
                        StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, "Enter seed to reload world. When done, type 'S'.");
                    } else {
                        StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, "Enter seed. When done, type 'S'.");
                    }
                    StdDraw.setFont(fontSmall);
                    StdDraw.text((float) WIDTH / 2, (float) HEIGHT / 2 - 2, String.valueOf(input));
                    StdDraw.show();
                    StdDraw.pause(PAUSING_TIME);
                }
            }
        }
//        return Long.parseLong(input);
    }

    public void solicitInteractivity(World world) {
        StdDraw.enableDoubleBuffering();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char x = StdDraw.nextKeyTyped();
                switch (x) {
                    case 'w' -> {
                        world.getAvatar().move("up");
                        StdDraw.pause((int) world.getAvatar().getSpeed());
                    }
                    case 'a' -> {
                        world.getAvatar().move("left");
                        StdDraw.pause((int) world.getAvatar().getSpeed());
                    }
                    case 's' -> {
                        world.getAvatar().move("down");
                        StdDraw.pause((int) world.getAvatar().getSpeed());
                    }
                    case 'd' -> {
                        world.getAvatar().move("right");
                        StdDraw.pause((int) world.getAvatar().getSpeed());
                    }
                    case 'q' -> {
                        world.saveWorld();
                        System.exit(0);
                    }
                    case 't' -> {
//                        lastTimeLineOfSightEnabled = world.sw.elapsedTime();
                        lineOfSightEnabled = !lineOfSightEnabled;
                    }
                    case 'i' -> {
                        world.getAvatar().pewpew("up");
                    }
                    case 'k' -> {
                        world.getAvatar().pewpew("down");
                    }
                    case 'j' -> {
                        world.getAvatar().pewpew("left");
                    }
                    case 'l' -> {
                        world.getAvatar().pewpew("right");
                    }
                    case 'c' -> {
                        // resume playing game
                    }
                    default -> {
                        // nothing here
                    }
                }
            }
            int mouseX = (int) StdDraw.mouseX();
            int mouseY = (int) StdDraw.mouseY();
            TETile[][] clickedTile = displayWorld(world);
            if (StdDraw.isMousePressed()) {
                if (mouseX >= 0 && mouseX < WIDTH && mouseY >= 0 && mouseY < HEIGHT) {
                    if (clickedTile[mouseX][mouseY].equals(Tileset.QUESTION)) {
                        drawGameOverScreen(world, "Game Commands");
//                        interactWithKeyboard();
                        World newWorld = solicitStartingCommand();
                        if (newWorld != null) {
                            ter.initialize(WIDTH, HEIGHT);
                            displayWorld(newWorld);
                            solicitInteractivity(newWorld);
                        } else {
                            interactWithKeyboard();
                        }
                        break;
                    }
                }
            } else {
                displayHUD(clickedTile[mouseX][mouseY].description());
            }
//            else {
//                if (mouseX >= 0 && mouseX < WIDTH && mouseY >= 0 && mouseY < HEIGHT) {
//                    TETile hoveredTile = world.getWorldArray()[mouseX][mouseY];
//                    displayHUD(hoveredTile.description());
//                }
//            }
            if (world.getAvatar().getHealth() <= 0) { // game over
                drawGameOverScreen(world, "GAME OVER!");
                lineOfSightEnabled = !lineOfSightEnabled;
                interactWithKeyboard();
                break;
            } else if (world.getKeys().size() == 1) {
                drawGameOverScreen(world, "YOU WIN!");
                interactWithKeyboard();
                break;
            }
            displayWorld(world);
        }
    }

    private String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm");
        return currentDateTime.format(formatter);
    }

    private void displayHUD(String info) {
        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(2, (float) HEIGHT - 1, "TILE: " + info.toUpperCase());
        StdDraw.show();
    }

    private void displayTime() {
        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft((float) WIDTH - 12, HEIGHT + 1, getCurrentDateTime());
        StdDraw.show();
    }

    public static void drawStartingScreen() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Octin", Font.BOLD, FONT_MEDIUM);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Octin", Font.BOLD, FONT_BIG);
        StdDraw.setFont(fontBig);
        StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, "THE DEATH MAZE");

        /* If the game is not over, display encouragement, and let the user know if they
         * should be typing their answer or watching for the next round. */
        Font fontSmall = new Font("Octin", Font.BOLD, FONT_SMALL);
        StdDraw.setFont(fontSmall);
        StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
        StdDraw.line(0, 2, WIDTH, 2);
        StdDraw.textLeft(1, HEIGHT - 1, "version 1.0");
        StdDraw.textRight(WIDTH - 1, HEIGHT - 1, "Build Your Own World");
        StdDraw.text((float) WIDTH / 2, 1, "Created by Surya Tallavarjula and Siddharth Ganapathy");

        StdDraw.text((float) WIDTH / 2, (float) HEIGHT / 2, "New Game: N");
        StdDraw.text((float) WIDTH / 2, (float) HEIGHT / 2 - 2, "Load Previous Game: L");
        StdDraw.text((float) WIDTH / 2, (float) HEIGHT / 2 - 4, "Quit: Q");
        StdDraw.show();
    }

    public static void drawGameOverScreen(World world, String message) {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Octin", Font.BOLD, FONT_MEDIUM);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        StdDraw.clear(Color.BLACK);
        Font fontBig = new Font("Octin", Font.BOLD, FONT_BIG);
        StdDraw.setFont(fontBig);

        Font fontSmall = new Font("Octin", Font.BOLD, FONT_SMALL);
        int x = 3;
        int x1 = 5;
        int x2 = 7;
        int x3 = 9;
        int x4 = 11;

        switch (message) {
            case "GAME OVER!" -> {
                StdDraw.setPenColor(Color.orange);
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, message);
                StdDraw.setFont(fontSmall);
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3 - 3, "Total Keys Left: " + world.getKeys().size());
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3 - x2, "New Game: N      Return Home: H      Quit: Q");
            }
            case "Game Commands" -> {
                StdDraw.setPenColor(Color.pink);
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, message);
                StdDraw.setFont(fontSmall);
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3 - x, "W & S: Move Up and Down");
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3 - x1, "A & D: Move Left and Right");
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3 - x2, "I & K: Shoot Up and Down");
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3 - x3, "J & L: Shoot Left and Right");
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3 - x4 - 2, "New Game: N      Continue Game: C      Quit: Q");
            }
            case "YOU WIN!" -> {
                StdDraw.setPenColor(Color.GREEN);
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, message);
                StdDraw.setFont(fontSmall);
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3 - x, "Time Elapsed: " + world.sw.elapsedTime());
                StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3 - x2, "New Game: N      Save Results: S      Quit: Q");
            }
            default -> {

            }
        }
        StdDraw.show();
    }


    public static void showLoreScreen(String[] lore, int index) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Octin", Font.PLAIN, FONT_SMALL);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        // Background story and lore text
        StdDraw.pause(PAUSING_TIME * 2);
        String loreText = lore[index];
        StdDraw.text((float) WIDTH / 2, (float) HEIGHT / 2, loreText);

        Font font2 = new Font("Octin", Font.BOLD, FONT_SMALL);
        StdDraw.setFont(font2);
        StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
        StdDraw.line(0, 2, WIDTH, 2);
        StdDraw.text((float) WIDTH / 2, (float) HEIGHT / 2 - 8, "Press 'C' to continue or 'S' to skip.");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 'C' || input == 'c') {
                    if (index >= LORE.length - 1) {
                        break;
                    } else {
                        showLoreScreen(lore, index + 1);
                    }
                    break;
                }
                if (input == 'S' || input == 's') {
                    break;
                }
            }
        }
    }

    public static Character drawGameMode() {
        StdDraw.clear(Color.BLACK);
//        StdDraw.setPenColor(Color.red);
        StdDraw.setPenColor(Color.white);
        Font fontBig = new Font("Octin", Font.BOLD, FONT_BIG);
        StdDraw.setFont(fontBig);
        StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, "Choose a Game Mode");

        Font fontSmall = new Font("Octin", Font.BOLD, FONT_SMALL);
        StdDraw.setFont(fontSmall);
        StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
        StdDraw.line(0, 2, WIDTH, 2);
        StdDraw.text((float) WIDTH / 2, (float) HEIGHT / 2, "Easy: E");
        StdDraw.text((float) WIDTH / 2, (float) HEIGHT / 2 - 2, "Medium: M");
        StdDraw.text((float) WIDTH / 2, (float) HEIGHT / 2 - 4, "Hard: H");
        StdDraw.text((float) WIDTH / 2, (float) HEIGHT / 2 - 6, "Impossible: I");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 'e' || input == 'm' || input == 'h' || input == 'i') {
                    return input;
                } else {
                    invalidSeed("Invalid command. Please try again.");
                    drawGameMode();
                    break;
                }
            }
        }
        return null;
    }

    public static void invalidSeed(String message) {
        int magicNumber = 10;
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Octin", Font.BOLD, magicNumber * 4);
        StdDraw.setFont(fontBig);
        StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
        StdDraw.line(0, 2, WIDTH, 2);
        StdDraw.text((float) WIDTH / 2, (float) 2 * HEIGHT / 3, message);
        StdDraw.show();
        StdDraw.pause(PAUSING_TIME * magicNumber);
    }

    public TETile[][] displayWorld(World world) {
        displayTime();
        TETile[][] rawWorld = world.getWorldArray();
        int avaX = world.getAvatar().getX();
        int avaY = world.getAvatar().getY();
        TETile[][] displayWorld = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int worldX = avaX - WIDTH / 2 + x;
                int worldY = avaY - HEIGHT / 2 + y;
                if (worldX >= 0 && worldX < rawWorld.length && worldY >= 0 && worldY < rawWorld[0].length) {
                    int dx = Math.abs(avaX - worldX);
                    int dy = Math.abs(avaY - worldY);
                    if (!lineOfSightEnabled || dx + dy <= LINE_OF_SIGHT_DISTANCE) {
                        displayWorld[x][y] = rawWorld[worldX][worldY];
                    } else {
                        displayWorld[x][y] = Tileset.NOTHING;
                    }
                } else {
                    displayWorld[x][y] = Tileset.NOTHING;
                }
            }
        }

        updateMobsAndBullets(world);

        for (Item i : world.getItems()) {
            if (rawWorld[i.x][i.y].getTileType() == TETile.TileType.FLOOR) {
                rawWorld[i.x][i.y] = i.tileType;
            }
            if (world.getAvatar().getX() == i.x && world.getAvatar().getY() == i.y) {
                if (i instanceof Key k) {
                    world.getItems().remove(i);
                    world.getKeys().remove(i);
                    world.getAvatar().keyList.add(k);
                    break;
                } else {
                    world.getItems().remove(i);
                    i.useItem();
                    break;
                }
            }
        }
        int magicNumber = 10;

        if (world.sw.elapsedTime() - world.lastTimeRaged > (int) Math.pow(10, 2) && !world.enraged) {
            world.buffStuff();
        }
        if (world.sw.elapsedTime() - world.lastTimeRaged > magicNumber * 3 && world.enraged) {
            world.unbuffStuff();
        }
        displayWorld = world.disp(displayWorld, WIDTH / 2 - 8, HEIGHT - 1);

        ter.renderFrame(displayWorld);
        return displayWorld;
    }

    private void updateMobsAndBullets(World world) {
        for (Mob m : world.getMobs()) {
            m.timeToEnrage();
            if (m.timeToMove(world.sw.elapsedTime())) {
                if (m.getRage()) {
                    m.move(m.getAvatarDirection());
                } else {
                    m.move("");
                }
            }
            if (m.getRage()) {
                m.dealDamage(m.getDamage());
            }
        }

        updateBullets(world);
    }

    private void updateBullets(World world) {
        if (world.getAvatar().shotFired && world.getAvatar().timeToShoot(world.sw.elapsedTime())) {
            int xC = world.getAvatar().bulletX;
            int yC = world.getAvatar().bulletY;

            switch (world.getAvatar().shootingDirection) {
                case "up":
                    processBulletMovement(world, xC, yC, 0, 1, world.getAvatar().getDamage());
                    break;
                case "down":
                    processBulletMovement(world, xC, yC, 0, -1, world.getAvatar().getDamage());
                    break;
                case "left":
                    processBulletMovement(world, xC, yC, -1, 0, world.getAvatar().getDamage());
                    break;
                case "right":
                    processBulletMovement(world, xC, yC, 1, 0, world.getAvatar().getDamage());
                    break;
                default:
                    // Do nothing here
            }
        }
    }

    private void processBulletMovement(World world, int xC, int yC, int dx, int dy, int damage) {
        int targetX = xC + dx;
        int targetY = yC + dy;

        if (world.getWorldArray()[targetX][targetY] == Tileset.FLOOR) {
            world.getWorldArray()[targetX][targetY] = Tileset.BULLET;
            if (world.getWorldArray()[xC][yC] != Tileset.AVATAR) {
                world.getWorldArray()[xC][yC] = Tileset.FLOOR;
            }
            world.getAvatar().bulletX = targetX;
            world.getAvatar().bulletY = targetY;
            world.getAvatar().lastTimeShot = world.sw.elapsedTime();
        } else if (world.getWorldArray()[targetX][targetY].getTileType() == TETile.TileType.MOB) {
            world.getAvatar().shotFired = false;
            if (world.getWorldArray()[xC][yC] != Tileset.AVATAR) {
                world.getWorldArray()[xC][yC] = Tileset.FLOOR;
            }
            for (int i = 0; i < world.getMobs().size(); i++) {
                Mob mob = world.getMobs().get(i);
                if (mob.getX() == targetX && mob.getY() == targetY) {
                    mob.loseHealth(damage);
                    if (mob.getHealth() <= 0) {
                        world.getWorldArray()[targetX][targetY] = Tileset.FLOOR;
                        world.getMobs().remove(i);
                        break;
                    }
                }
            }
        } else {
            world.getAvatar().shotFired = false;
            if (world.getWorldArray()[xC][yC] != Tileset.AVATAR) {
                world.getWorldArray()[xC][yC] = Tileset.FLOOR;
            }
        }
    }


}
