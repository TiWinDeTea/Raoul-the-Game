package com.github.tiwindetea.dungeonoflegend;

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
    private ArrayList<InteractiveObject> interactiveObjects;
    private Tile[][] map;

    public Map() {
        this.seed = new Seed();

        Random rand = new Random();
        this.stairsUpPosition = new Vector2i(rand.nextInt(MIN_LEVEL_WIDTH - 10) + 5, rand.nextInt(MIN_LEVEL_HEIGHT - 10) + 5);
    }

    public Map(Seed seed, Vector2i stairsUpPosition) {
        this.seed = seed;
        this.stairsUpPosition = stairsUpPosition;
    }

    public static void main(String[] args) {
        Map map = new Map();
        map.map = new Tile[9][9];
        for (int i = 0; i < map.map.length; i++) {
            for (int j = 0; j < map.map[i].length; j++) {
                map.map[i][j] = Tile.GROUND;
            }
        }

        map.map[0][0] = Tile.WALL;
        map.map[0][1] = Tile.WALL;
        map.map[0][2] = Tile.WALL;
        map.map[1][2] = Tile.WALL;
        map.map[2][2] = Tile.WALL;
        map.map[3][2] = Tile.WALL;
        map.map[3][1] = Tile.WALL;
        map.map[4][1] = Tile.OPENED_DOOR;
        map.map[5][1] = Tile.WALL;
        map.map[5][2] = Tile.WALL;
        map.map[5][3] = Tile.CLOSED_DOOR;
        map.map[5][4] = Tile.WALL;
        map.map[6][4] = Tile.WALL;
        map.map[7][4] = Tile.WALL;
        map.map[7][5] = Tile.WALL;
        map.map[7][6] = Tile.WALL;
        map.map[6][7] = Tile.WALL;
        map.map[6][8] = Tile.WALL;
        map.map[2][5] = Tile.WALL;
        Tile[][] LOS = map.getLOS(new Vector2i(4, 4), 4);

        for (Tile[] tiles : LOS) {
            for (Tile tile : tiles) {
                switch (tile) {
                    case WALL:
                        System.out.print("#");
                        break;
                    case GROUND:
                        System.out.print(".");
                        break;
                    case OPENED_DOOR:
                        System.out.print("O");
                        break;
                    case CLOSED_DOOR:
                        System.out.print("C");
                        break;
                    case STAIR_UP:
                        break;
                    case STAIR_DOWN:
                        break;
                    case UNKNOWN:
                        System.out.print("?");
                        break;
                    default:
                        System.out.print("-");
                }
            }
            System.out.println();
        }
    }

    public void triggerTile(Vector2i position, LivingThing target) {
        for (int i = 0; i < interactiveObjects.size(); i++) {
            if (interactiveObjects.get(i).getPosition().equals(position)) {
                if (interactiveObjects.get(i).trigger(target)) {
                    interactiveObjects.remove(i);
                }
            }
        }
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

        Tile[][] LOS = new Tile[2 * visionRange + 1][2 * visionRange + 1];
        int i_start = Math.max(position.x - visionRange, 0);
        int i_end = Math.min(position.x + visionRange, this.map.length);
        int j_start = Math.max(position.y - visionRange, 0);
        int j_end = Math.min(position.y + visionRange, this.map[0].length);
        int squaredVisionRange = (int) Math.pow(visionRange, 2);

        for (int i = 0; i < LOS.length; i++) {
            for (int j = 0; j < LOS[i].length; j++) {
                LOS[i][j] = Tile.UNKNOWN;
            }
        }

        for (int i = i_start; i < i_end; ++i) {
            for (int j = j_start; j < j_end; ++j) {
                if (Math.pow(i - position.x, 2) + Math.pow(j - position.y, 2) <= squaredVisionRange) {
                    if (this.isVisibleFrom(new Vector2i(i, j), position)) {
                        LOS[i][j] = this.map[i][j];
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

        int distance = (int) Math.sqrt(Math.pow(tilePosition.x - watcherPosition.x, 2)
                + Math.pow(tilePosition.y - watcherPosition.y, 2));
        float xShifting = (watcherPosition.x - tilePosition.x);
        float yShifting = (watcherPosition.y - tilePosition.y);
        xShifting /= distance;
        yShifting /= distance;
        float x = tilePosition.x + 0.4f + xShifting;
        float y = tilePosition.y + 0.4f + yShifting;
        --distance;

        for (int i = 0; i < distance; i++) {
            if (this.map[(int) x][(int) y] == Tile.WALL || this.map[(int) x][(int) y] == Tile.CLOSED_DOOR)
                return false;
            x += xShifting;
            y += yShifting;
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

        //TODO: Generate the map

        if (withEntities) {
            //TODO: Generate the entities
        }
    }
}
