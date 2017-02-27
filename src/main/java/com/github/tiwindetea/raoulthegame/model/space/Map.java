//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model.space;

import com.github.tiwindetea.raoulthegame.events.game.map.TileModificationEvent;
import com.github.tiwindetea.raoulthegame.listeners.game.map.TileModificationListener;
import com.github.tiwindetea.raoulthegame.model.Seed;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Mob;
import com.github.tiwindetea.soundplayer.Sound;
import com.github.tiwindetea.soundplayer.Sounds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;


/**
 * The type Map.
 */
/*
 * Map
 * This class manages a map, by generating it through a procedural algorithm and calculating paths from point A to B.
 *
 * @author Lucas LAZARE
 */
public class Map {

    /*
     * Tuning parameters for the map generation
     */
    private static final int MIN_ROOM_WIDTH = 3; // >= 3
    private static final int MAX_ROOM_WIDTH = 12; // >= MIN_ROOM_WIDTH
    private static final int MIN_ROOM_HEIGHT = 3; // >= 3
    private static final int MAX_ROOM_HEIGHT = 12; // >= MIN_ROOM_HEIGHT
    private static final int MIN_ROOM_NUMBER = 13; // >= 1
    private static final int MAX_ROOM_NUMBER = 23; // >= MIN_ROOM_NUMBER
    private static final int MIN_CORRIDOR_NBR = 4; // >= 1
    private static final int MAX_CORRIDOR_NBR = 8; // >= MIN_CORRIDOR_NBR
    private static final int MIN_CORRIDOR_LENGTH = 6; // >= 1
    private static final int MAX_CORRIDOR_LENGTH = 18; // >= MIN_CORRIDOR_LENGTH
    private static final int MIN_LEVEL_WIDTH = 50; // >= MIN_ROM_WIDTH + MIN_CORRIDOR_LENGTH
    private static final int MAX_LEVEL_WIDTH = 275; // >= MIN_LEVEL_WIDTH
    private static final int MIN_LEVEL_HEIGHT = 50; // >= MIN_ROOM_HEIGHT + MIN_CORRIDOR_LENGTH
    private static final int MAX_LEVEL_HEIGHT = 275; // >= MIN_LEVEL_HEIGHT
    private static final int RETRIES_NBR = 5000; // < Integer.MAX_VALUE / 2
    private static final Tile DEFAULT_DOOR = Tile.HOLE;
    private static final int BULB_MINIMUM_NBR = 2; // >= 0 ;
    private static final int BULB_MAXIMUM_NBR = 3; // >= BULB_MINIMUM_NBR

    private static final int PROBABILITY_UNIT = 100; // > 0
    /* Following values should be between 0 and PROBABILITY_UNIT (both included) */
    private static final int MIN_HOLE_COVERAGE = 25;
    private static final int MAX_HOLE_COVERAGE = 65; // >= MIN_HOLE_COVERAGE
    private static final int HOLE_CHANCE = 5;
    private static final int ROOM_BINDED_TO_CORRIDOR_CHANCE = 95;
    private static final int CORRIDOR_BINDED_TO_CORRIDOR_CHANCE = 25;
    private static final int PILLAR_PROBABILITY = 40;
    private static final int MAX_PILLAR_NBR_PER_ROOM = 5;
    private static final int REBIND_CORRIDOR_TO_CORRIDOR_CHANCE = 30;
    private static final int REBIND_ROOM_TO_CORRIDOR_CHANCE = 15;
    private static final int REBIND_ROOM_TO_ROOM_CHANCE = 10;
    private static final int DOOR_CHANCE = 100;

    private static ArrayList<TileModificationListener> listeners = new ArrayList<>(1);

    public boolean[][] explored;

    private Seed seed;
    private Random random;
    private Vector2i stairsUpPosition;
    private Vector2i stairsDownPosition;
    private ArrayList<Vector2i> bulbPosition;
    private Tile[][] map;

    public static void addListener(TileModificationListener listener) {
        listeners.add(listener);
    }

    /**
     * Instantiates a new Map.
     */
    public Map() {
        this.bulbPosition = new ArrayList<>();
        this.seed = new Seed();
    }

    /**
     * Instantiates a new Map.
     *
     * @param seed the seed
     */
    public Map(Seed seed) {
        this.bulbPosition = new ArrayList<>();
        this.seed = seed;
    }

    /**
     * Gets seed.
     *
     * @return the seed
     */
    public Seed getSeed() {
        return this.seed;
    }

    /**
     * Trigers doors (opens it if it is closed, and vice-versa).<!-- --> If there is no door, does nothing.
     *
     * @param position Position of the tile to trigger.
     */
    public void triggerTile(Vector2i position) {
        if (this.map[position.x][position.y] == Tile.CLOSED_DOOR) {
            this.map[position.x][position.y] = Tile.OPENED_DOOR;
            Sound.player.play(Sounds.DOOR_SOUND);
        } else if (this.map[position.x][position.y] == Tile.OPENED_DOOR) {
            this.map[position.x][position.y] = Tile.CLOSED_DOOR;
            Sound.player.play(Sounds.DOOR_SOUND);
        }

        for (TileModificationListener listener : listeners) {
            listener.handle(new TileModificationEvent(position, this.map[position.x][position.y]));
        }
    }

    /**
     * Gets map copy.
     *
     * @return A copy of the tile map
     */
    public Tile[][] getMapCopy() {
        Tile[][] copy = new Tile[this.map.length][this.map[0].length];
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                copy[i][j] = this.map[i][j];
            }
        }
        return copy;
    }

    /**
     * Gets stairs up position.
     *
     * @return The position of the upgoing stairs
     */
    public Vector2i getStairsUpPosition() {
        return this.stairsUpPosition;
    }

    /**
     * Gets stairs down position.
     *
     * @return The position of the downgoing stairs
     */
    public Vector2i getStairsDownPosition() {
        return this.stairsDownPosition;
    }

    /**
     * Gets the alpha of the seed.
     *
     * @return The alpha of the Seed
     */
    public long getAlphaSeed() {
        return this.seed.getAlphaSeed();
    }

    /**
     * Gets the beta of the seed.
     *
     * @return The beta of the seed
     */
    public long getBetaSeed() {
        return this.seed.getBetaSeed();
    }

    /**
     * Getter for a Tile.<!-- --> Returns null if the given position is not in the map
     *
     * @param position Position of the tile
     * @return The tile present at the given position. null if position is out of the map
     */
    public Tile getTile(Vector2i position) {
        if (position.x < 0 || position.x > this.map.length || position.y < 0 || position.y > this.map[0].length)
            return null;
        return this.map[position.x][position.y];
    }

    /**
     * Get the los of an entity, given its position and its vision range.
     *
     * @param position    the position
     * @param visionRange the vision range
     * @param tries       the number of rays casted for each block
     * @return the LOS
     */
    public boolean[][] getLOS(Vector2i position, int visionRange, int tries) {
        if (visionRange < 0) {
            throw new IllegalArgumentException("visionRange should not be negative (value : " + visionRange + ")");
        }
        if (position.x < 0 || position.y < 0 || position.x > this.map.length || position.y > this.map[0].length) {
            throw new IllegalArgumentException("Watcher outside of the map !");
        }
        if (tries > 4 || tries < 0) {
            throw new IllegalArgumentException("restriction should be between 0 and 4 (value : " + tries + ")");
        }

        boolean[][] LOS = new boolean[2 * visionRange + 1][2 * visionRange + 1];
        int squaredVisionRange = visionRange * visionRange;
        int xlos_shift = position.x - visionRange;
        int ylos_shift = position.y - visionRange;
        int i_end = Math.min(position.x + visionRange + 1, this.map.length);
        int j_end = Math.min(position.y + visionRange + 1, this.map[0].length);

        /* Cast rays from the watcher position to all tiles within its vision radius that also exist in the map */
        /* nb : as LOS is an array, it is in a heap so it is initialised to false */
        for (int i = Math.max(position.x - visionRange, 0); i < i_end; ++i) {
            for (int j = Math.max(position.y - visionRange, 0); j < j_end; ++j) {
                if (Math.pow(i - position.x, 2) + Math.pow(j - position.y, 2) <= squaredVisionRange) {
                    LOS[i - xlos_shift][j - ylos_shift] = this.isVisibleFrom(new Vector2i(i, j), position, tries);
                }
            }
        }
        return LOS;
    }

    /**
     * Gets size of the map.
     *
     * @return the width (Vector2i.x) and the height (Vector2i.y) of the map
     */
    public Vector2i getSize() {
        return new Vector2i(this.map.length, this.map[0].length);
    }

    private boolean isVisibleFrom(Vector2i tilePosition, Vector2i watcherPosition, int tries) {
        float distance = (float) Math.sqrt(Math.pow(tilePosition.x - watcherPosition.x, 2)
                + Math.pow(tilePosition.y - watcherPosition.y, 2));


        if (distance != 0) {

            boolean visible = false;

            for (int j = 0; j < tries && !visible; j++) {
                visible = true;
                float xShifting = (tilePosition.x + j / 2 - watcherPosition.x) / distance;
                float yShifting = (tilePosition.y + j % 2 - watcherPosition.y) / distance;
                float currentX = watcherPosition.x;
                float currentY = watcherPosition.y;
                int x, y;
                int i;

            /* Cast a ray from the watcher to the tile, stopping if a block is found to be obstructed (ie : wall) */
                i = (int) distance;
                while (i > 0 && visible) {
                    currentX += xShifting;
                    currentY += yShifting;
                    x = (int) Math.min(currentX, this.map.length - 1);
                    y = (int) Math.min(currentY, this.map[0].length - 1);
                    if (Tile.isObstructed(this.map[x][y]) && !tilePosition.equals(new Vector2i(x, y))
                            && this.map[x][y] != Tile.HOLE)
                        visible = false;
                    --i;
                }
            }
            return visible;
        }
        return true;
    }

    /**
     * Generates a level using a procedural algorithm and tuning parameters defined hereinabove
     *
     * @param level The level to generate
     * @return A lists of the rooms in this level
     */
    public List<Room> generateLevel(int level) {
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Room> corridors = new ArrayList<>();
        int retries = 0;

        /* Generate the base of the level (its size) and set all the blocks to Tile.UNKNOWN*/
        this.random = this.seed.getRandomizer(level);
        this.map = new Tile[this.random.nextInt(MAX_LEVEL_WIDTH - MIN_LEVEL_WIDTH + 1) + MIN_LEVEL_WIDTH]
                [this.random.nextInt(MAX_LEVEL_HEIGHT - MIN_LEVEL_HEIGHT + 1) + MIN_LEVEL_HEIGHT];
        this.explored = new boolean[this.map.length][this.map[0].length];
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                this.map[i][j] = Tile.UNKNOWN;
                this.explored[i][j] = false;
            }
        }

        /* Generate the numbers of corridors and the number of rooms*/
        int roomNbr = this.random.nextInt(MAX_ROOM_NUMBER - MIN_ROOM_NUMBER + 1) + MIN_ROOM_NUMBER;
        int corridorNbr = this.random.nextInt(MAX_CORRIDOR_NBR - MIN_CORRIDOR_NBR + 1) + MIN_CORRIDOR_NBR;
        int x, y, width, height;
        Room room, corridor;

        /* Dig the first room */
        do {
            width = this.random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
            x = this.random.nextInt(width + 1) + this.map.length / 2 - 4 * width / 5;
            height = this.random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
            y = this.random.nextInt(height + 1) + this.map[0].length / 2 - 4 * height / 5;
            room = new Room(x, y, x + width, y + height);
        } while (!isPlaceable(room));
        rooms.add(room);
        room.print();

        /* Dig the first corridor */
        do {
            corridor = generateRoom(room, true, true);
        } while (corridor == null);
        corridors.add(corridor);

        /* Dig the rest of the map */
        do {
            ++retries;

            // If we are going to generate a corridor
            if (this.random.nextInt(roomNbr - rooms.size() + corridorNbr - corridors.size()) < (corridorNbr - corridors.size())) {
                if (CORRIDOR_BINDED_TO_CORRIDOR_CHANCE >= Map.this.random.nextInt(PROBABILITY_UNIT + 1)) {
                    corridor = generateRoom(corridors.get(this.random.nextInt(corridors.size())), true, false);
                } else {
                    corridor = generateRoom(rooms.get(this.random.nextInt(rooms.size())), true, true);
                }
                if (corridor != null) {
                    retries = 0;
                    corridors.add(corridor);
                }
            } else { // We are going to generate a room
                if (ROOM_BINDED_TO_CORRIDOR_CHANCE >= this.random.nextInt(PROBABILITY_UNIT + 1)) {
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
        // Until (you have enough rooms and corridors),
        // or (you failed RETRIES_NBR times consecutively and have at least MIN_ROOM_NUMBER rooms and MIN_CORRIDOR_NBR corridors)
        // or (you failed 2 * RETRIES_NBR times consecutively)
        while ((rooms.size() < roomNbr || corridors.size() < corridorNbr)
                && (retries < RETRIES_NBR || rooms.size() < MIN_ROOM_NUMBER || corridors.size() < MIN_CORRIDOR_NBR)
                && retries < 2 * RETRIES_NBR);

        /* Randomly remove some walls and make passages (when possible) */
        for (int i = 0; i < rooms.size(); ++i) {
            for (int j = i + 1; j < rooms.size(); ++j) {
                if (REBIND_ROOM_TO_ROOM_CHANCE > this.random.nextInt(PROBABILITY_UNIT + 1)) {
                    rebind(rooms.get(i), rooms.get(j), true);
                }
            }
            for (Room corridor1 : corridors) {
                if (REBIND_ROOM_TO_CORRIDOR_CHANCE > this.random.nextInt(PROBABILITY_UNIT + 1)) {
                    rebind(rooms.get(i), corridor1, true);
                }
            }
        }
        for (int i = 0; i < corridors.size(); ++i) {
            for (int j = i + 1; j < corridors.size(); ++j) {
                if (REBIND_CORRIDOR_TO_CORRIDOR_CHANCE > this.random.nextInt(PROBABILITY_UNIT + 1)) {
                    rebind(corridors.get(i), corridors.get(j), false);
                }
            }
        }

        /* How may bulbs ? */
        int bulbsNbr = this.random.nextInt(BULB_MAXIMUM_NBR - BULB_MINIMUM_NBR + 1) + BULB_MINIMUM_NBR;
        boolean done;

        /* Computes possible positions for stairs and bulbs*/
        Vector2i[] positions = new Vector2i[2 + bulbsNbr];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new Vector2i(0, 0);
        }
        int i = 0;
        retries = 0;
        do {
            for (; i < positions.length; i++) {
                do {
                    room = rooms.get(this.random.nextInt(rooms.size()));
                    positions[i].x = this.random.nextInt(room.bottom.x - room.top.x - 2) + room.top.x + 1;
                    positions[i].y = this.random.nextInt(room.bottom.y - room.top.y - 2) + room.top.y + 1;
                } while (Tile.isObstructed(this.map[positions[i].x][positions[i].y]));
            }
            done = true;
            for (i = 1; i < positions.length && done; i++) {
                if (this.getPath(positions[i - 1], positions[i], true, null) == null) {
                    done = false;
                }
            }
            --i;
            ++retries;
        } while (!done && retries < RETRIES_NBR);

        this.stairsDownPosition = positions[0];
        this.stairsUpPosition = positions[1];
        this.bulbPosition.clear();
        this.bulbPosition.addAll(Arrays.asList(positions).subList(2, positions.length));

        this.map[this.stairsUpPosition.x][this.stairsUpPosition.y] = Tile.STAIR_UP;
        this.map[this.stairsDownPosition.x][this.stairsDownPosition.y] = Tile.STAIR_DOWN;

        rooms.addAll(corridors);
        return rooms;
    }

    /**
     * Demolishes a wall between two rooms to link them, if possible
     *
     * @param source Source room
     * @param target Target room
     * @param door   True to put a door, false to put some ground
     */
    private void rebind(Room source, Room target, boolean door) {
        int min, max, tmp, nbr;
        Tile tile = door ? DEFAULT_DOOR : Tile.GROUND;

        // If both rooms are connected at their top / bottom
        if (source.top.y - 2 == target.bottom.y) {
            if (source.top.x < target.bottom.x && source.bottom.x >= target.top.x) {
                min = Math.max(source.top.x, target.top.x);
                tmp = Math.min(source.bottom.x, target.bottom.x);
                max = Math.max(min, tmp);
                min = min + tmp - max;
                tmp = source.top.y - 1;
                // Don't do anything if the rooms already have a passage
                for (int i = min; i <= max; ++i) {
                    if (!Tile.isObstructed(this.map[i][tmp]) || this.map[i][tmp] == Tile.CLOSED_DOOR)
                        return;
                }

                // Select the part of the wall to destroy
                if (max != min) {
                    nbr = this.random.nextInt(max - min) + min;
                } else {
                    nbr = min;
                }
                this.map[nbr][tmp] = tile;

                // Remake the walls gracefully
                reconstrucWalls(source, target, new Vector2i(nbr, tmp));
            }
            // else If both rooms are connected at their top / bottom (the other combination)
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
            // else If both rooms are connected at their left / right
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
            // else If both rooms are connected at their left / right (the other combination)
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

    /**
     * Reconstructs the wall to remove door's artifact
     *
     * @param source1 First room
     * @param source2 Second room
     * @param tile    Position where the door was
     */
    private void reconstrucWalls(Room source1, Room source2, Vector2i tile) {
        if (source1.top.y == source2.top.y && source1.bottom.y == source2.bottom.y) {
            this.map[tile.x][tile.y - 1] = Tile.WALL_DOWN;
            this.map[tile.x][tile.y + 1] = Tile.WALL_TOP;
        } else if (source1.top.x == source2.top.x && source1.bottom.x == source2.bottom.x) {
            this.map[tile.x - 1][tile.y] = Tile.WALL_RIGHT;
            this.map[tile.x + 1][tile.y] = Tile.WALL_LEFT;
        }
    }

    /**
     * Tells if a room can be placed in the map
     *
     * @param room Concerned room
     * @return True if the the room can be placed
     */
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

    /**
     * Generate a room
     *
     * @param link       Room linked to the room to generate
     * @param isCorridor Indicates if the new room should be a corridor
     * @param withDoor   Indicates if the link should have a door or not
     * @return The generated room. null if the generation process failed
     */
    private Room generateRoom(Room link, boolean isCorridor, boolean withDoor) {
        Room room;
        int x, y;
        int doorX, doorY;
        int height, width;
        if (isCorridor) {
            height = 0;
            width = 0;
        } else {
            height = this.random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
            width = this.random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        }

        /* Choose wether the new room will be binded to given previous room at the North, South, East, or West */
        switch (this.random.nextInt(4)) {
            // EAST
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
                    width = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH + 1) + MIN_CORRIDOR_LENGTH;
                } else {
                    if (this.random.nextBoolean()) {
                        y -= height;
                    }
                }
                break;
            // WEST
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
                    width = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH + 1) + MIN_CORRIDOR_LENGTH;
                } else {
                    if (this.random.nextBoolean()) {
                        y -= height;
                    }
                }
                x -= width;
                break;
            // SOUTH
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
                    height = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH + 1) + MIN_CORRIDOR_LENGTH;
                } else {
                    if (this.random.nextBoolean()) {
                        x -= width;
                    }
                }
                break;
            case 3:
                /* Falls through */
                // NORTH
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
                    height = this.random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH + 1) + MIN_CORRIDOR_LENGTH;
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
            if (DOOR_CHANCE > this.random.nextInt(PROBABILITY_UNIT + 1)) {
                this.map[doorX][doorY] = DEFAULT_DOOR;
            } else {
                this.map[doorX][doorY] = Tile.GROUND;
            }
        } else {
            this.map[doorX][doorY] = Tile.GROUND;
            reconstrucWalls(link, room, new Vector2i(doorX, doorY));
        }
        return room;
    }

    /**
     * Gets bulb position.
     *
     * @return the bulb position
     */
    public ArrayList<Vector2i> getBulbPosition() {
        return this.bulbPosition;
    }

    /**
     * Copies the map.
     *
     * @return a copy of this, with the exception of this.random that's set to null.
     */
    public Map copy() {
        Map map = new Map(this.seed.copy());
        map.random = null;
        map.stairsDownPosition = this.stairsDownPosition.copy();
        map.stairsUpPosition = this.stairsUpPosition.copy();
        map.bulbPosition.addAll(this.bulbPosition.stream().map(Vector2i::copy).collect(Collectors.toList()));
        map.map = this.getMapCopy();
        return map;
    }


    /**
     * Room (level generation)
     *
     * @author Lucas LAZARE
     */
    public class Room {
        public Vector2i top;
        public Vector2i bottom;

        /**
         * Instantiates a new Room.
         *
         * @param top    the top
         * @param bottom the bottom
         */
        public Room(Vector2i top, Vector2i bottom) {
            this.top = top;
            this.bottom = bottom;
        }

        /**
         * Instantiates a new Room.
         *
         * @param x1 the x 1
         * @param y1 the y 1
         * @param x2 the x 2
         * @param y2 the y 2
         */
        public Room(int x1, int y1, int x2, int y2) {
            this(new Vector2i(x1, y1), new Vector2i(x2, y2));
        }

        /**
         * Prints the room in the map, with or without pillar, with or without holes, according to the tunning  parameters defined hereinabove
         */
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

            // If it's not a corridor, try to add some holes or some pillars
            int minimum = Math.min(this.bottom.x - this.top.x, this.bottom.y - this.top.y);
            if (minimum > 2) {
                if (HOLE_CHANCE > Map.this.random.nextInt(PROBABILITY_UNIT + 1)) {
                    int holesNbr = Map.this.random.nextInt(MAX_HOLE_COVERAGE - MIN_HOLE_COVERAGE + 1) + MIN_HOLE_COVERAGE;
                    holesNbr = (holesNbr * (this.top.x - this.bottom.x) * (this.top.y - this.bottom.y)) / PROBABILITY_UNIT;
                    Vector2i start = this.top.copy();
                    Vector2i end = this.bottom.copy();
                    start.x++;
                    start.y++;
                    end.x--;
                    end.y--;
                    switch (Map.this.random.nextInt(4)) {
                        case 0:
                            break;
                        case 1: {
                            Vector2i tmp;
                            tmp = start;
                            start = end;
                            end = tmp;
                            break;
                        }
                        case 2: {
                            int tmp;
                            tmp = start.x;
                            start.x = end.x;
                            end.x = tmp;
                            break;
                        }
                        case 3:
                            /* Falls through */
                        default: {
                            int tmp = start.y;
                            start.y = end.y;
                            end.y = tmp;
                            break;
                        }
                    }
                    int i_dir = (int) Math.signum(end.y - start.y);
                    int j_dir = (int) Math.signum(end.x - start.x);
                    int vol = 0;
                    for (int i = start.y; i != end.y && vol < holesNbr; i += i_dir) {
                        for (int j = start.x; j != end.x && vol < holesNbr; j += j_dir) {
                            Map.this.map[j][i] = Tile.HOLE;
                            ++vol;
                        }
                    }
                }
                int usableSurface = (this.bottom.x - this.top.x - 2) * (this.bottom.y - this.top.y - 2);
                for (int i = 0; i < MAX_PILLAR_NBR_PER_ROOM; ++i) {
                    if (usableSurface > 2 + i && PILLAR_PROBABILITY >= Map.this.random.nextInt(PROBABILITY_UNIT + 1)) {
                        this.randomPillar();
                    }
                }
            }
        }

        /**
         * Generates a pillar at a random place, in the map
         */
        private void randomPillar() {
            int x, y;
            do {
                x = Map.this.random.nextInt(this.bottom.x - this.top.x - 1) + this.top.x + 1;
                y = Map.this.random.nextInt(this.bottom.y - this.top.y - 1) + this.top.y + 1;
            } while (Map.this.map[x][y] != Tile.GROUND && Map.this.map[x][y] != Tile.HOLE);
            Map.this.map[x][y] = Tile.PILLAR;
        }

        @Override
        public String toString() {
            return "Room{" +
                    "top=" + this.top +
                    ", bottom=" + this.bottom +
                    '}';
        }
    }

    /**
     * Gets path, using A* algorithm [Will perhaps be upgraded using the Jump Point Search method].
     *
     * @param departure  the departure
     * @param arrival    the arrival
     * @param ignoreDoor true if the algorithm should consider all doors to be open
     * @param entities   the entities
     * @return the path
     */
    public Stack<Vector2i> getPath(Vector2i departure, Vector2i arrival, boolean ignoreDoor, Collection<LivingThing> entities) {
        if (departure.equals(arrival)) {
            return new Stack<>();
        }
        if (Tile.isObstructed(this.map[arrival.x][arrival.y]) && this.map[arrival.x][arrival.y] != Tile.HOLE)
            return null;
        ArrayList<Node> closedList = new ArrayList<>();
        NodePriorityQueue openList = new NodePriorityQueue(new NodeComparator());
        boolean notDone = true;
        if (entities == null) {
            entities = new ArrayList<>();
        }

        Node dep = new Node(departure, heuristic(departure, arrival), 0, null);
        Node arr = null;
        closedList.add(dep);
        Node node;
        Node openListNode;
        Vector2i next;
        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        openList.add(dep);
        do {
            node = openList.poll();
            closedList.add(node);
            for (Direction direction : directions) {
                next = node.pos.copy().add(direction);
                if (next.equals(arrival)) {
                    notDone = false;
                    arr = new Node(next.copy(), node);
                } else if (((!Tile.isObstructed(this.map[next.x][next.y]))
                        || (this.map[next.x][next.y] == Tile.CLOSED_DOOR && ignoreDoor))
                        && !entities.contains(new Mob(next))) {
                    openListNode = openList.find(next);
                    if (openListNode != null) {
                        computeDistance(node, openListNode);
                    } else if (!closedList.contains(new Node(next))) {
                        openList.add(new Node(next, heuristic(next, arrival), node.distance + 1, node));
                    }
                }
            }
        } while (notDone && openList.size() != 0);

        if (arr == null) {
            return null;
        }
        Stack<Vector2i> path = new Stack<>();
        do {
            path.add(arr.pos);
            arr = arr.parent;
        } while (arr.parent != null);
        return path;
    }

    /**
     * Computes the heuristic. (Path finding)
     */
    private int heuristic(Vector2i departure, Vector2i arrival) {
        return 2 * (Math.abs(departure.x - arrival.x) + Math.abs(departure.y - arrival.y)) / 3;
    }

    /**
     * Computes the distance. (Path finding)
     */
    private void computeDistance(Node parent, Node neighbour) {
        int distance = parent.distance + 1;
        if (distance < neighbour.distance) {
            neighbour.distance = distance;
            neighbour.parent = parent;
        }
    }

    /**
     * Compares two nodes. (Path finding)
     */
    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return Integer.compare(o1.sum(), o2.sum());
        }
    }
}
