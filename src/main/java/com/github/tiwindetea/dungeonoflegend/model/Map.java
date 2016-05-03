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
    public static final int MIN_LEVEL_WIDTH = 40;
    public static final int MAX_LEVEL_WIDTH = 60;
    public static final int MIN_LEVEL_HEIGHT = 30;
    public static final int MAX_LEVEL_HEIGHT = 34;
    public static final int MIN_ROOM_NUMBER = 5;
    public static final int MAX_ROOM_NUMBER = 12;
    public static final int MIN_CORRIDOR_NBR = 4;
    public static final int MAX_CORRIDOR_NBR = 7;
    public static final int MIN_CORRIDOR_LENGTH = 3;
    public static final int MAX_CORRIDOR_LENGTH = 6;
    public static final int RETRIES_NBR = 100;
    public static final int PROBABILITY_UNIT = 100;
    public static final int ROOM_BINDED_TO_CORRIDOR_CHANCE = 95;
    public static final int CORRIDOR_BINDED_TO_CORRIDOR_CHANCE = 10;
    public static final int PILLAR_PROBABILITY = 75;
    public static final int MAX_PILLAR_NBR_PER_ROOM = 5;

    private Seed seed;
    private Random random;
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
        ArrayList<String> test;
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
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Room> corridors = new ArrayList<>();
        int retries = 0;

        this.random = this.seed.getRandomizer(level);
        this.map = new Tile[this.random.nextInt(MAX_LEVEL_WIDTH - MIN_LEVEL_WIDTH) + MIN_LEVEL_WIDTH]
                [this.random.nextInt(MAX_LEVEL_HEIGHT - MIN_LEVEL_HEIGHT) + MIN_LEVEL_HEIGHT];
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                this.map[i][j] = Tile.UNKNOWN;
            }
        }

        int roomNbr = this.random.nextInt(MAX_ROOM_NUMBER - MIN_ROOM_NUMBER) + MIN_ROOM_NUMBER;
        int corridorNbr = this.random.nextInt(MAX_CORRIDOR_NBR - MIN_CORRIDOR_NBR) + MIN_CORRIDOR_NBR;
        int x, y, width, height;
        Room room, corridor;

        do {
            width = this.random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH) + MIN_ROOM_WIDTH;
            height = this.random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT) + MIN_ROOM_HEIGHT;
            x = this.random.nextInt(this.map.length / 2 - 5 - width) + 10 - height;
            y = this.random.nextInt(this.map.length / 2 - 5 - height) + 10 + height;
            room = new Room(x, y, x + width, y + height);
        } while (!isPlaceable(room));

        rooms.add(room);
        room.print();
        do {
            corridor = generateCorridor(room);
        } while (corridor == null);
        corridors.add(corridor);

        do {
            ++retries;
            if (this.random.nextInt(roomNbr - rooms.size() + corridorNbr - corridors.size()) < (corridorNbr - corridors.size())) {
                if (CORRIDOR_BINDED_TO_CORRIDOR_CHANCE >= Map.this.random.nextInt(PROBABILITY_UNIT)) {
                    corridor = generateCorridor(corridors.get(this.random.nextInt(corridors.size())));
                } else {
                    corridor = generateCorridor(rooms.get(this.random.nextInt(rooms.size())));
                }
                if (corridor != null) {
                    retries = 0;
                    corridors.add(corridor);
                }
            } else {
                if (ROOM_BINDED_TO_CORRIDOR_CHANCE >= this.random.nextInt(PROBABILITY_UNIT)) {
                    room = generateRoom(corridors.get(this.random.nextInt(corridors.size())));
                } else {
                    room = generateRoom(rooms.get(this.random.nextInt(rooms.size())));
                }
                if (room != null) {
                    retries = 0;
                    rooms.add(room);
                }
            }
        }
        while ((rooms.size() < roomNbr || corridors.size() < corridorNbr)
                && (retries < RETRIES_NBR || rooms.size() < MIN_ROOM_NUMBER || corridors.size() < MIN_CORRIDOR_NBR)
                && retries < 5 * Byte.MAX_VALUE);

        if (withEntities) {
            //TODO: Generate the entities ?
        }
    }

    private boolean isPlaceable(Room room) {
        if (room.top.x < 1 || room.top.y < 1 || room.bottom.x >= this.map.length - 1 || room.bottom.y >= this.map[0].length - 1)
            return false;
        for (int i = room.top.x; i <= room.bottom.x; ++i) {
            for (int j = room.top.y; j <= room.bottom.y; ++j) {
                if (this.map[i][j] != Tile.UNKNOWN) {
                    return false;
                }
            }
        }
        return true;
    }

    private Room generateCorridor(Room link) {
        int x, y;
        int doorX, doorY;
        int height = 0, width = 0;
        Room corridor;
        switch (this.random.nextInt(4)) {
            case 0:
                x = link.bottom.x + 1;
                if (link.bottom.y == link.top.y) {
                    y = link.top.y;
                } else {
                    y = this.random.nextInt(link.bottom.y - link.top.y) + link.top.y;
                }
                doorX = x - 1;
                doorY = y;
                width = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;
                break;
            case 1:
                x = link.top.x - 2;
                if (link.bottom.y == link.top.y) {
                    y = link.top.y;
                } else {
                    y = this.random.nextInt(link.bottom.y - link.top.y) + link.top.y;
                }
                doorX = x + 1;
                doorY = y;
                width = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;
                x -= width;
                break;
            case 2:
                if (link.bottom.x == link.top.x) {
                    x = link.top.x;
                } else {
                    x = this.random.nextInt(link.bottom.x - link.top.x) + link.top.x;
                }
                y = link.bottom.y + 1;
                doorX = x;
                doorY = y - 1;
                height = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;
                break;
            case 3:
                /* Falls through */
            default:
                if (link.bottom.x == link.top.x) {
                    x = link.top.x;
                } else {
                    x = this.random.nextInt(link.bottom.x - link.top.x) + link.top.x;
                }
                y = link.top.y - 2;
                doorX = x;
                doorY = y + 1;
                height = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;
                y -= height;
                break;
        }
        corridor = new Room(x, y, x + width, y + height);
        int a = 0;
        a++;
        if (!isPlaceable(corridor)) {
            return null;
        }
        corridor.print();
        this.map[doorX][doorY] = Tile.CLOSED_DOOR;
        return corridor;
    }

    private Room generateRoom(Room link) {
        Room room;
        int x, y, tmp;
        int doorX, doorY;
        int height = this.random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT) + MIN_ROOM_HEIGHT;
        int width = this.random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH) + MIN_ROOM_WIDTH;
        tmp = this.random.nextInt(4);
        switch (tmp) {
            case 0:
                x = link.bottom.x + 1;
                if (link.bottom.y == link.top.y) {
                    y = link.top.y;
                } else {
                    y = this.random.nextInt(link.bottom.y - link.top.y) + link.top.y;
                }
                doorX = x - 1;
                doorY = y;
                if (this.random.nextBoolean()) {
                    y -= height;
                }
                break;
            case 1:
                x = link.top.x - 2;
                if (link.bottom.y == link.top.y) {
                    y = link.top.y;
                } else {
                    y = this.random.nextInt(link.bottom.y - link.top.y) + link.top.y;
                }
                doorX = x + 1;
                doorY = y;
                x -= width;
                if (this.random.nextBoolean()) {
                    y -= height;
                }
                break;
            case 2:
                if (link.bottom.x == link.top.x) {
                    x = link.top.x;
                } else {
                    x = this.random.nextInt(link.bottom.x - link.top.x) + link.top.x;
                }
                y = link.bottom.y + 1;
                doorX = x;
                doorY = y - 1;
                if (this.random.nextBoolean()) {
                    x -= width;
                }
                break;
            case 3:
                /* Falls through */
            default:
                if (link.bottom.x == link.top.x) {
                    x = link.top.x;
                } else {
                    x = this.random.nextInt(link.bottom.x - link.top.x) + link.top.x;
                }
                y = link.top.y - 2;
                doorX = x;
                doorY = y + 1;
                y -= height;
                if (this.random.nextBoolean()) {
                    x -= width;
                }
                break;
        }
        room = new Room(x, y, x + width, y + height);
        if (!isPlaceable(room)) {
            return null;
        }
        room.print();
        this.map[doorX][doorY] = Tile.CLOSED_DOOR;
        return room;
    }

    private class Room {
        private Vector2i top;
        private Vector2i bottom;

        public Room(Vector2i top, Vector2i bottom) {
            this.top = top;
            this.bottom = bottom;
        }

        public Room(int x1, int y1, int x2, int y2) {
            this(new Vector2i(x1, y1), new Vector2i(x2, y2));
        }

        public void print() {
            for (int i = this.top.x; i <= this.bottom.x; ++i) {
                Map.this.map[i][this.top.y - 1] = Tile.WALL_DOWN;
                Map.this.map[i][this.bottom.y + 1] = Tile.WALL_TOP;
                for (int j = this.top.y; j <= this.bottom.y; ++j) {
                    Map.this.map[i][j] = Tile.GROUND;
                }
            }
            for (int i = this.top.y; i <= this.bottom.y; ++i) {
                Map.this.map[this.top.x - 1][i] = Tile.WALL_RIGHT;
                Map.this.map[this.bottom.x + 1][i] = Tile.WALL_LEFT;
            }
            Map.this.map[this.top.x - 1][this.top.y - 1] = Tile.WALL_DOWNRIGHT;
            Map.this.map[this.top.x - 1][this.bottom.y + 1] = Tile.WALL_TOPRIGHT;
            Map.this.map[this.bottom.x + 1][this.top.y - 1] = Tile.WALL_DOWNLEFT;
            Map.this.map[this.bottom.x + 1][this.bottom.y + 1] = Tile.WALL_TOPLEFT;

            int minimum = Math.min(this.bottom.x - this.top.x, this.bottom.y - this.top.y);
            if (minimum > 2) {
                for (int i = 0; i < MAX_PILLAR_NBR_PER_ROOM; ++i) {
                    if (minimum > 2 + i && PILLAR_PROBABILITY >= Map.this.random.nextInt(PROBABILITY_UNIT)) {
                        this.randomPillar();
                    }
                }
            }
        }

        private void randomPillar() {
            int x, y;
            do {
                x = Map.this.random.nextInt(this.bottom.x - this.top.x - 2) + this.top.x + 1;
                y = Map.this.random.nextInt(this.bottom.y - this.top.y - 2) + this.top.y + 1;
            } while (Map.this.map[x][y] != Tile.GROUND);
            switch (Map.this.random.nextInt(4)) {
                case 0:
                    Map.this.map[x][y] = Tile.WALL_TOP;
                    break;
                case 1:
                    Map.this.map[x][y] = Tile.WALL_RIGHT;
                    break;
                case 2:
                    Map.this.map[x][y] = Tile.WALL_LEFT;
                    break;
                case 3:
                    /* Falls through */
                default:
                    Map.this.map[x][y] = Tile.WALL_DOWN;
                    break;
            }
        }

        @Override
        public String toString() {
            return "Room{" +
                    "top=" + this.top +
                    ", bottom=" + this.bottom +
                    '}';
        }

        public Vector2i top() {
            return this.top;
        }

        public Vector2i bottom() {
            return this.bottom;
        }
    }
}
