package com.github.tiwindetea.dungeonoflegend.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by maxime on 4/24/16.
 */
public class Map {
    public static final int MIN_ROOM_WIDTH = 3;
    public static final int MAX_ROOM_WIDTH = 12;
    public static final int MIN_ROOM_HEIGHT = 3;
    public static final int MAX_ROOM_HEIGHT = 12;
    public static final int MIN_LEVEL_WIDTH = 80;
    public static final int MAX_LEVEL_WIDTH = 120;
    public static final int MIN_LEVEL_HEIGHT = 80;
    public static final int MAX_LEVEL_HEIGHT = 120;

    private Seed seed;
    private Vector2i stairsUpPosition;
    private Vector2i stairsDownPosition;
    private ArrayList<InteractiveObject> interactiveObjects = new ArrayList<>();
    private Tile[][] map;

    public Map() {
        this.seed = new Seed();

        Random rand = new Random();
        this.stairsUpPosition = new Vector2i(rand.nextInt(MIN_LEVEL_WIDTH - 10) + 5, rand.nextInt(MIN_LEVEL_HEIGHT - 10) + 5);
        /* Sample test map, to be removed in further versions (TODO) */
        this.map = new Tile[11][11];
        this.map[0][0] = Tile.WALL_DOWNRIGHT;
        this.map[this.map.length - 1][0] = Tile.WALL_TOPRIGHT;
        this.map[this.map.length - 1][this.map[0].length - 1] = Tile.WALL_TOPLEFT;
        this.map[0][this.map[0].length - 1] = Tile.WALL_DOWNLEFT;
        for (int i = 1; i < this.map[0].length - 1; i++) {
            this.map[0][i] = Tile.WALL_DOWN;
            this.map[this.map.length - 1][i] = Tile.WALL_TOP;
        }
        for (int i = 1; i < this.map.length - 1; i++) {
            this.map[i][0] = Tile.WALL_RIGHT;
            this.map[i][this.map[0].length - 1] = Tile.WALL_LEFT;
        }
        for (int i = 1; i < this.map.length - 1; i++) {
            for (int j = 1; j < this.map[i].length - 1; j++) {
                this.map[i][j] = Tile.GROUND;
            }
        }

        this.map[1][3] = Tile.WALL_DOWNLEFT;
        this.map[2][3] = Tile.WALL_LEFT;
        this.map[3][3] = Tile.WALL_LEFT;
        this.map[4][1] = Tile.CLOSED_DOOR;
        this.map[4][3] = Tile.WALL_TOPLEFT;
        this.map[4][2] = Tile.WALL_TOP;
        this.map[5][3] = Tile.CLOSED_DOOR;
        this.map[6][2] = Tile.WALL_DOWN;
        this.map[6][3] = Tile.WALL_DOWN;
        this.map[6][4] = Tile.WALL_DOWN;
        this.map[6][5] = Tile.WALL_RIGHT;
        this.map[7][5] = Tile.CLOSED_DOOR;
        this.map[8][5] = Tile.WALL_TOPRIGHT;
        this.map[8][6] = Tile.WALL_TOP;
        this.map[8][7] = Tile.WALL_TOP;
        this.map[7][8] = Tile.WALL_TOP;
        this.map[7][9] = Tile.WALL_TOP;
        this.map[3][6] = Tile.WALL_TOP;
    }

    public Map(Seed seed) {
        this.seed = seed;
    }

    public static void main(String[] args) {
        Map world = new Map();

        Vector2i pos = new Vector2i(1, 1);

        for (pos.x = 0; pos.x < 9; ++pos.x) {
            for (pos.y = 0; pos.y < 9; ++pos.y) {
                System.out.println("position = " + pos);
                Tile[][] LOS = world.getLOS(pos, 8);

                for (int i = 0; i < LOS.length; i++) {
                    for (int j = 0; j < LOS[i].length; j++) {
                        if (i == LOS.length / 2 && j == LOS[i].length / 2) {
                            System.out.print("\033[35m@\033[0m");
                        } else if (Tile.isObstructed(LOS[i][j])) {
                            System.out.print("#");
                        } else {
                            switch (LOS[i][j]) {
                                case GROUND:
                                    System.out.print(".");
                                    break;
                                case OPENED_DOOR:
                                    System.out.print("O");
                                    break;
                                case CLOSED_DOOR:
                                    System.out.print("X");
                                    break;
                                case STAIR_UP:
                                    System.out.print("*");
                                    break;
                                case STAIR_DOWN:
                                    System.out.print(" ");
                                    break;
                                case UNKNOWN:
                                    System.out.print("~");
                                    break;
                                default:
                                    System.out.print("-");
                            }
                        }
                    }
                    System.out.println("");
                }
                System.out.println("\n\n");
            }
            System.out.println("\n\n");
        }
    }

    public void triggerTile(Vector2i position, LivingThing target) {
        if (this.map[position.x][position.y] == Tile.CLOSED_DOOR) {
            this.map[position.x][position.y] = Tile.OPENED_DOOR;
        } else if (this.map[position.x][position.y] == Tile.OPENED_DOOR) {
            this.map[position.x][position.y] = Tile.CLOSED_DOOR;
        } else {
            for (int i = 0; i < this.interactiveObjects.size(); i++) {
                if (this.interactiveObjects.get(i).getPosition().equals(position)) {
                    if (this.interactiveObjects.get(i).trigger(target)) {
                        this.interactiveObjects.remove(i);
                    }
                }
            }
        }
    }

    public Tile[][] getMapCopy() {
        return this.map.clone();
    }

    public Vector2i getStairsUpPosition() {
        return this.stairsUpPosition;
    }

    public Vector2i getStairsDownPosition() {
        return this.stairsDownPosition;
    }

    public long getAlphaSeed() {
        return this.seed.getAlphaSeed();
    }

    public long getBetaSeed() {
        return this.seed.getBetaSeed();
    }

    public Tile getTile(Vector2i position) {
        if (position.x < 0 || position.x > this.map.length || position.y < 0 || position.y > this.map[0].length)
            return null;
        return this.map[position.x][position.y];
    }

    public Tile[][] getLOS(Vector2i position, int visionRange) {
        if (visionRange < 0) {
            throw new IllegalArgumentException("visionRange should not be negative (value : " + visionRange);
        }
        if (position.x < 0 || position.y < 0 || position.x > this.map.length || position.y > this.map[0].length) {
            throw new IllegalArgumentException("Watcher outside of the map !");
        }

        Tile[][] LOS = new Tile[2 * visionRange + 1][2 * visionRange + 1];
        int squaredVisionRange = visionRange * visionRange;
        int xlos_shift = position.x - visionRange;
        int ylos_shift = position.y - visionRange;
        int i_end = Math.min(position.x + visionRange + 1, this.map.length);
        int j_end = Math.min(position.y + visionRange + 1, this.map[0].length);

        for (int i = 0; i < LOS.length; i++) {
            for (int j = 0; j < LOS[i].length; j++) {
                LOS[i][j] = Tile.UNKNOWN;
            }
        }

        for (int i = Math.max(position.x - visionRange, 0); i < i_end; ++i) {
            for (int j = Math.max(position.y - visionRange, 0); j < j_end; ++j) {
                if (Math.pow(i - position.x, 2) + Math.pow(j - position.y, 2) <= squaredVisionRange) {
                    if (this.isVisibleFrom(new Vector2i(i, j), position)) {
                        LOS[i - xlos_shift][j - ylos_shift] = this.map[i][j];
                    }
                }
            }
        }
        return LOS;
    }

    public Vector2i getSize() {
        return new Vector2i(this.map.length, this.map[0].length);
    }

    private boolean isVisibleFrom(Vector2i tilePosition, Vector2i watcherPosition) {
        float distance = (float) Math.sqrt(Math.pow(tilePosition.x - watcherPosition.x, 2)
                + Math.pow(tilePosition.y - watcherPosition.y, 2));

        if (distance != 0) {
            float xShifting = (tilePosition.x - watcherPosition.x) / distance;
            float yShifting = (tilePosition.y - watcherPosition.y) / distance;
            float currentX = watcherPosition.x;
            float currentY = watcherPosition.y;
            int x, y;
            float a = 0;

            for (int i = (int) (Math.floor(distance)); i > 0; --i) {
                currentX += xShifting;
                currentY += yShifting;
                x = Math.round(currentX);
                y = Math.round(currentY);
                if (Tile.isObstructed(this.map[x][y])
                        && !tilePosition.equals(new Vector2i(x, y)))
                    return false;
            }
        }
        return true;
    }

    public void generateLevel(int level) {
        generateLevel(level, true);
    }

    public void restoreLevel(int level) {
        generateLevel(level, false);
    }

    private void generateLevel(int level, boolean withEntities) {
        Random random = this.seed.getRandomizer(level);
        this.map = new Tile[random.nextInt(MAX_LEVEL_WIDTH - MIN_LEVEL_WIDTH) + MIN_LEVEL_WIDTH]
                [random.nextInt(MAX_LEVEL_HEIGHT - MIN_LEVEL_HEIGHT) + MIN_LEVEL_HEIGHT];


        if (withEntities) {
            //TODO: Generate the entities
        }
    }
}
