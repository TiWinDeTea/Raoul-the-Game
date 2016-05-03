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
    public static final int MIN_LEVEL_WIDTH = 75;
    public static final int MAX_LEVEL_WIDTH = 125;
    public static final int MIN_LEVEL_HEIGHT = 75;
    public static final int MAX_LEVEL_HEIGHT = 125;
    public static final int MIN_ROOM_NUMBER = 60;
    public static final int MAX_ROOM_NUMBER = 61;
    public static final int MIN_CORRIDOR_NBR = 20;
    public static final int MAX_CORRIDOR_NBR = 30;
    public static final int MIN_CORRIDOR_LENGTH = 6;
    public static final int MAX_CORRIDOR_LENGTH = 18;
    public static final int RETRIES_NBR = 5000;

    public static final int PROBABILITY_UNIT = 100;
    public static final int ROOM_BINDED_TO_CORRIDOR_CHANCE = 95;
    public static final int CORRIDOR_BINDED_TO_CORRIDOR_CHANCE = 25;
    public static final int PILLAR_PROBABILITY = 40;
    public static final int MAX_PILLAR_NBR_PER_ROOM = 5;
    public static final int REBIND_CORRIDOR_TO_CORRIDOR_CHANCE = 30;
    public static final int REBIND_ROOM_TO_CORRIDOR_CHANCE = 15;
    public static final int REBIND_ROOM_TO_ROOM_CHANCE = 10;

    private Seed seed;
    private Random random;
    private Vector2i stairsUpPosition;
    private Vector2i stairsDownPosition;
    private ArrayList<InteractiveObject> interactiveObjects = new ArrayList<>();
    private Tile[][] map;

    public Map() {
        this.seed = new Seed();
        Random rand = new Random();
    }

    public Map(Seed seed) {
        this.seed = seed;
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
            x = this.random.nextInt((this.map.length - width) / 2 - 5) + 10 + width;
            y = this.random.nextInt((this.map[0].length - height) / 2 - 5) + 10 + height;
            room = new Room(x, y, x + width, y + height);
        } while (!isPlaceable(room));

        rooms.add(room);
        room.print();
        do {
            corridor = generateRoom(room, true, true);
        } while (corridor == null);
        corridors.add(corridor);

        do {
            ++retries;
            if (this.random.nextInt(roomNbr - rooms.size() + corridorNbr - corridors.size()) < (corridorNbr - corridors.size())) {
                if (CORRIDOR_BINDED_TO_CORRIDOR_CHANCE >= Map.this.random.nextInt(PROBABILITY_UNIT)) {
                    corridor = generateRoom(corridors.get(this.random.nextInt(corridors.size())), true, false);
                } else {
                    corridor = generateRoom(rooms.get(this.random.nextInt(rooms.size())), true, true);
                }
                if (corridor != null) {
                    retries = 0;
                    corridors.add(corridor);
                }
            } else {
                if (ROOM_BINDED_TO_CORRIDOR_CHANCE >= this.random.nextInt(PROBABILITY_UNIT)) {
                    room = generateRoom(corridors.get(this.random.nextInt(corridors.size())), false, true);
                } else {
                    room = generateRoom(rooms.get(this.random.nextInt(rooms.size())), false, true);
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

        for (int i = 0; i < rooms.size(); ++i) {
            for (int j = i + 1; j < rooms.size(); ++j) {
                if (REBIND_ROOM_TO_ROOM_CHANCE > this.random.nextInt(PROBABILITY_UNIT)) {
                    rebind(rooms.get(i), rooms.get(j), true);
                }
            }
            for (Room corridor1 : corridors) {
                if (REBIND_ROOM_TO_CORRIDOR_CHANCE > this.random.nextInt(PROBABILITY_UNIT)) {
                    rebind(rooms.get(i), corridor1, true);
                }
            }
        }
        for (int i = 0; i < corridors.size(); ++i) {
            for (int j = i + 1; j < corridors.size(); ++j) {
                if (REBIND_CORRIDOR_TO_CORRIDOR_CHANCE > this.random.nextInt(PROBABILITY_UNIT)) {
                    rebind(corridors.get(i), corridors.get(j), false);
                }
            }
        }


        if (withEntities) {
            //TODO: Generate the entities ?
        }
    }

    private void rebind(Room source, Room target, boolean door) {
        int min, max, tmp, nbr;
        Tile tile = door ? Tile.CLOSED_DOOR : Tile.GROUND;
        if (source.top.y - 2 == target.bottom.y) {
            if (source.top.x < target.bottom.x && source.bottom.x >= target.top.x) {
                min = Math.max(source.top.x, target.top.x);
                tmp = Math.min(source.bottom.x, target.bottom.x);
                max = Math.max(min, tmp);
                min = min + tmp - max;
                tmp = source.top.y - 1;
                for (int i = min; i <= max; ++i) {
                    if (!Tile.isObstructed(this.map[i][tmp]) || this.map[i][tmp] == Tile.CLOSED_DOOR)
                        return;
                }
                if (max != min) {
                    nbr = this.random.nextInt(max - min) + min;
                } else {
                    nbr = min;
                }
                this.map[nbr][tmp] = tile;
                reconstrucWalls(source, target, new Vector2i(nbr, tmp));
            }
        } else if (source.bottom.y + 2 == target.top.y) {
            if (source.top.x < target.bottom.x && source.bottom.x >= target.top.x) {
                min = Math.max(source.top.x, target.top.x);
                tmp = Math.min(source.bottom.x, target.bottom.x);
                max = Math.max(min, tmp);
                min = min + tmp - max;
                tmp = source.bottom.y + 1;
                for (int i = min; i <= max; ++i) {
                    if (!Tile.isObstructed(this.map[i][tmp]) || this.map[i][tmp] == Tile.CLOSED_DOOR)
                        return;
                }
                if (max != min) {
                    nbr = this.random.nextInt(max - min) + min;
                } else {
                    nbr = min;
                }
                this.map[min][tmp] = tile;
                reconstrucWalls(source, target, new Vector2i(nbr, tmp));
            }
        } else if (source.top.x - 2 == target.bottom.x) {
            if (source.top.y < target.bottom.y && source.bottom.y >= target.top.y) {
                min = Math.max(source.top.y, target.top.y);
                tmp = Math.min(source.bottom.y, target.bottom.y);
                max = Math.max(min, tmp);
                min = min + tmp - max;
                tmp = source.top.x - 1;
                for (int i = min; i <= max; ++i) {
                    if (!Tile.isObstructed(this.map[tmp][i]) || this.map[tmp][i] == Tile.CLOSED_DOOR)
                        return;
                }
                if (max != min) {
                    nbr = this.random.nextInt(max - min) + min;
                } else {
                    nbr = min;
                }
                this.map[tmp][nbr] = tile;
                reconstrucWalls(source, target, new Vector2i(tmp, nbr));
            }
        } else if (source.bottom.x + 2 == target.top.x) {
            if (source.top.y < target.bottom.y && source.bottom.y >= target.top.y) {
                min = Math.max(source.top.y, target.top.y);
                tmp = Math.min(source.bottom.y, target.bottom.y);
                max = Math.max(min, tmp);
                min = min + tmp - max;
                tmp = source.bottom.x + 1;
                for (int i = min; i <= max; ++i) {
                    if (!Tile.isObstructed(this.map[tmp][i]) || this.map[tmp][i] == Tile.CLOSED_DOOR)
                        return;
                }
                if (max != min) {
                    nbr = this.random.nextInt(max - min) + min;
                } else {
                    nbr = min;
                }
                this.map[tmp][nbr] = tile;
                reconstrucWalls(source, target, new Vector2i(tmp, nbr));
            }
        }
    }

    private void reconstrucWalls(Room source1, Room source2, Vector2i replacedTile) {
        // TODO
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

    private Room generateRoom(Room link, boolean isCorridor, boolean withDoor) {
        Room room;
        int x, y;
        int doorX, doorY;
        int height, width;
        if (isCorridor) {
            height = 0;
            width = 0;
        } else {
            height = this.random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT) + MIN_ROOM_HEIGHT;
            width = this.random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH) + MIN_ROOM_WIDTH;
        }
        switch (this.random.nextInt(4)) {
            case 0:
                x = link.bottom.x + 2;
                if (link.bottom.y == link.top.y) {
                    y = link.top.y;
                } else {
                    y = this.random.nextInt(link.bottom.y - link.top.y) + link.top.y;
                }
                doorX = x - 1;
                doorY = y;
                if (isCorridor) {
                    width = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;
                } else {
                    if (this.random.nextBoolean()) {
                        y -= height;
                    }
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
                if (isCorridor) {
                    width = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;
                } else {
                    if (this.random.nextBoolean()) {
                        y -= height;
                    }
                }
                x -= width;
                break;
            case 2:
                if (link.bottom.x == link.top.x) {
                    x = link.top.x;
                } else {
                    x = this.random.nextInt(link.bottom.x - link.top.x) + link.top.x;
                }
                y = link.bottom.y + 2;
                doorX = x;
                doorY = y - 1;
                if (isCorridor) {
                    height = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;
                } else {
                    if (this.random.nextBoolean()) {
                        x -= width;
                    }
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
                if (isCorridor) {
                    height = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;
                } else {
                    if (this.random.nextBoolean()) {
                        x -= width;
                    }
                }
                y -= height;
                break;
        }
        room = new Room(x, y, x + width, y + height);
        if (!isPlaceable(room)) {
            return null;
        }
        room.print();
        if (withDoor) {
            this.map[doorX][doorY] = Tile.CLOSED_DOOR;
        } else {
            this.map[doorX][doorY] = Tile.GROUND;
            reconstrucWalls(link, room, new Vector2i(doorX, doorY));
        }
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
            Map.this.map[x][y] = Tile.PILLAR;
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
