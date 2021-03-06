//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////


package com.github.tiwindetea.raoulthegame.model;

import com.github.tiwindetea.raoulthegame.Settings;
import com.github.tiwindetea.raoulthegame.events.game.LevelUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.ScoreUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.SpellSelectionEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityLOSModificationEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityMoveEvent;
import com.github.tiwindetea.raoulthegame.events.game.map.CenterOnTileEvent;
import com.github.tiwindetea.raoulthegame.events.game.map.FogAdditionEvent;
import com.github.tiwindetea.raoulthegame.events.game.map.MapCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.players.PlayerCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.players.PlayerDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.players.PlayerNextTickEvent;
import com.github.tiwindetea.raoulthegame.events.game.static_entities.StaticEntityCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.static_entities.StaticEntityDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.static_entities.StaticEntityLOSDefinitionEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.CastSpellRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.CenterViewRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.InteractionRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.LockViewRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.MoveRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.RequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.SpellSelectedRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.inventory.DropRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.inventory.EquipRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.inventory.UsageRequestEvent;
import com.github.tiwindetea.raoulthegame.listeners.game.GameListener;
import com.github.tiwindetea.raoulthegame.listeners.game.spells.SpellListener;
import com.github.tiwindetea.raoulthegame.listeners.gui.request.RequestListener;
import com.github.tiwindetea.raoulthegame.model.items.Armor;
import com.github.tiwindetea.raoulthegame.model.items.ArmorType;
import com.github.tiwindetea.raoulthegame.model.items.Consumable;
import com.github.tiwindetea.raoulthegame.model.items.ConsumableType;
import com.github.tiwindetea.raoulthegame.model.items.InteractiveObject;
import com.github.tiwindetea.raoulthegame.model.items.Pot;
import com.github.tiwindetea.raoulthegame.model.items.Scroll;
import com.github.tiwindetea.raoulthegame.model.items.StorableObject;
import com.github.tiwindetea.raoulthegame.model.items.StorableObjectType;
import com.github.tiwindetea.raoulthegame.model.items.Weapon;
import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.livings.Mob;
import com.github.tiwindetea.raoulthegame.model.livings.Player;
import com.github.tiwindetea.raoulthegame.model.space.Direction;
import com.github.tiwindetea.raoulthegame.model.space.Map;
import com.github.tiwindetea.raoulthegame.model.space.Tile;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.model.spells.Spell;
import com.github.tiwindetea.raoulthegame.model.spells.SpellsController;
import com.github.tiwindetea.raoulthegame.view.entities.LivingEntityType;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import com.github.tiwindetea.raoulthegame.view.entities.StaticEntityType;
import com.github.tiwindetea.soundplayer.Sound;
import com.github.tiwindetea.soundplayer.Sounds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Game.
 *
 * @author Lucas LAZARE
 */
public class Game implements RequestListener, Runnable, Stoppable {

    private class AlreadyRunningException extends RuntimeException {
    }

    /* Tunning parameters for the entities generation */
    private static final int MIN_MOB_PER_LEVEL = 1;
    private static final int MAX_MOB_PER_LEVEL = 3;
    private static final int MIN_MOB_PER_ROOM = 0; // >= 0 ; extra mobs (doesn't count in min_mob_per_level ; (negative to ignore)
    private static final int MAX_MOB_PER_ROOM = 3; // >= min_mob_per_room ; same
    private static final int MIN_TRAPS_QTT_PER_LEVEL = 8;
    private static final int MAX_TRAPS_QTT_PER_LEVEL = 12;
    private static final int MIN_CHEST_QTT_PER_LEVEL = 2;
    private static final int MAX_CHEST_QTT_PER_LEVEL = 4;
    private static final int MIN_MOBS_LOOTS_PER_LEVEL = 5;
    private static final int MAX_MOBS_LOOTS_PER_LEVEL = 10;
    private static final int BULB_LOS = 2;
    private static final int MINIMUN_TURN_TIME_MS = 100;
    private static final int REFRESH_TIME_MS = 10;


    private static Logger logger = Logger.getLogger(MainPackage.name + ".model.Game");

    private Queue<RequestEvent> requestedEvent = new LinkedList<>();
    private Sounds currentMusic = Sounds.MAIN_MUSIC;

    private volatile boolean isRunning = false;
    private volatile boolean isPaused = false;
    private volatile boolean viewIsLocked = false;

    private boolean selectingSpell = true;
    private SpellType selectedSpell = SpellType.NOT_A_SPELL;

    private static final Level debugLevel = Level.FINEST; //debug

    private final List<GameListener> listeners = new ArrayList<>();
    private int level;
    private Map world;
    private Seed seed;
    private int mobsLootsQtt;
    private String gameName;

    private final List<javafx.util.Pair<Vector2i, Pair<StorableObject>>> objectsOnGround = new LinkedList<>();
    private final List<Pair<InteractiveObject>> interactiveObjects = new ArrayList<>();
    private final List<Pair<Vector2i>> bulbsOn = new ArrayList<>();
    private final List<Pair<Vector2i>> bulbsOff = new ArrayList<>();

    // pair's key : position on the map. pair's value : x = LOS, y = Explore LOS
    private final HashMap<Long, javafx.util.Pair<Vector2i, Vector2i>> ghostsStatic = new HashMap<>();

    private List<Mob> mobs = new ArrayList<>();
    private final List<LivingThing> livingSpells = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private final List<Player> playersOnNextLevel = new ArrayList<>();
    private final HashMap<Long, javafx.util.Pair<LivingThing, Integer>> livingsSharedLOS = new HashMap<>();

    // pair's key : position on the map. pair's value : x = LOS, y = Explore LOS
    private final HashMap<Long, javafx.util.Pair<Vector2i, Vector2i>> ghostsLiving = new HashMap<>();

    private Player currentPlayer;
    private int playerTurn;

    private Spell<?> currentSpell = null;
    private final Collection<LivingThing> currentSpellTargets = new ArrayList<>(8);

    private Pair<Consumable> objectToUse = null;
    private List<Consumable> triggeredObjects = new ArrayList<>();

    private final PriorityQueue<Player> sortedPlayers = new PriorityQueue<>(Comparator.comparingInt(Player::getNumber));

    /* Level generation variables. Charged at the creation of a game */
    private LivingEntityType[] mobsTypes;
    private double[] mobsBaseHP;
    private double[] mobsBaseDef;
    private double[] mobsBaseAttack;
    private double[] mobsHPPerLevel;
    private double[] mobsDefPerLevel;
    private double[] mobsAttackPerLevel;
    private int[] mobsChaseRange;
    private int[] mobsBaseXpGain;
    private int[] mobsXpGainPerLevel;
    private int[] mobsLikelihood;
    private String[] mobsName;
    private int mobsLikelihoodSum;

    private double[] trapsBaseHP;
    private double[] trapsBaseMana;
    private double[] trapsHPPerLevel;
    private double[] trapsManaPerLevel;

    private ArmorType[] lootsArmorType;
    private double[] lootsArmorBaseDefense;
    private double[] lootsArmorBaseAttack;
    private double[] lootsArmorDefensePerLevel;
    private double[] lootsArmorAttackPerLevel;
    private int[] lootsArmorLikelihood;
    private int lootsArmorLikelihoodSum;

    private int[] lootsWeaponRange;
    private double[] lootsWeaponBaseAttack;
    private double[] lootsWeaponManaCost;
    private double[] lootsWeaponAttackPerLevel;
    private double[] lootsWeaponManaCostPerLevel;
    private int[] lootsWeaponLikelihood;
    private int lootsWeaponLikelihoodSum;

    private int[] lootsPotTurns;
    private double[] lootsPotHeal;
    private double[] lootsPotMana;
    private double[] lootsPotAttackMod;
    private double[] lootsPotDefMod;
    private double[] lootsPotHPMod;
    private double[] lootsPotManaMod;
    private double[] lootsPotHealPerLevel;
    private double[] lootsPotManaPerLevel;
    private int[] lootsPotLikelihood;
    private int lootsPotLikelihoodSum;

    private double[] lootsScrollBaseDamagePerTurn;
    private double[] lootsScrollBaseDamageModPerTurn;
    private double[] lootsScrollDamagePerTurnPerLevel;
    private double[] lootsScrollDamageModPerTurnPerLevel;
    private int[] lootsScrollTurns;
    private int[] lootsScrollLikelihood;
    private int lootsScrollLikelihoodSum;

    private int lootsLikelihoodSum;
    private int globalScore;
    private int bulbXp;
    private int bulbXpGainPerLevel;

    private SpellsController spellsController = new SpellsController() {

        private void fireLivingEntityMoveEvent(LivingEntityMoveEvent e) {
            for (GameListener listener : Game.this.listeners) {
                listener.handle(e);
            }
        }

        @Override
        public long createGhostEntity(LivingEntityType entityType, Vector2i position, String description, Direction direction, int LOSRange, int explorationRange) {
            long id = Pair.getUniqueId();
            fire(new LivingEntityCreationEvent(id, entityType, position, direction, description));
            if (LOSRange > 0) {
                boolean[][] tmp = Game.this.world.getLOS(position, LOSRange, 4);
                fire(new LivingEntityLOSDefinitionEvent(id, tmp));
                tmp = Game.this.world.getLOS(position, explorationRange, 4);
                fire(new FogAdditionEvent(position, tmp));
            }
            Game.this.ghostsLiving.put(id, new javafx.util.Pair<>(position, new Vector2i(LOSRange, explorationRange)));
            return id;
        }

        @Override
        public long createGhostEntity(StaticEntityType entityType, Vector2i position, String description, int LOSRange, int explorationRange) {
            long id = Pair.getUniqueId();
            fire(new StaticEntityCreationEvent(id, entityType, position, description));
            Game.this.ghostsStatic.put(id, new javafx.util.Pair<>(position, new Vector2i(LOSRange, explorationRange)));
            return id;
        }

        @Override
        public void moveGhostLivingEntity(long entityId, Vector2i position) {
            javafx.util.Pair pair = Game.this.ghostsLiving.get(entityId);
            ((Vector2i) pair.getKey()).set(position);
            fireLivingEntityMoveEvent(new LivingEntityMoveEvent(entityId, position));
        }

        @Override
        public void deleteGhostStorableEntity(long entityId) {
            fire(new StaticEntityDeletionEvent(entityId));
            Game.this.ghostsStatic.remove(entityId);
        }

        @Override
        public void deleteGhostLivingEntity(long entityId) {
            fire(new LivingEntityDeletionEvent(entityId));
            Game.this.ghostsLiving.remove(entityId);
        }

        @Override
        public void createEntity(LivingThing entity, LivingEntityType aspect, Direction direction) {
            Game.this.livingSpells.add(entity);
            fire(new LivingEntityCreationEvent(entity.getId(), aspect, entity.getPosition(), direction, entity.getDescription()));
        }

        @Override
        public long createEntity(StorableObject entity, Vector2i position, StaticEntityType aspect) {
            Pair<StorableObject> p = new Pair<>(entity);
            Game.this.objectsOnGround.add(new javafx.util.Pair<>(position, p));
            fire(new StaticEntityCreationEvent(p.getId(), aspect, position, p.object.getDescription()));
            return p.getId();
        }

        @Override
        public void explore(Vector2i position, int range) {
            fire(new LivingEntityCreationEvent(Pair.ERROR_VAL, LivingEntityType.ARISTOCRAT_DUCK, position, Direction.DOWN, ""));
            fire(new LivingEntityLOSDefinitionEvent(Pair.ERROR_VAL, Game.this.world.getLOS(position, range, 4)));
            fire(new LivingEntityDeletionEvent(Pair.ERROR_VAL));
        }

        @Override
        public void shareLosWith(long id, int range) {
            for (LivingThing livingSpell : Game.this.livingSpells) {
                if (livingSpell.getId() == id) {
                    Game.this.livingsSharedLOS.put(id, new javafx.util.Pair<>(livingSpell, range));
                    return;
                }
            }
            for (LivingThing mob : Game.this.mobs) {
                if (mob.getId() == id) {
                    Game.this.livingsSharedLOS.put(id, new javafx.util.Pair<>(mob, range));
                    return;
                }
            }
            for (LivingThing player : Game.this.players) {
                if (player.getId() == id) {
                    Game.this.livingsSharedLOS.put(id, new javafx.util.Pair<>(player, range));
                    return;
                }
            }
        }

        @Override
        public void unshareLos(long id) {
            Game.this.livingsSharedLOS.remove(id);
            fire(new LivingEntityLOSDefinitionEvent(id, new boolean[1][1]));
        }

        @Override
        public List<Mob> retrieveMobs() {
            return Game.this.mobs;
        }

        @Override
        public Collection<Player> retrievePlayers() {
            return Game.this.players;
        }

        @Override
        public Collection<LivingThing> retrieveLivingSpells() {
            return Game.this.livingSpells;
        }

        @Override
        public Collection<Pair<InteractiveObject>> retrieveIO() {
            return Game.this.interactiveObjects;
        }

        @Override
        public Collection<javafx.util.Pair<Vector2i, Pair<StorableObject>>> retrieveObjectsOnFloor() {
            return Game.this.objectsOnGround;
        }

        @Override
        public Collection<? extends SpellListener> getSpellListeners() {
            return Game.this.listeners;
        }
    };

    /**
     * Instantiates a new Game.
     *
     * @param gameName the game name (used as the save/load file)
     */
    public Game(String gameName) {
        this.gameName = gameName;
        this.loadMobs();
        this.loadChests();
        this.loadTraps();
        this.loadBulb();
        Spell.setController(this.spellsController);
    }

    /**
     * Initiliases a game. equivalent to Game.initNew(numberOfPlayers, new Seed())
     *
     * @param numberOfPlayers the number of players
     */
    public void initNew(int numberOfPlayers) {
        this.initNew(numberOfPlayers, new Seed());
    }

    /**
     * Initiliases a game.
     *
     * @param numberOfPlayers the number of players
     * @param seed            the seed that should be used for this game
     */
    public void initNew(int numberOfPlayers, Seed seed) {
        this.clearAll();
        ArrayList<Player> players = new ArrayList<>();
        ResourceBundle playersBundle = ResourceBundle.getBundle(MainPackage.name + ".Players");

        for (int i = 0; i < numberOfPlayers; i++) {
            String pString = "player" + i + ".";
            players.add(new Player(
                    playersBundle.getString(pString + "name"),
                    i,
                    Integer.parseInt(playersBundle.getString(pString + "los")),
                    Integer.parseInt(playersBundle.getString(pString + "explorelos")),
                    1,
                    Integer.parseInt(playersBundle.getString(pString + "base-leveling-xp")),
                    Integer.parseInt(playersBundle.getString(pString + "leveling-xp-per-level")),
                    1,
                    Integer.parseInt(playersBundle.getString(pString + "maxStorageCapacity")),
                    Double.parseDouble(playersBundle.getString(pString + "baseHealth")),
                    Double.parseDouble(playersBundle.getString(pString + "baseMana")),
                    Double.parseDouble(playersBundle.getString(pString + "baseAttack")),
                    Double.parseDouble(playersBundle.getString(pString + "baseDef")),
                    Double.parseDouble(playersBundle.getString(pString + "baseAggro")),
                    Double.parseDouble(playersBundle.getString(pString + "healthPerLevel")),
                    Double.parseDouble(playersBundle.getString(pString + "manaPerLevel")),
                    Double.parseDouble(playersBundle.getString(pString + "attackPerLevel")),
                    Double.parseDouble(playersBundle.getString(pString + "defensePerLevel")),
                    Double.parseDouble(playersBundle.getString(pString + "aggroPerLevel"))
            ));
        }
        this.initNew(players, seed, 1);
    }

    /**
     * Initialises a game.
     *
     * @param players the players
     * @param seed    the seed
     * @param level   the level
     */
    public void initNew(Collection<Player> players, Seed seed, int level) {

        this.playerTurn = 0;
        this.level = level;
        this.seed = seed;
        this.world = new Map(this.seed);
        this.clearAll();

        this.players = new ArrayList<>(players.size());
        this.players.addAll(players);
        Tile[][] tiles = new Tile[1][1];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = Tile.UNKNOWN;
            }
        }
        fire(new MapCreationEvent(tiles));

        for (Player player : players) {
            player.addToInventory(new Pair<>(new Pot(4, 15, 15, 0, 0, 0, 0)));
            player.addToInventory(new Pair<>(new Scroll(10, 1, 1)));
            player.addToInventory(new Pair<>(new Weapon(5, 1, 0)));
        }
        this.currentPlayer = this.players.get(0);
        fire(new PlayerNextTickEvent(0));
        this.generateLevel();
        this.globalScore = 0;
    }

    private void loadMobs() {
        /* Loads the mobs stats from the .properties */
        this.mobsLikelihoodSum = 0;
        ResourceBundle mobs = ResourceBundle.getBundle(MainPackage.name + ".Mobs");
        int mobQtt = Integer.parseInt(mobs.getString("mobs.qtt"));
        this.mobsName = new String[mobQtt];
        this.mobsTypes = new LivingEntityType[mobQtt];
        this.mobsBaseHP = new double[mobQtt];
        this.mobsBaseDef = new double[mobQtt];
        this.mobsBaseAttack = new double[mobQtt];
        this.mobsHPPerLevel = new double[mobQtt];
        this.mobsDefPerLevel = new double[mobQtt];
        this.mobsAttackPerLevel = new double[mobQtt];
        this.mobsChaseRange = new int[mobQtt];
        this.mobsBaseXpGain = new int[mobQtt];
        this.mobsXpGainPerLevel = new int[mobQtt];
        this.mobsLikelihood = new int[mobQtt];
        for (int i = 0; i < mobQtt; i++) {
            String mobName = "mob" + i + ".";
            this.mobsTypes[i] = LivingEntityType.parseLivingEntity(mobs.getString(mobName + "type"));
            this.mobsName[i] = mobs.getString(mobName + "name");
            this.mobsBaseHP[i] = Double.parseDouble(mobs.getString(mobName + "base-hp"));
            this.mobsBaseDef[i] = Double.parseDouble(mobs.getString(mobName + "base-def"));
            this.mobsBaseAttack[i] = Double.parseDouble(mobs.getString(mobName + "base-attack"));
            this.mobsHPPerLevel[i] = Double.parseDouble(mobs.getString(mobName + "defense-per-level"));
            this.mobsDefPerLevel[i] = Double.parseDouble(mobs.getString(mobName + "attack-per-level"));
            this.mobsAttackPerLevel[i] = Double.parseDouble(mobs.getString(mobName + "hp-per-level"));
            this.mobsChaseRange[i] = Integer.parseInt(mobs.getString(mobName + "chase-range"));
            this.mobsBaseXpGain[i] = Integer.parseInt(mobs.getString(mobName + "base-xp-gain"));
            this.mobsXpGainPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "xp-gain-per-level"));
            this.mobsLikelihood[i] = Integer.parseInt(mobs.getString(mobName + "likelihood"));
            this.mobsLikelihoodSum += this.mobsLikelihood[i];
        }
    }

    private void loadChests() {
        /* Loads the possible chests from the .properties */
        ResourceBundle loots = ResourceBundle.getBundle(MainPackage.name + ".Chests");
        int armorQtt = Integer.parseInt(loots.getString("armors.qtt"));
        this.lootsArmorType = new ArmorType[armorQtt];
        this.lootsArmorBaseDefense = new double[armorQtt];
        this.lootsArmorBaseAttack = new double[armorQtt];
        this.lootsArmorDefensePerLevel = new double[armorQtt];
        this.lootsArmorAttackPerLevel = new double[armorQtt];
        this.lootsArmorLikelihood = new int[armorQtt];
        this.lootsArmorLikelihoodSum = 0;
        for (int i = 0; i < armorQtt; i++) {
            String armorName = "armor" + i + ".";
            this.lootsArmorType[i] = ArmorType.parseArmorType(loots.getString(armorName + "type"));
            this.lootsArmorBaseDefense[i] = Double.parseDouble(loots.getString(armorName + "base-defense"));
            this.lootsArmorBaseAttack[i] = Double.parseDouble(loots.getString(armorName + "base-attack"));
            this.lootsArmorDefensePerLevel[i] = Double.parseDouble(loots.getString(armorName + "defense-per-level"));
            this.lootsArmorAttackPerLevel[i] = Double.parseDouble(loots.getString(armorName + "attack-per-level"));
            this.lootsArmorLikelihood[i] = Integer.parseInt(loots.getString(armorName + "likelihood"));
            this.lootsArmorLikelihoodSum += this.lootsArmorLikelihood[i];
        }

        int weaponQtt = Integer.parseInt(loots.getString("weapons.qtt"));
        this.lootsWeaponRange = new int[weaponQtt];
        this.lootsWeaponBaseAttack = new double[weaponQtt];
        this.lootsWeaponManaCost = new double[weaponQtt];
        this.lootsWeaponAttackPerLevel = new double[weaponQtt];
        this.lootsWeaponManaCostPerLevel = new double[weaponQtt];
        this.lootsWeaponLikelihood = new int[weaponQtt];
        this.lootsWeaponLikelihoodSum = 0;
        for (int i = 0; i < weaponQtt; i++) {
            String weaponName = "weapon" + i + ".";
            this.lootsWeaponRange[i] = Integer.parseInt(loots.getString(weaponName + "range"));
            this.lootsWeaponBaseAttack[i] = Double.parseDouble(loots.getString(weaponName + "base-attack"));
            this.lootsWeaponManaCost[i] = Double.parseDouble(loots.getString(weaponName + "mana-cost"));
            this.lootsWeaponAttackPerLevel[i] = Double.parseDouble(loots.getString(weaponName + "attack-per-level"));
            this.lootsWeaponManaCostPerLevel[i] = Double.parseDouble(loots.getString(weaponName + "mana-cost-per-level"));
            this.lootsWeaponLikelihood[i] = Integer.parseInt(loots.getString(weaponName + "likelihood"));
            this.lootsWeaponLikelihoodSum += this.lootsWeaponLikelihood[i];
        }

        int potQtt = Integer.parseInt(loots.getString("pots.qtt"));
        this.lootsPotTurns = new int[potQtt];
        this.lootsPotHeal = new double[potQtt];
        this.lootsPotMana = new double[potQtt];
        this.lootsPotAttackMod = new double[potQtt];
        this.lootsPotDefMod = new double[potQtt];
        this.lootsPotHPMod = new double[potQtt];
        this.lootsPotManaMod = new double[potQtt];
        this.lootsPotHealPerLevel = new double[potQtt];
        this.lootsPotManaPerLevel = new double[potQtt];
        this.lootsPotLikelihood = new int[potQtt];
        this.lootsPotLikelihoodSum = 0;
        for (int i = 0; i < potQtt; i++) {
            String potName = "pot" + i + ".";
            this.lootsPotTurns[i] = Integer.parseInt(loots.getString(potName + "turns"));
            this.lootsPotHeal[i] = Double.parseDouble(loots.getString(potName + "base-hp"));
            this.lootsPotMana[i] = Double.parseDouble(loots.getString(potName + "base-mana"));
            this.lootsPotAttackMod[i] = Double.parseDouble(loots.getString(potName + "attack-mod"));
            this.lootsPotDefMod[i] = Double.parseDouble(loots.getString(potName + "def-mod"));
            this.lootsPotHPMod[i] = Double.parseDouble(loots.getString(potName + "hp-mod"));
            this.lootsPotManaMod[i] = Double.parseDouble(loots.getString(potName + "mana-mod"));
            this.lootsPotHealPerLevel[i] = Double.parseDouble(loots.getString(potName + "hp-per-level"));
            this.lootsPotManaPerLevel[i] = Double.parseDouble(loots.getString(potName + "mana-per-level"));
            this.lootsPotLikelihood[i] = Integer.parseInt(loots.getString(potName + "likelihood"));
            this.lootsPotLikelihoodSum += this.lootsPotLikelihood[i];
        }

        int scrollQtt = Integer.parseInt(loots.getString("scrolls.qtt"));
        this.lootsScrollBaseDamagePerTurn = new double[scrollQtt];
        this.lootsScrollBaseDamageModPerTurn = new double[scrollQtt];
        this.lootsScrollDamagePerTurnPerLevel = new double[scrollQtt];
        this.lootsScrollDamageModPerTurnPerLevel = new double[scrollQtt];
        this.lootsScrollTurns = new int[scrollQtt];
        this.lootsScrollLikelihood = new int[scrollQtt];
        this.lootsScrollLikelihoodSum = 0;
        for (int i = 0; i < scrollQtt; i++) {
            String scrollName = "scroll" + i + ".";
            this.lootsScrollBaseDamagePerTurn[i] = Double.parseDouble(loots.getString(scrollName + "base-damage-per-turn"));
            this.lootsScrollBaseDamageModPerTurn[i] = Double.parseDouble(loots.getString(scrollName + "base-damage-mod-per-turn"));
            this.lootsScrollDamagePerTurnPerLevel[i] = Double.parseDouble(loots.getString(scrollName + "damage-per-turn-per-level"));
            this.lootsScrollDamageModPerTurnPerLevel[i] = Double.parseDouble(loots.getString(scrollName + "damage-mod-per-turn-per-level"));
            this.lootsScrollTurns[i] = Integer.parseInt(loots.getString(scrollName + "turns"));
            this.lootsScrollLikelihood[i] = Integer.parseInt(loots.getString(scrollName + "likelihood"));
            this.lootsScrollLikelihoodSum += this.lootsScrollLikelihood[i];
        }

        this.lootsLikelihoodSum = this.lootsScrollLikelihoodSum + this.lootsPotLikelihoodSum + this.lootsWeaponLikelihoodSum + this.lootsArmorLikelihoodSum;
    }

    private void loadTraps() {
        /* Loads the possible traps from the .properties */
        ResourceBundle traps = ResourceBundle.getBundle(MainPackage.name + ".Traps", Locale.getDefault());
        int trapsQtt = Integer.parseInt(traps.getString("traps.qtt"));
        this.trapsBaseHP = new double[trapsQtt];
        this.trapsBaseMana = new double[trapsQtt];
        this.trapsHPPerLevel = new double[trapsQtt];
        this.trapsManaPerLevel = new double[trapsQtt];
        for (int i = 0; i < trapsQtt; i++) {
            String trapName = "trap" + i + ".";
            this.trapsBaseHP[i] = Double.parseDouble(traps.getString(trapName + "base-hp"));
            this.trapsBaseMana[i] = Double.parseDouble(traps.getString(trapName + "base-mana"));
            this.trapsHPPerLevel[i] = Double.parseDouble(traps.getString(trapName + "hp-per-level"));
            this.trapsManaPerLevel[i] = Double.parseDouble(traps.getString(trapName + "mana-per-level"));
        }
    }

    private void loadBulb() {
        ResourceBundle bulbs = ResourceBundle.getBundle(MainPackage.name + ".StaticEntity", Locale.getDefault());
        this.bulbXp = Integer.parseInt(bulbs.getString("lit-bulb.xp.gain"));
        this.bulbXpGainPerLevel = Integer.parseInt(bulbs.getString("lit-bulb.xp.gain-per-level"));
    }

    /**
     * Adds a Gamelistener.
     *
     * @param listener the listener
     */
    public void addGameListener(GameListener listener) {
        this.listeners.add(listener);
        LivingThing.addGameListener(listener);
        Map.addListener(listener);
    }

    private GameListener[] getGameListeners() {
        return this.listeners.toArray(new GameListener[this.listeners.size()]);
    }

    private void fire(LivingEntityCreationEvent event) {
        for (GameListener listener : this.getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(LivingEntityDeletionEvent event) {
        for (GameListener listener : this.getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(LivingEntityLOSDefinitionEvent event) {
        for (GameListener listener : this.getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(LivingEntityLOSModificationEvent event) {
        for (GameListener listener : this.getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(ScoreUpdateEvent event) {
        for (GameListener listener : getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(MapCreationEvent event) {
        for (GameListener listener : this.getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(PlayerCreationEvent event) {
        for (GameListener listener : this.getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(StaticEntityCreationEvent event) {
        for (GameListener listener : this.getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(StaticEntityDeletionEvent event) {
        for (GameListener listener : this.getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(StaticEntityLOSDefinitionEvent event) {
        for (GameListener listener : this.getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(PlayerNextTickEvent event) {
        for (GameListener listener : getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(FogAdditionEvent event) {
        for (GameListener listener : getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(CenterOnTileEvent event) {
        for (GameListener listener : getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(PlayerDeletionEvent event) {
        for (GameListener listener : getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(LevelUpdateEvent event) {
        for (GameListener listener : getGameListeners()) {
            listener.handle(event);
        }
    }

    private void fire(SpellSelectionEvent event) {
        for (GameListener listener : this.listeners) {
            listener.handle(event);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(DropRequestEvent e) {
        this.requestedEvent.add(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(InteractionRequestEvent e) {
        this.requestedEvent.add(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(UsageRequestEvent e) {
        this.requestedEvent.add(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(MoveRequestEvent e) {
        this.requestedEvent.add(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(CenterViewRequestEvent e) {
        this.requestedEvent.add(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(EquipRequestEvent e) {
        this.requestedEvent.add(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(LockViewRequestEvent e) {
        this.viewIsLocked = !this.viewIsLocked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(CastSpellRequestEvent e) {
        this.requestedEvent.add(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(SpellSelectedRequestEvent e) {
        this.requestedEvent.add(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        this.isRunning = false;
    }

    /**
     * generates a level
     */
    private void generateLevel() {

        Sound.player.play(Sounds.NEXT_FLOOR_SOUND);
        System.out.println("Entering level " + this.level + " of seed [" + this.seed.getAlphaSeed() + " ; " + this.seed.getBetaSeed() + "]");
        fire(new LevelUpdateEvent(this.level));

		/* Generate the level and bulbs to turn off */
        List<Map.Room> rooms = this.world.generateLevel(this.level);
        fire(new MapCreationEvent(this.world.getMapCopy()));

        for (Vector2i bulb : this.world.getBulbPosition()) {
            Pair<Vector2i> p = new Pair<>(bulb);
            Pair<Vector2i> p2 = new Pair<>(bulb);
            this.bulbsOn.add(p);
            this.bulbsOff.add(p2);
            fire(new StaticEntityCreationEvent(p2.getId(), StaticEntityType.UNLIT_BULB, bulb, "An Unlit bulb"));
            fire(new StaticEntityCreationEvent(p.getId(), StaticEntityType.LIT_BULB, bulb, "A Lit bulb"));
            fire(new StaticEntityLOSDefinitionEvent(
                    p.getId(),
                    this.world.getLOS(bulb, BULB_LOS, 4)
            ));
        }

        Random random = this.seed.getRandomizer(this.level);
        int chestsNbr = random.nextInt(MAX_CHEST_QTT_PER_LEVEL - MIN_CHEST_QTT_PER_LEVEL + 1) + MIN_CHEST_QTT_PER_LEVEL;
        int trapsNbr = random.nextInt(MAX_TRAPS_QTT_PER_LEVEL - MIN_TRAPS_QTT_PER_LEVEL + 1) + MIN_TRAPS_QTT_PER_LEVEL;
        int selection;
        Vector2i pos;

		/* Generate traps */
        for (int i = 0; i < trapsNbr; i++) {
            selection = random.nextInt(this.trapsBaseHP.length);
            pos = selectRandomGroundPosition(rooms, random);
            Pair<InteractiveObject> pair = new Pair<>(new InteractiveObject(pos,
                    this.trapsBaseHP[selection] + this.trapsHPPerLevel[selection] * random.nextInt(3) + this.level - 1,
                    this.trapsBaseMana[selection] + this.trapsManaPerLevel[selection] * random.nextInt(3) + this.level - 1));
            this.interactiveObjects.add(pair);
            fire(new StaticEntityCreationEvent(pair.getId(), StaticEntityType.TRAP, pos, pair.object.getDescription()));
        }

		/* Generate some chests */
        for (int i = 0; i < chestsNbr; i++) {
            Pair<InteractiveObject> pair;
            //TODOchestLevel = randomExcept.nextInt(3) + this.level - 1;
            pos = selectRandomGroundPosition(rooms, random);
            pair = new Pair<>(new InteractiveObject(pos, lootSelector(random)));
            this.interactiveObjects.add(pair);
            fire(new StaticEntityCreationEvent(pair.getId(), StaticEntityType.CHEST, pos, pair.object.getDescription()));
        }

		/* Generate the mobs */
        Mob.setMap(this.world);
        int mobsNbr = random.nextInt(MAX_MOB_PER_LEVEL - MIN_MOB_PER_LEVEL + 1) + MIN_MOB_PER_LEVEL;
        this.mobs = new ArrayList<>(mobsNbr);
        for (int i = 0; i < mobsNbr; ++i) {
            do {
                pos = selectRandomGroundPosition(rooms, random);
            } while (pos.equals(this.world.getStairsUpPosition()));
            this.addRandomMob(pos, random);
        }

        if (MIN_MOB_PER_ROOM >= 0) {
            for (Map.Room room : rooms) {
                mobsNbr = random.nextInt(MAX_MOB_PER_ROOM - MIN_MOB_PER_ROOM + 1) + MIN_MOB_PER_ROOM;
                for (int i = 0; i < mobsNbr; i++) {
                    ArrayList<Map.Room> r = new ArrayList<>();
                    r.add(room);
                    do {
                        pos = selectRandomGroundPosition(r, random);
                    } while (pos.equals(this.world.getStairsUpPosition()));
                    this.addRandomMob(pos, random);
                }
            }
        }

        this.mobsLootsQtt = random.nextInt(MAX_MOBS_LOOTS_PER_LEVEL - MIN_MOBS_LOOTS_PER_LEVEL + 1) + MIN_MOBS_LOOTS_PER_LEVEL;

        Spell.setMap(this.world);
        this.players.addAll(this.playersOnNextLevel);
        this.playersOnNextLevel.clear();
        this.triggeredObjects.clear();

        this.sortedPlayers.clear();

		/* Move the players to the right position */
        for (Player player : this.players) {
            this.sortedPlayers.add(player);
            fire(new LivingEntityCreationEvent(
                    player.getId(),
                    player.getGType(),
                    new Vector2i(0, 0),
                    Direction.DOWN,
                    player.getDescription()
            ));
            if (player.hasFallen) {
                do {
                    player.setPosition(this.selectRandomGroundPosition(
                            rooms,
                            new Random(this.world.getBetaSeed() * this.level)
                    ));
                } while (Tile.isObstructed(this.world.getTile(player.getPosition())));
                player.hasFallen = false;
            } else {
                player.heal(player.getMaxHitPoints() / 3);
                player.addMana(player.getMaxMana());
                player.setPosition(this.world.getStairsUpPosition());
            }
            fire(new LivingEntityLOSDefinitionEvent(
                    player.getId(),
                    this.world.getLOS(player.getPosition(), player.getLos(), 4)
            ));

            explore(player.getPosition(), this.world.getLOS(player.getPosition(), player.getExploreLOS(), 4));
        }

        ArrayList<LivingThing> livings = new ArrayList<>(this.mobs.size()
                + this.players.size()
                + this.playersOnNextLevel.size());

        livings.addAll(this.players);
        livings.addAll(this.playersOnNextLevel);
        //livings.addAll(this.mobs); todo : uncomment if mobs have spells
        livings.forEach(l -> {
            for (Spell spell : l.getSpells()) {
                if (spell != null) {
                    spell.nextFloor();
                }
            }
        });

        fire(new CenterOnTileEvent(this.players.get(0).getPosition()));
    }

    //-------------------------------------//
    private Vector2i selectRandomGroundPosition(List<Map.Room> rooms, Random random) {
        Map.Room selectedRoom;
        Vector2i pos = new Vector2i();
        do {
            selectedRoom = rooms.get(random.nextInt(rooms.size()));
            pos.x = random.nextInt(selectedRoom.bottom.x - selectedRoom.top.x + 1) + selectedRoom.top.x;
            pos.y = random.nextInt(selectedRoom.bottom.y - selectedRoom.top.y + 1) + selectedRoom.top.y;
        } while (!isAccessible(pos));
        return pos;
    }

    /**
     * Step into the next tick (next player)
     */
    private void nextTick() {
        if (this.players.isEmpty()) {
            return;
        }
        this.objectToUse = null;
        do {
            ++this.playerTurn;
        }
        while (this.playerTurn < this.players.size()
                && this.players.get(this.playerTurn).getFloor() != this.level);

        if (this.playerTurn >= this.players.size()) {
            this.playerTurn = 0;
            this.currentPlayer = this.players.get(0);
            long time = System.currentTimeMillis();
            this.nextTurn();
            time = System.currentTimeMillis() - time;
            if (time < MINIMUN_TURN_TIME_MS) {
                try {
                    Thread.sleep(MINIMUN_TURN_TIME_MS - time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.currentPlayer = this.players.get(this.playerTurn);
        }
        if (!this.players.isEmpty()) {
            fire(new PlayerNextTickEvent(this.currentPlayer.getNumber()));
        }

        if (this.players.size() == 0 && this.playersOnNextLevel.size() > 0) {
            ++this.level;
            this.clearLevel();
            this.generateLevel(); // Hot dog !
            this.treatRequestEvent((SpellSelectedRequestEvent) null);
        }

        if (this.viewIsLocked) {
            fire(new CenterOnTileEvent(this.currentPlayer.getPosition()));
        }
    }

    /**
     * Step into the next turn (all entities live)
     */
    private void nextTurn() {
        HashMap<Integer, Pair<StorableObject>> objectsJustDropped = new HashMap<>();
        ArrayList<Mob> mobsToDelete = new ArrayList<>(3);

        this.nextTurnPlayers(objectsJustDropped);
        this.updateLivingsSpells();
        this.nextTurnMobs(mobsToDelete);
        this.killDeads(mobsToDelete);
        this.updateConsumables();
        this.computeWorldInteractions(objectsJustDropped);

        this.recomputeLoses();
    }

    private void nextTurnPlayers(HashMap<Integer, Pair<StorableObject>> objectsJustDropped) {
        Vector2i pos;
        LivingThing target;

        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);
            Vector2i playerPos = player.getPosition();
            int distance;
            Pair<StorableObject> drop;
            if ((pos = player.getRequestedMove()) != null) {
            /*See if the player can move as he wanted to */
                distance = Math.abs(playerPos.x - pos.x) + Math.abs(playerPos.y - pos.y);
                if (distance <= 1 && isAccessible(pos)) {
                    player.setPosition(pos);
                    fire(new LivingEntityLOSDefinitionEvent(player.getId(), this.world.getLOS(pos, player.getLos(), 4)));
                } else if (distance <= 1 && this.world.getTile(pos) == Tile.HOLE) {
                    player.setFloor(this.level + 1);
                    player.setPosition(new Vector2i(0, 0));
                    player.hasFallen = true;
                    player.damage(player.getMaxHitPoints() / 5, null);
                    fire(new LivingEntityDeletionEvent(player.getId()));
                    this.playersOnNextLevel.add(player);
                    this.players.remove(i);
                    --i;
                } else {
                    logger.log(debugLevel, "Player " + player.getName() + " requested an invalid move ! (from "
                            + playerPos + " to " + pos + ")");
                    player.moveRequestDenied();
                }
            } else if ((target = player.getRequestedAttack()) != null) {
                pos = target.getPosition();
                if (playerPos.squaredDistance(pos) <= Math.pow(player.getAttackRange(), 2)) {
                    boolean[][] los = this.world.getLOS(playerPos, player.getLos(), 4);
                    if (los[los.length / 2 - playerPos.x + pos.x][los[0].length / 2 - playerPos.y + pos.y]) {
                        int j = this.mobs.indexOf(new Mob(pos));
                        if (j >= 0) {
                            player.attack(this.mobs.get(j));
                        } else {
                            // Healing by 20%, because why not
                            player.heal(player.getMaxHitPoints() / 5);
                        }
                    }
                }
                player.setRequestedAttack(null);
            } else if ((pos = player.getRequestedInteraction()) != null) {
            /* Make the player to interact with the map */
                distance = Math.max(Math.abs(playerPos.x - pos.x), Math.abs(playerPos.y - pos.y));
                if (distance == 1) {
                    this.world.triggerTile(pos);
                }
                player.setRequestedInteraction(null);
            } else if ((drop = player.getObjectToDrop()) != null) {
                pos = player.getDropPos();
                distance = Math.abs(playerPos.x - pos.x) + Math.abs(playerPos.y - pos.y);
                if (drop.object != null && distance <= 1) {
                    player.dropRequestAccepted();
                    if (this.world.getTile(pos) != Tile.HOLE) {
                        objectsJustDropped.put(new Integer(player.getNumber()), drop);
                        fire(new StaticEntityCreationEvent(
                                drop.getId(),
                                drop.object.getGType(),
                                pos,
                                drop.object.getDescription()
                        ));
                        this.objectsOnGround.add(new javafx.util.Pair<>(pos, drop));
                    }
                }
            } else {
                String msg = "Nothing to do with player " + player.getName()
                        + " (player #" + (player.getNumber() + 1) + ")";
                logger.log(debugLevel, msg);
            }
        }
    }

    private void nextTurnMobs(ArrayList<Mob> mobToDelete) {
        Vector2i pos;
        LivingThing target;

		/* Letting the mobs to play */
        for (Mob mob : this.mobs) {
            if (mob.isAlive()) {
                mob.live(this.mobs, this.players, this.livingSpells, this.world.getLOS(mob.getPosition(), mob.getChaseRange(), 4));
                if ((pos = mob.getRequestedMove()) != null) {
                    int distance = Math.abs(mob.getPosition().x - pos.x) + Math.abs(mob.getPosition().y - pos.y);
                    if (distance <= 1 && isAccessible(pos) && !this.livingSpells.contains(new Mob(pos))) {
                        mob.setPosition(pos);
                    }
                } else if ((target = mob.getRequestedAttack()) != null) {
                    pos = target.getPosition();
                    int distance = Math.abs(mob.getPosition().x - pos.x) + Math.abs(mob.getPosition().y - pos.y);
                    if (distance <= 1) {
                        mob.attack(target);
                    }
                }
            } else {
                mobToDelete.add(mob); // Woops, you're dead
                for (Player player : this.players) {
                    player.xp(mob.getXpGain());
                }
            }
        }
    }

    private void updateLivingsSpells() {
        ArrayList<LivingThing> livings = new ArrayList<>(this.mobs.size() + this.players.size());
        livings.addAll(this.players);
        //livings.addAll(this.mobs); todo : uncomment if mobs have spells

        for (LivingThing living : livings) {
            for (Spell spell : living.getSpells()) {
                if (spell != null) {
                    if (spell.isPassive()) {
                        spell.update(null);
                    } else if (spell.isAOE()) {
                        Vector2i pos = spell.getSpellSource();
                        int range = spell.getRange();
                        if (pos != null) {
                            LinkedList<LivingThing> targets = livings.stream()
                                    .filter(thing -> pos.squaredDistance(thing.getPosition()) < range)
                                    .collect(Collectors.toCollection(LinkedList::new));
                            spell.update(targets);
                        } else {
                            spell.update(null);
                        }
                    } else {
                        spell.update(null);
                    }
                }
            }
        }
    }

    private void killDeads(ArrayList<Mob> mobToDelete) {
        Random random = new Random();
        for (Mob mob : mobToDelete) {
            fire(new LivingEntityDeletionEvent(mob.getId()));
            if (random.nextInt(101) < 100 * this.mobsLootsQtt / this.mobs.size()) {
                this.mobsLootsQtt--;
                Pair<StorableObject> loot = new Pair<>(lootSelector(random));
                this.objectsOnGround.add(new javafx.util.Pair<>(mob.getPosition(), loot));
                fire(new StaticEntityCreationEvent(
                        loot.getId(),
                        loot.object.getGType(),
                        mob.getPosition(),
                        loot.object.getDescription()
                ));
            }
            this.mobs.remove(mob);
        }

        for (LivingThing livingSpell : this.livingSpells) {
            if (!livingSpell.isAlive()) {
                fire(new LivingEntityDeletionEvent(livingSpell.getId()));
                livingsSharedLOS.remove(new Long(livingSpell.getId()));
            }
        }
        this.livingSpells.removeIf(spell -> !spell.isAlive());

        ArrayList<Player> playerToDelete = new ArrayList<>();
        for (Player player : this.players) {
            if (!player.isAlive()) {
                playerToDelete.add(player);
            }
        }

        for (Player player : playerToDelete) {
            fire(new LivingEntityLOSDefinitionEvent(
                    player.getId(),
                    new boolean[0][]
            ));
            fire(new LivingEntityDeletionEvent(player.getId()));
            this.players.remove(player);
            this.deletePlayer(player);
            for (int number = player.getNumber(); number < this.players.size(); number++) {
                this.players.get(number).setNumber(this.players.get(number).getNumber() - 1);
            }
            for (Player player1 : this.playersOnNextLevel) {
                player1.setNumber(player1.getNumber() - 1);
            }
        }
        if (this.players.isEmpty() && this.playersOnNextLevel.isEmpty()) {
            this.isRunning = false;
        }
    }

    private void updateConsumables() {
        ArrayList<Consumable> consumableToDelete = this.triggeredObjects.stream()
                /* if its effect is finished */
                .filter(Consumable::nextTick)
                .collect(Collectors.toCollection(ArrayList::new));

        for (Consumable consumable : consumableToDelete) {
            this.triggeredObjects.remove(consumable);
        }
    }

    private void computeWorldInteractions(HashMap<Integer, Pair<StorableObject>> objectsJustDropped) {
        Vector2i pos;
        /* Scanning for loots&chests to give to players, for traps, next level & light bulb */
        for (int i = 0; i < this.players.size(); ++i) {
            Player player = this.players.get(i);
            pos = player.getPosition();

			/* Loots */
            ListIterator<javafx.util.Pair<Vector2i, Pair<StorableObject>>> iter = this.objectsOnGround.listIterator();
            while (iter.hasNext()) {
                javafx.util.Pair<Vector2i, Pair<StorableObject>> objPair = iter.next();
                Pair<StorableObject> objDroppedByPlayer = objectsJustDropped.get(new Integer(player.getNumber()));
                if (objPair.getKey().equals(pos)
                        && (objDroppedByPlayer == null || objDroppedByPlayer.getId() != objPair.getValue().getId())) {

                    Pair<StorableObject> obj = null;


                    if (objPair.getValue().object.getType() == StorableObjectType.ARMOR) {
                        if (Settings.autoEquip) {
                            Pair<Armor> arm = player.autoEquipArmor(new Pair<>(objPair.getValue(), false), Settings.autoEquipCanDrop);
                            if (arm == null) {
                                fire(new StaticEntityDeletionEvent(objPair.getValue().getId()));
                                iter.remove();
                            } else {
                                if (arm.getId() != Pair.ERROR_VAL) {
                                    fire(new StaticEntityDeletionEvent(objPair.getValue().getId()));
                                    fire(new StaticEntityCreationEvent(
                                            arm.getId(),
                                            arm.object.getGType(),
                                            objPair.getKey(),
                                            arm.object.getDescription()
                                    ));
                                    iter.set(new javafx.util.Pair<>(objPair.getKey(), new Pair<>(arm)));
                                } else {
                                    obj = objPair.getValue();
                                }
                            }
                        } else if (Settings.simpleAutoEquip) {
                            if (player.simpleAutoEquipArmor(new Pair<>(objPair.getValue(), false))) {
                                fire(new StaticEntityDeletionEvent(objPair.getValue().getId()));
                                iter.remove();
                            } else {
                                obj = objPair.getValue();
                            }
                        } else {
                            obj = objPair.getValue();
                        }
                    } else if (objPair.getValue().object.getType() == StorableObjectType.WEAPON) {
                        if (Settings.autoEquip) {
                            Pair<Weapon> weap = player.autoEquipWeapon(new Pair<>(objPair.getValue(), false), Settings.autoEquipCanDrop);
                            if (weap == null) {
                                fire(new StaticEntityDeletionEvent(objPair.getValue().getId()));
                                iter.remove();
                            } else {
                                if (weap.getId() != Pair.ERROR_VAL) {
                                    fire(new StaticEntityDeletionEvent(objPair.getValue().getId()));
                                    fire(new StaticEntityCreationEvent(
                                            weap.getId(),
                                            weap.object.getGType(),
                                            objPair.getKey(),
                                            weap.object.getDescription()
                                    ));
                                    iter.set(new javafx.util.Pair<>(objPair.getKey(), new Pair<>(weap)));
                                } else {
                                    obj = objPair.getValue();
                                }
                            }
                        } else if (Settings.simpleAutoEquip) {
                            if (player.simpleAutoEquipWeapon(new Pair<>(objPair.getValue(), false))) {
                                fire(new StaticEntityDeletionEvent(objPair.getValue().getId()));
                                iter.remove();
                            } else {
                                obj = objPair.getValue();
                            }
                        } else {
                            obj = objPair.getValue();
                        }
                    } else {
                        obj = objPair.getValue();
                    }

                    if (obj != null && obj.object != null && player.addToInventory(obj)) {
                        fire(new StaticEntityDeletionEvent(objPair.getValue().getId()));
                        iter.remove();
                    }
                }
            }

			/* Next floor */
            if (!player.isARequestPending()) {
                if (player.getPosition().equals(this.world.getStairsDownPosition())) {
                    player.setFloor(this.level + 1);
                    player.setPosition(new Vector2i(0, 0));
                    fire(new LivingEntityDeletionEvent(player.getId()));
                    this.playersOnNextLevel.add(player);
                    this.players.remove(i);
                    --i;
                }
            }

			/* bulbs */
            for (int j = 0; j < this.bulbsOn.size(); j++) {
                if (this.bulbsOn.get(j).object.equals(player.getPosition())) {
                    fire(new StaticEntityDeletionEvent(this.bulbsOn.get(j).getId()));
                    this.bulbsOn.remove(j);
                    Sound.player.play(Sounds.BULB_SOUND);
                    player.heal(2 * player.getMaxHitPoints() / 3);
                    player.xp(this.bulbXp + this.bulbXpGainPerLevel * this.level);
                    ++this.globalScore;
                    fire(new ScoreUpdateEvent(this.globalScore));
                }
            }

			/* chests and traps */
            ArrayList<Pair<InteractiveObject>> objectsToDelete = this.interactiveObjects.stream()
                    .filter(interactiveObject -> interactiveObject.object.getPosition().equals(player.getPosition()))
                    .filter(interactiveObject -> interactiveObject.object.trigger(player))
                    .collect(Collectors.toCollection(ArrayList::new));

            for (Pair<InteractiveObject> interactiveObject : objectsToDelete) {
                fire(new StaticEntityDeletionEvent(interactiveObject.getId()));
                this.interactiveObjects.remove(interactiveObject);
            }
        }
    }

    //-------------------------------------//
    private boolean isAccessible(Vector2i pos) {
        return !(Tile.isObstructed(this.world.getTile(pos))
                || this.mobs.contains(new Mob(pos)));
    }

    /**
     * Loads a save.
     */
    public void loadSave() {
        try {
            Scanner file = new Scanner(new FileInputStream(this.gameName));
            clearAll();
            Tile[][] tiles = new Tile[1][1];
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    tiles[i][j] = Tile.UNKNOWN;
                }
            }
            fire(new MapCreationEvent(tiles));
            this.seed = Seed.parseSeed(file.nextLine());
            this.world = new Map(this.seed);
            String str = file.nextLine();
            this.level = Integer.parseInt(str.substring(str.indexOf('=') + 1));
            str = file.nextLine();
            this.globalScore = Integer.parseInt(str.substring(str.indexOf('=') + 1));
            this.players.clear();
            while (file.hasNext()) {
                str = file.nextLine();
                Player tmp = Player.parsePlayer(str);
                this.players.add(tmp);
                tmp.setFloor(this.level);
            }
            file.close();
            this.currentPlayer = this.players.get(0);
            this.currentPlayer.test();
            fire(new PlayerNextTickEvent(this.currentPlayer.getNumber()));
            fire(new ScoreUpdateEvent(this.globalScore));
            this.generateLevel();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open the save file");
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("Corrupted save");
            throw e;
        }
    }

    //-------------------------------------//
    private void save() {
        PriorityQueue<Player> playerPriorityQueue = new PriorityQueue<>(Comparator.comparingInt(Player::getNumber));

        playerPriorityQueue.addAll(this.playersOnNextLevel);
        playerPriorityQueue.addAll(this.players);
        try {
            FileWriter file = new FileWriter(new File(this.gameName));
            file.write(this.seed.toString() + "\n");
            file.write("level=" + this.level + "\n");
            file.write("score=" + this.globalScore + "\n");
            while (!playerPriorityQueue.isEmpty()) {
                Player p = playerPriorityQueue.poll();
                if (!p.getPosition().equals(world.getStairsUpPosition())) {
                    p.hasFallen = true;
                }
                file.write(p.toString() + "\n");
                p.hasFallen = false;
            }
            file.close();
        } catch (IOException e) {
            System.out.println("Failed to open the save file");
            e.printStackTrace();
        }
    }

    private void recomputeLoses() {
        for (Player player : this.players) {
            Vector2i pos = player.getPosition();
            boolean[][] playerLOS = this.world.getLOS(pos, player.getLos(), 4);
            player.live(this.mobs, this.players, this.livingSpells, playerLOS);
            fire(new LivingEntityLOSDefinitionEvent(
                    player.getId(),
                    playerLOS
            ));
            explore(pos, this.world.getLOS(pos, player.getExploreLOS(), 4));
        }


        this.ghostsLiving.forEach((id, pair) -> {
            if (pair.getValue().x > 0 || pair.getValue().y > 0) {
                boolean[][] tmp = Game.this.world.getLOS(pair.getKey(), pair.getValue().x, 4);
                fire(new LivingEntityLOSDefinitionEvent(id, tmp));
                tmp = Game.this.world.getLOS(pair.getKey(), pair.getValue().y, 4);
                explore(pair.getKey(), tmp);
            }
        });

        this.ghostsStatic.forEach((id, pair) -> {
            if (pair.getValue().x > 0 || pair.getValue().y > 0) {
                boolean[][] tmp = Game.this.world.getLOS(pair.getKey(), pair.getValue().x, 4);
                fire(new StaticEntityLOSDefinitionEvent(id, tmp));
                tmp = Game.this.world.getLOS(pair.getKey(), pair.getValue().y, 4);
                explore(pair.getKey(), tmp);
            }
        });

        this.livingsSharedLOS
                .forEach((id, pair) -> {
                    boolean[][] tmp = this.world.getLOS(pair.getKey().getPosition(),
                                                        pair.getValue(),
                                                        4);
                    fire(new LivingEntityLOSDefinitionEvent(
                            id,
                            tmp
                    ));
                    explore(pair.getKey().getPosition(), tmp);
                }
        );
    }

    private void addRandomMob(Vector2i mobPos, Random random) {
        int selectedMob = randomSelector(random, this.mobsLikelihoodSum, this.mobsLikelihood);
        int mobLevel = random.nextInt(3) + this.level - 1;
        Mob mob = new Mob(
                this.mobsName[selectedMob],
                mobLevel,
                mobLevel * this.mobsXpGainPerLevel[selectedMob] + this.mobsBaseXpGain[selectedMob],
                (mobLevel * this.mobsHPPerLevel[selectedMob] + this.mobsBaseHP[selectedMob]) * (Settings.difficulty + 0.5),
                (mobLevel * this.mobsAttackPerLevel[selectedMob] + this.mobsBaseAttack[selectedMob]) * (Settings.difficulty + 0.5),
                (mobLevel * this.mobsDefPerLevel[selectedMob] + this.mobsBaseDef[selectedMob]) * (Settings.difficulty + 0.5),
                this.mobsChaseRange[selectedMob],
                mobPos.copy());
        this.mobs.add(mob);
        fire(new LivingEntityCreationEvent(mob.getId(), this.mobsTypes[selectedMob],
                mobPos.copy(), Direction.DOWN, mob.getDescription()));
    }

    private StorableObject lootSelector(Random random) {
        int chestLevel = random.nextInt(3) + this.level - 1;
        Vector2i selectedChest = stuffSelector(random);
        int selection = selectedChest.y;
        StorableObject ans;
        switch (selectedChest.x) {
            case 0:
                ans = new Armor(this.lootsArmorDefensePerLevel[selection] * chestLevel + this.lootsArmorBaseDefense[selection],
                        this.lootsArmorAttackPerLevel[selection] * chestLevel + this.lootsArmorBaseAttack[selection],
                        this.lootsArmorType[selection]);
                break;
            case 1:
                ans = new Weapon(this.lootsWeaponBaseAttack[selection] + this.lootsWeaponAttackPerLevel[selection] * chestLevel,
                        this.lootsWeaponRange[selection],
                        this.lootsWeaponManaCost[selection] + this.lootsWeaponManaCostPerLevel[selection] * chestLevel);
                break;
            case 2:
                ans = new Scroll(this.lootsScrollTurns[selection],
                        this.lootsScrollBaseDamagePerTurn[selection] + chestLevel * this.lootsScrollDamagePerTurnPerLevel[selection],
                        this.lootsScrollBaseDamageModPerTurn[selection] + chestLevel * this.lootsScrollDamageModPerTurnPerLevel[selection]);
                break;
            case 3:
                    /* Falls through */
            default:
                ans = new Pot(this.lootsPotTurns[selection],
                        this.lootsPotHeal[selection] + this.lootsPotHealPerLevel[selection] * chestLevel,
                        this.lootsPotMana[selection] + this.lootsPotManaPerLevel[selection] * chestLevel,
                        this.lootsPotDefMod[selection],
                        this.lootsPotAttackMod[selection],
                        this.lootsPotHPMod[selection],
                        this.lootsPotManaMod[selection]);
                break;
        }
        return ans;
    }

    private Vector2i stuffSelector(Random random) {
        Vector2i ans = new Vector2i();
        int rand = random.nextInt(this.lootsLikelihoodSum);
        int sum = this.lootsArmorLikelihoodSum;
        if (rand <= sum) {
            ans.x = 0;
            ans.y = randomSelector(random, this.lootsArmorLikelihoodSum, this.lootsArmorLikelihood);

        } else {
            sum += this.lootsWeaponLikelihoodSum;
            if (rand <= sum) {
                ans.x = 1;
                ans.y = randomSelector(random, this.lootsWeaponLikelihoodSum, this.lootsWeaponLikelihood);

            } else {
                sum += this.lootsScrollLikelihoodSum;
                if (rand <= sum) {
                    ans.x = 2;
                    ans.y = randomSelector(random, this.lootsScrollLikelihoodSum, this.lootsScrollLikelihood);

                } else {
                    ans.x = 3;
                    ans.y = randomSelector(random, this.lootsPotLikelihoodSum, this.lootsPotLikelihood);
                }
            }
        }
        return ans;
    }

    private int randomSelector(Random random, int likelihoodSum, int[] table) {
        int rand = random.nextInt(likelihoodSum);
        int i = 0;
        do {
            rand -= table[i++];
        } while (rand >= 0);
        return i - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        if (this.isRunning) {
            throw new AlreadyRunningException();
        }
        this.isRunning = true;

        Sound.player.stopAny();
        this.currentMusic = Sounds.MAIN_MUSIC;
        Sound.player.play(this.currentMusic);
        RequestEvent event;
        this.currentPlayer.test();
        try {
            do {
                if (!this.isPaused) {
                    do {
                        try {
                            Thread.sleep(REFRESH_TIME_MS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        event = this.requestedEvent.poll();
                        while (event != null) {
                            try { // debug
                                switch (event.getSubType()) {
                                    case MOVE_REQUEST_EVENT:
                                        treatRequestEvent((MoveRequestEvent) event);
                                        break;
                                    case INTERACTION_REQUEST_EVENT:
                                        treatRequestEvent((InteractionRequestEvent) event);
                                        break;
                                    case USAGE_REQUEST_EVENT:
                                        treatRequestEvent((UsageRequestEvent) event);
                                        break;
                                    case DROP_REQUEST_EVENT:
                                        treatRequestEvent((DropRequestEvent) event);
                                        break;
                                    case CENTER_VIEW_REQUEST_EVENT:
                                        treatRequestEvent((CenterViewRequestEvent) event);
                                        break;
                                    case LOCK_VIEW_REQUEST_EVENT:
                                        break;
                                    case EQUIP_REQUEST_EVENT:
                                        treatRequestEvent((EquipRequestEvent) event);
                                        break;
                                    case CAST_SPELL_REQUEST_EVENT:
                                        treatRequestEvent((CastSpellRequestEvent) event);
                                        break;
                                    case SPELL_SELECTED_REQUEST_EVENT:
                                        treatRequestEvent((SpellSelectedRequestEvent) event);
                                        break;
                                }
                            } catch (Exception e) {
                                // debug
                                e.printStackTrace();
                            } finally {
                                event = this.requestedEvent.poll();
                            }
                        }
                        if (this.currentPlayer.isARequestPending() || this.currentPlayer.getFloor() != this.level) {
                            this.nextTick(); // modifies the here-above boolean
                        }
                    } while (this.currentPlayer.isARequestPending() || this.currentPlayer.getFloor() != this.level);
                } else {
                    try {
                        Thread.sleep(REFRESH_TIME_MS * 20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (this.isRunning);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        Sound.player.stop(this.currentMusic);

        if (Settings.permaDeath && this.players.size() == 0) {
            try {
                Files.deleteIfExists(new File(this.gameName).toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean[][] bool = new boolean[(this.world.getSize().x / 2) * 2 + 1][(this.world.getSize().y / 2) * 2 + 1];
        for (int i = 0; i < bool.length; i++) {
            for (int j = 0; j < bool[i].length; j++) {
                bool[i][j] = true;
            }
        }
        fire(new LivingEntityCreationEvent(
                Pair.ERROR_VAL,
                LivingEntityType.LITTLE_SATANIC_DUCK,
                new Vector2i(this.world.getSize().x / 2, this.world.getSize().y / 2),
                Direction.DOWN,
                ""
        ));
        fire(new LivingEntityLOSDefinitionEvent(Pair.ERROR_VAL, bool));
        fire(new LivingEntityDeletionEvent(Pair.ERROR_VAL));
        this.currentMusic = Sounds.DEATH_MUSIC;
        Sound.player.play(this.currentMusic);
    }

    private void treatRequestEvent(SpellSelectedRequestEvent event) {
        if (!this.sortedPlayers.isEmpty()) {
            if (this.selectingSpell) {
                if (event != null) {
                    Player player = this.sortedPlayers.poll();
                    Spell<?> s = null;
                    for (Spell spell : player.getSpells()) {
                        if (spell != null) {
                            if (spell.getSpellType().equals(event.getSpellType())) {
                                s = spell;
                                break;
                            }
                        }
                    }
                    if (s != null) {
                        s.nextSpellLevel();
                    } else {
                        if (selectedSpell != SpellType.NOT_A_SPELL) {
                            Spell[] spells = player.getSpells();
                            for (int i = 0; i < spells.length; i++) {
                                if (selectedSpell.equals(spells[i].getSpellType())) {
                                    spells[i] = null;
                                    break;
                                }
                            }
                        }
                        Spell.makeSpell(event.getSpellType(), player);
                    }
                }

                if (!this.sortedPlayers.isEmpty()) {
                    Collection<SpellType> upgradeableSpells = new ArrayList<>(4);
                    for (Spell spell : this.sortedPlayers.peek().getSpells()) {
                        if (spell != null) {
                            upgradeableSpells.add(spell.getSpellType());
                        }
                    }
                    while (upgradeableSpells.size() < 4) {
                        upgradeableSpells.add(SpellType.NOT_A_SPELL);
                    }
                    fire(new SpellSelectionEvent("Select a spell to upgrade / change", upgradeableSpells));
                    this.selectingSpell = false;
                } else {
                    this.save();
                }
            } else {
                if (event != null) {
                    this.selectedSpell = event.getSpellType();
                } else {
                    this.selectedSpell = SpellType.NOT_A_SPELL;
                }

                Collection<SpellType> spells = new ArrayList<>(4);

                if (this.selectedSpell != SpellType.NOT_A_SPELL) {
                    spells.add(this.selectedSpell);
                }

                Collection<SpellType> tmp = new ArrayList<>(5);
                for (Spell spell : this.sortedPlayers.peek().getSpells()) {
                    if (spell != null) {
                        tmp.add(spell.getSpellType());
                    }
                }
                tmp.add(SpellType.NOT_A_SPELL);
                spells.addAll(SpellType.randomExcept(3, tmp));

                fire(new SpellSelectionEvent("Upgrade your spell / change it.", spells));
                this.selectingSpell = true;
            }
        }
    }

    private void treatRequestEvent(CastSpellRequestEvent event) {
        this.objectToUse = null;
        this.currentSpellTargets.clear();

        Spell<?> spell = this.players.stream()
                .filter(player -> player.getNumber() == event.getPlayerNumber())
                .findAny()
                .get()
                .getSpells()
                [event.getSpellNumber()];

        if (spell == this.currentSpell) {
            this.currentSpell = null;
            return;
        }

        this.currentSpell = spell;
        if (this.currentSpell.getTargetNumber() == 0 && !this.currentSpell.needSourcePosition()) {
            this.currentSpell.cast(Collections.emptyList(), null);
            this.currentSpell = null;
        }
    }

    private void treatRequestEvent(CenterViewRequestEvent e) {
        fire(new CenterOnTileEvent(this.currentPlayer.getPosition()));
    }

    private void treatRequestEvent(DropRequestEvent e) {
        /* Take the shortest path possible to drop the item */
        if (!Tile.isObstructed(this.world.getTile(e.getDropPosition())) || this.world.getTile(e.getDropPosition()) == Tile.HOLE) {
            Stack<Vector2i> path = this.shortestPathApprox(this.currentPlayer.getPosition(), e.getDropPosition(), false, null);
            if (path != null) {
                this.currentPlayer.setRequestedPath(path);
                this.currentPlayer.setObjectToDrop(e.getObjectId(), e.getDropPosition());
                this.nextTick();
            }
        }
    }

    private void treatRequestEvent(InteractionRequestEvent e) {
        logger.log(debugLevel, "interaction requested for player " + this.currentPlayer.getNumber());
        Stack<Vector2i> path = this.world.getPath(this.currentPlayer.getPosition(), e.getTilePosition(), false, null);
        boolean success = true;
        Tile tile = this.world.getTile(e.getTilePosition());
        int distance = Math.max(Math.abs(e.getTilePosition().x - this.currentPlayer.getPosition().x),
                Math.abs(e.getTilePosition().y - this.currentPlayer.getPosition().y));

        if (this.currentSpell != null) {
            if (this.currentSpellTargets.size() >= this.currentSpell.getTargetNumber()) {
                this.currentSpell.cast(this.currentSpellTargets, e.getTilePosition());
                this.currentSpellTargets.clear();
                this.currentSpell = null;
            } else {
                boolean[][] los = this.world.getLOS(this.currentPlayer.getPosition(), this.currentPlayer.getLos(), 4);
                Vector2i p = this.currentPlayer.getPosition();
                if (distance <= los.length / 2
                        && los[los.length / 2 - p.x + e.getTilePosition().x]
                        [los[0].length / 2 - p.y + e.getTilePosition().y]
                        ) {
                    Mob mob = getMob(e.getTilePosition());
                    if (mob != null) {
                        this.currentSpellTargets.add(mob);
                        if (this.currentSpellTargets.size() == this.currentSpell.getTargetNumber() && !this.currentSpell
                                .needSourcePosition()) {
                            this.currentSpell.cast(this.currentSpellTargets, null);
                            this.currentSpell = null;
                            this.currentSpellTargets.clear();
                        }
                    }
                }
            }
        } else {
            if ((tile == Tile.CLOSED_DOOR || tile == Tile.OPENED_DOOR) && distance > 1) {
                path = shortestPathApprox(this.currentPlayer.getPosition(), e.getTilePosition(), false, null);
                if (path != null) {
                    this.currentPlayer.setRequestedPath(path);
                    this.currentPlayer.setRequestedInteraction(e.getTilePosition());
                } else {
                    success = false;
                }
            } else if (distance <= this.currentPlayer.getLos()) {
                /* Looking for a mob to attack*/
                boolean[][] los;
                if (this.objectToUse != null) {
                    los = this.world.getLOS(this.currentPlayer.getPosition(), this.currentPlayer.getLos(), 4);
                } else {
                    los = this.world.getLOS(this.currentPlayer.getPosition(), this.currentPlayer.getAttackRange(), 1);
                }
                Vector2i p = this.currentPlayer.getPosition();
                if (distance <= los.length / 2 && los[los.length / 2 - p.x + e.getTilePosition().x][los[0].length / 2 - p.y + e
                        .getTilePosition().y]) {
                    int i = this.findMob(e.getTilePosition());
                    if (i >= 0) {
                        if (this.objectToUse != null) {
                            if (this.objectToUse.object.getConsumableType() == ConsumableType.SCROLL) {
                                ((Scroll) this.objectToUse.object).setSource(this.currentPlayer);
                            }
                            this.objectToUse.object.trigger(this.mobs.get(i));
                            this.currentPlayer.removeFromInventory(new Pair<>(this.objectToUse));
                            this.triggeredObjects.add(this.objectToUse.object);
                            this.objectToUse = null;
                            success = false;
                            path = null;
                        } else {
                            this.currentPlayer.setRequestedAttack(this.mobs.get(i));
                        }
                    } else if (distance == 1 && (tile == Tile.CLOSED_DOOR || tile == Tile.OPENED_DOOR)) {
                    /* interaction with doors */
                        this.currentPlayer.setRequestedInteraction(e.getTilePosition());
                    } else {
                        success = false; // no mob found, neither doors
                    }
                } else {
                    success = false; // tile not in the los or out of range for damages
                    if (p.squaredDistance(e.getTilePosition()) > Math.pow(this.currentPlayer.getAttackRange(),
                                                                          2) && this.findMob(e.getTilePosition()) > -1) {
                        Collection<LivingThing> shadow = new ArrayList<>(this.mobs.size());
                        shadow.addAll(this.mobs);
                        shadow.addAll(this.players);
                        Stack<Vector2i> tmp = shortestPathApprox(p, e.getTilePosition(), false, shadow);
                        if (tmp != null) {
                            path = new Stack<>();
                            path.add(tmp.peek());
                        }
                    }
                }
            } else {
                success = false; // tile too far away
            }

		/* If you weren't able to do anything, just go to the tile */
            if (!success && path != null) {
                this.currentPlayer.setRequestedPath(path);
                success = true;
            }

		/* If you did something, keep going */
            if (success) {
                nextTick();
            } else {
                this.currentPlayer.doNothing();
            }
        }
    }

    private void treatRequestEvent(UsageRequestEvent e) {
        logger.log(debugLevel, "Usage requested for player " + this.currentPlayer.getNumber());
        List<Pair<StorableObject>> inventory = this.currentPlayer.getInventory();
        this.objectToUse = null;
        this.currentSpell = null;
        this.currentSpellTargets.clear();
        Pair<StorableObject> obj = null;
        for (Pair<StorableObject> pair : inventory) {
            if (pair.getId() == e.getObjectId()) {
                if (pair.object.getType() == StorableObjectType.CONSUMABLE) {
                    this.objectToUse = new Pair<>(pair.getId(), (Consumable) pair.object);
                } else {
                    obj = pair;
                }
            }
        }
        if (this.objectToUse != null) {
            if (this.objectToUse.object.getConsumableType() == ConsumableType.POT) {
                this.triggeredObjects.add(this.objectToUse.object);
                this.objectToUse.object.trigger(this.currentPlayer);
                this.currentPlayer.removeFromInventory(new Pair<>(this.objectToUse));
                this.objectToUse = null;
            }
        } else if (obj != null) {
            this.currentPlayer.removeFromInventory(obj);
            if (obj.object.getType() == StorableObjectType.ARMOR) {
                this.currentPlayer.equipWithArmor(new Pair<>(obj.getId(), (Armor) obj.object));
            } else if (obj.object.getType() == StorableObjectType.WEAPON) {
                this.currentPlayer.equipWithWeapon(new Pair<>(obj.getId(), (Weapon) obj.object));
            }
        } else {
            this.currentPlayer.unequip(e.getObjectId());
        }
    }

    private void treatRequestEvent(MoveRequestEvent e) {
        logger.log(debugLevel, "Move requested for player " + this.currentPlayer.getNumber());
        Vector2i pos = this.currentPlayer.getPosition().copy().add(e.getMoveDirection());
        if (isAccessible(pos)) {
            Stack<Vector2i> stack = new Stack<>();
            stack.add(pos);
            this.currentPlayer.setRequestedPath(stack);
            this.nextTick();
        } else {
            this.handle(new InteractionRequestEvent(pos));
        }
    }

    private void treatRequestEvent(EquipRequestEvent e) {
        List<Pair<StorableObject>> inventory = this.currentPlayer.getInventory();
        this.objectToUse = null;
        for (Pair<StorableObject> pair : inventory) {
            if (pair.getId() == e.getObjectId()) {
                if (pair.object.getType() != StorableObjectType.CONSUMABLE) {
                    this.currentPlayer.removeFromInventory(pair);
                    if (pair.object.getType() == StorableObjectType.ARMOR) {
						this.currentPlayer.equipWithArmor(new Pair<>(pair.getId(), (Armor) pair.object));
					} else if (pair.object.getType() == StorableObjectType.WEAPON) {
						this.currentPlayer.equipWithWeapon(new Pair<>(pair.getId(), (Weapon) pair.object));
					}
                }
                return;
            }
        }
        this.currentPlayer.unequip(e.getObjectId());
    }

    /**
     * @return true if an instance of the game is running, false otherwise
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    private Stack<Vector2i> shortestPathApprox(Vector2i dep, Vector2i arr, boolean ignoreDoors, Collection<LivingThing> entities) {
        Direction[] directions = {Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.UP};
        Stack<Vector2i> ans = null;
        Stack<Vector2i> tmp;
        for (Direction direction : directions) {
            tmp = this.world.getPath(dep, arr.copy().add(direction), ignoreDoors, entities);
            if ((tmp != null) && (ans == null || tmp.size() < ans.size())) {
                ans = tmp;
            }
        }
        tmp = this.world.getPath(dep, arr, ignoreDoors, entities);
        if ((tmp != null) && (ans == null || tmp.size() < ans.size())) {
            ans = tmp;
        }
        return ans;
    }

    /**
     * Pauses the game (ignore any incoming events)
     */
    public void pause() {
        Sound.player.pause(this.currentMusic);
        this.requestedEvent.clear();
        for (Player player : this.players) {
            player.doNothing();
        }
        this.isPaused = true;
    }

    /**
     * Resumes the game
     */
    public void resume() {
        this.requestedEvent.clear();
        this.isPaused = false;
        Sound.player.resume(this.currentMusic);
    }

    private void clearAll() {
        this.clearLevel();
        for (int i = this.players.size() - 1; i >= 0; i--) {
            this.deletePlayer(this.players.get(i));
        }
        this.players.clear();
        for (int i = this.playersOnNextLevel.size() - 1; i >= 0; i--) {
            this.deletePlayer(this.playersOnNextLevel.get(i));
        }
        this.playersOnNextLevel.clear();
    }

    private void clearLevel() {

        /* Next block deletes all entities on the GUI */

        this.livingsSharedLOS.forEach((id, pair) -> fire(new LivingEntityDeletionEvent(id)));
        this.livingsSharedLOS.clear();
        // colision with previous deletion
        this.livingSpells.forEach(l -> fire(new LivingEntityDeletionEvent(l.getId())));
        this.livingSpells.clear();

        this.ghostsLiving.forEach((id, pair) -> fire(new LivingEntityDeletionEvent(id)));
        this.ghostsLiving.clear();

        this.ghostsStatic.forEach((id, pair) -> fire(new LivingEntityDeletionEvent(id)));
        this.ghostsStatic.clear();

        for (Player player : this.players) {
            fire(new LivingEntityDeletionEvent(player.getId()));
        }

        for (Mob mob : this.mobs) {
            fire(new LivingEntityDeletionEvent(mob.getId()));
        }
        this.mobs.clear();

        for (Pair<InteractiveObject> obj : this.interactiveObjects) {
            fire(new StaticEntityDeletionEvent(obj.getId()));
        }
        this.interactiveObjects.clear();

        for (Pair<Vector2i> bulb : this.bulbsOff) {
            fire(new StaticEntityDeletionEvent(bulb.getId()));
        }
        this.bulbsOff.clear();

        for (Pair<Vector2i> bulb : this.bulbsOn) {
            fire(new StaticEntityDeletionEvent(bulb.getId()));
        }
        this.bulbsOn.clear();

        for (javafx.util.Pair<Vector2i, Pair<StorableObject>> pair : this.objectsOnGround) {
            fire(new StaticEntityDeletionEvent(pair.getValue().getId()));
        }
        this.objectsOnGround.clear();
    }

    private void deletePlayer(Player player) {
        player.deleteEquipedObjects();
        List<Pair<StorableObject>> playerInventory = new ArrayList<>(player.getInventory().size());
        playerInventory.addAll(player.getInventory());
        playerInventory.forEach(player::removeFromInventory);
        fire(new PlayerDeletionEvent(player.getNumber()));
    }

    private int findMob(Vector2i position) {
        int i = -1;
        int j = 0;
        while (i == -1 && j < this.mobs.size()) {
            if (this.mobs.get(j).getPosition().equals(position)) {
                i = j;
            }
            ++j;
        }
        return i;
    }

    private Mob getMob(Vector2i position) {
        return this.mobs.stream().filter(m -> m.getPosition().equals(position)).findFirst().orElse(null);
    }

    private void explore(Vector2i fogCenterTile, boolean[][] los) {
        int pX = fogCenterTile.x - ((los.length - 1) / 2);
        int pY = fogCenterTile.y - ((los[0].length - 1) / 2);
        for (int i = 0; i < los.length; i++) {
            for (int j = 0; j < los[i].length; j++) {
                if (los[i][j]
                        && (i + pX) >= 0 && (i + pX) < this.world.explored.length
                        && (j + pY) >= 0 && (j + pY) < this.world.explored[0].length) {
                    this.world.explored[i + pX][j + pY] = true;
                }
            }
        }
        fire(new FogAdditionEvent(fogCenterTile, los));
    }
}
