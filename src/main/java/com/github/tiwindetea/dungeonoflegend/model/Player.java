//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.BOOTS;
import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.BREAST_PLATE;
import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.GLOVES;
import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.HELMET;
import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.PANTS;

/**
 * Created by maxime on 4/23/16.
 */
public class Player extends LivingThing {
	private List<Pair<StorableObject>> inventory;
	private static ArmorType[] equipedArmor = {HELMET, BREAST_PLATE, GLOVES, PANTS, BOOTS};
	private int maxStorageCapacity;
	private int maxMana;
	private int mana;
	private List<Pair<Armor>> armors;
	private Pair<Weapon> weapon;
	private String name;
	private int hitPointsPerLevel;
	private int manaPerLevel;
	private int attackPowerPerLevel;
	private int defensePowerPerLevel;
	private Stack<Vector2i> requestedPath;
	private long objectToDropId;
	private int floor;
	private int xp;
	private int los;
	private int number;
	private int exploreLOS;
	private Vector2i requestedAttack;
	private Vector2i requestedInteraction;

	public Player(String name, int number, int los, int exploreLOS, int level, int maxStorageCapacity, int baseHealth, int baseMana, int baseAttack, int baseDef, int healthPerLevel,
				  int manaPerLevel, int attackPowerPerLevel, int defensePowerPerLevel) {
		super();
		this.number = number;
		this.inventory = new ArrayList<>();
		this.armors = new ArrayList<>(5);
		for (int i = 0; i < 5; i++) {
			this.armors.add(new Pair<>(null));
		}
		this.weapon = null;
		this.name = name;
		this.level = level;
		this.maxStorageCapacity = maxStorageCapacity;
		this.hitPoints = baseHealth + healthPerLevel * level;
		this.maxHitPoints = this.hitPoints;
		this.mana = baseMana + manaPerLevel * level;
		this.maxMana = this.mana;
		this.attackPower = baseAttack + attackPowerPerLevel * level;
		this.defensePower = baseDef + defensePowerPerLevel * level;
		this.hitPointsPerLevel = healthPerLevel;
		this.manaPerLevel = manaPerLevel;
		this.attackPowerPerLevel = attackPowerPerLevel;
		this.defensePowerPerLevel = defensePowerPerLevel;
		this.requestedPath = new Stack<>();
		this.floor = 0;
		this.xp = 0;
		this.los = los;
		this.exploreLOS = exploreLOS;
	}

	private Player() {
		super();
		this.inventory = new ArrayList<>();
		this.armors = new ArrayList<>(5);
		for (int i = 0; i < 5; i++) {
			this.armors.add(new Pair<>(null));
		}
		this.requestedPath = new Stack<>();
	}

	public long getObjectToDropId() {
		return this.objectToDropId;
	}

	public void setObjectToDropId(long objectToDropId) {
		this.objectToDropId = objectToDropId;
	}

	public void setRequestedPath(Stack<Vector2i> requestedPath) {
		this.requestedPath = requestedPath;
	}

	public Vector2i getRequestMove() {
		return this.requestedPath.size() == 0 ? null : this.requestedPath.get(0);
	}

	public int getFloor() {
		return this.floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getMana() {
		return this.mana;
	}

	public String getName() {
		return this.name;
	}

	public Pair<Armor>[] getEquipedArmor() {
		return (Pair<Armor>[]) this.armors.toArray();
	}

	public Pair<Weapon> getEquipedWeapon() {
		return this.weapon;
	}

	public int getExploreLOS() {
		return this.exploreLOS;
	}

	public int getLos() {
		return this.los;
	}

	public void setRequestedAttack(Vector2i requestedAttack) {
		this.requestedAttack = requestedAttack.copy();
	}

	public int getNumber() {
		return this.number;
	}

	public Vector2i getRequestedAttack() {
		return this.requestedAttack.copy();
	}

	public void setRequestedInteraction(Vector2i position) {
		this.requestedInteraction = position;
	}

	public Pair<StorableObject>[] getInventory() {
		return (Pair<StorableObject>[]) this.inventory.toArray();
	}

	public void addToInventory(Pair<StorableObject> storable) {
		if (this.inventory.size() < this.maxStorageCapacity) {
			this.inventory.add(storable);
		}
	}

	public StorableObject dropRequestAccepted() {
		StorableObject object = this.removeFromInventory(this.objectToDropId);
		this.objectToDropId = Pair.ERROR_VAL;
		return object;
	}

	public Pair<StorableObject> removeFromInventory(int index) {
		Pair<StorableObject> obj = this.inventory.get(index);
		this.inventory.remove(index);
		return obj;
	}

	public StorableObject removeFromInventory(long id) {
		int i = this.inventory.indexOf(new Pair<StorableObject>(id));
		if (i < 0) {
			return null;
		}
		StorableObject obj = this.inventory.get(i).object;
		this.inventory.remove(i);
		return obj;
	}

	public boolean useMana(int consumption) {
		if(this.mana > consumption) {
			this.mana -= consumption;
			return true;
		}
		return false;
	}

	public Pair<Armor> equipWithArmor(Pair<Armor> armor) {
		Pair<Armor> removedArmor = null;
		for (int i = 0; i < equipedArmor.length; i++) {
			if (equipedArmor[i] == armor.object.getArmorType()) {
				if (this.armors.get(i).object != null) {
					removedArmor = this.armors.get(i);
					addToInventory(new Pair<>(removedArmor.getId(), removedArmor.object));
				}
				this.armors.set(i, armor);
				return removedArmor;
			}
		}
		return null;
	}

	public Pair<Weapon> equipWithWeapon(Pair<Weapon> weapon) {
		Pair<Weapon> removedWeapon = this.weapon;
		this.weapon = weapon;
		if (removedWeapon != null) {
			this.addToInventory(new Pair<>(removedWeapon.getId(), removedWeapon.object));
		}
		return removedWeapon;
	}

	public void addMana(int mana) {
		this.mana = Math.min(this.maxMana, mana + this.mana);
	}

	public void heal(int hp) {
		this.hitPoints = Math.min(this.maxHitPoints, hp + this.hitPoints);
	}

	public void increaseAttack(int ad) {
		this.attackPower += ad;
	}

	public void increaseHP(int hp) {
		this.maxHitPoints += hp;
		this.hitPoints += hp;
	}

	public void increaseDefense(int def) {
		this.defensePower += def;
	}

	public void increaseMana(int manaModifier) {
		this.maxMana += manaModifier;
		this.mana += manaModifier;
	}

	public static Player parsePlayer(String str) {
		if (!str.substring(0, 7).equals("player=")) {
			throw new IllegalArgumentException("Invoking Player.parsePlayer with input string: \"" + str + "\"");
		}

		int name = str.indexOf("name=") + 5;
		int nbr = str.indexOf("nbr=", name) + 4;
		int los = str.indexOf("los=", nbr) + 4;
		int elos = str.indexOf("elos=", los) + 5;
		int capa = str.indexOf("capacity=", elos) + 9;
		int mhp = str.indexOf("maxHitPoints=", capa) + 13;
		int mm = str.indexOf("maxMana=", mhp) + 8;
		int hp = str.indexOf("hitPoints=", mm) + 10;
		int mana = str.indexOf("mana=", hp) + 5;
		int hppl = str.indexOf("hitPointsPerLevel=", mana) + 18;
		int mpl = str.indexOf("manaPerLevel=", hppl) + 13;
		int level = str.indexOf("level=", mpl) + 6;
		int xp = str.indexOf("xp=", level) + 3;
		int ap = str.indexOf("attackPower=", xp) + 12;
		int dp = str.indexOf("defensePower=", ap) + 13;
		int appl = str.indexOf("attackPowerPerLevel=", dp) + 20;
		int adpl = str.indexOf("defensePowerPerLevel=", appl) + 21;

		Player p = new Player();

		p.name = str.substring(name, str.indexOf(',', name));
		p.number = Integer.parseInt(str.substring(nbr, str.indexOf(',', nbr)));
		p.los = Integer.parseInt(str.substring(los, str.indexOf(',', los)));
		p.exploreLOS = Integer.parseInt(str.substring(elos, str.indexOf(',', elos)));
		p.maxStorageCapacity = Integer.parseInt(str.substring(capa, str.indexOf(',', capa)));
		p.maxHitPoints = Integer.parseInt(str.substring(mhp, str.indexOf(',', mhp)));
		p.maxMana = Integer.parseInt(str.substring(mm, str.indexOf(',', mm)));
		p.hitPoints = Integer.parseInt(str.substring(hp, str.indexOf(',', hp)));
		p.mana = Integer.parseInt(str.substring(mana, str.indexOf(',', mana)));
		p.hitPointsPerLevel = Integer.parseInt(str.substring(hppl, str.indexOf(',', hppl)));
		p.manaPerLevel = Integer.parseInt(str.substring(mpl, str.indexOf(',', mpl)));
		p.level = Integer.parseInt(str.substring(level, str.indexOf(',', level)));
		p.xp = Integer.parseInt(str.substring(xp, str.indexOf(',', xp)));
		p.attackPower = Integer.parseInt(str.substring(ap, str.indexOf(',', ap)));
		p.defensePower = Integer.parseInt(str.substring(dp, str.indexOf(',', dp)));
		p.attackPowerPerLevel = Integer.parseInt(str.substring(appl, str.indexOf(',', appl)));
		p.defensePowerPerLevel = Integer.parseInt(str.substring(adpl, str.indexOf(',', adpl)));

		String weapon = str.substring(str.indexOf("weapon={"));
		weapon = weapon.substring(0, weapon.indexOf('}') + 1);
		p.weapon = new Pair<>(Weapon.parseWeapon(weapon));

		ArmorType[] armorTypes = {
				BREAST_PLATE,
				GLOVES,
				HELMET,
				BOOTS,
				PANTS
		};

		int armorIndex = 0;
		for (int i = 0; i < armorTypes.length; i++) {
			armorIndex = str.indexOf("armor={", armorIndex + 1);
			String armor = str.substring(armorIndex, str.indexOf('}', armorIndex) + 1);
			p.armors.set(i, new Pair<>(Armor.parseArmor(armor)));
		}

		str = "," + str.substring(str.indexOf("inventory={") + 11);
		String item;
		while (!str.equals(",},}")) {
			item = str.substring(str.indexOf(',') + 1, str.indexOf('}') + 1);
			str = str.substring(str.indexOf("}") + 1);
			switch (item.substring(0, item.indexOf('='))) {
				case "weapon":
					p.inventory.add(new Pair<>(Weapon.parseWeapon(item)));
					break;
				case "armor":
					p.inventory.add(new Pair<>(Armor.parseArmor(item)));
					break;
				case "pot":
					p.inventory.add(new Pair<>(Pot.parsePot(item)));
					break;
				case "scroll":
					p.inventory.add(new Pair<>(Scroll.parseScroll(item)));
					break;
				default:
					System.out.println("Unexpected item when parsing the inventory of " + p.name);
					System.out.println("(got: " + item + ")");
					break;
			}
		}

		return p;
	}

	@Override
	public String toString() {
		String ans = "player={name=" + this.name
				+ ",nbr=" + this.number
				+ ",los=" + this.los
				+ ",elos=" + this.exploreLOS
				+ ",capacity=" + this.maxStorageCapacity
				+ ",maxHitPoints=" + this.maxHitPoints
				+ ",maxMana=" + this.maxMana
				+ ",hitPoints=" + this.hitPoints
				+ ",mana=" + this.mana
				+ ",hitPointsPerLevel=" + this.hitPointsPerLevel
				+ ",manaPerLevel=" + this.manaPerLevel
				+ ",level=" + this.level
				+ ",xp=" + this.xp
				+ ",attackPower=" + this.attackPower
				+ ",defensePower=" + this.defensePower
				+ ",attackPowerPerLevel=" + this.attackPowerPerLevel
				+ ",defensePowerPerLevel=" + this.defensePowerPerLevel;

		if (this.weapon == null) {
			ans += ",weapon={null}";
		} else {
			ans += "," + this.weapon.object;
		}
		for (Pair<Armor> anArmor : this.armors) {
			ans += ",";
			if (anArmor.object == null) {
				ans += "armor={null}";
			} else {
				ans += anArmor.object;
			}
		}
		ans += ",inventory={";
		for (Pair<StorableObject> storableObjectPair : this.inventory) {
			ans += storableObjectPair.object + ",";
		}
		return ans.substring(0, ans.length() - 1) + ",},}";
	}

	@Override
	public void live(Collection<Pair<LivingThing>> livingEntities) {
		for (Pair<LivingThing> entity : livingEntities) {
			if (entity.object.getType() == LivingThingType.MOB) {
				this.requestedPath = null;
			}
		}
	}

	@Override
	public LivingThingType getType() {
		return LivingThingType.PLAYER;
	}

	public boolean isARequestPending() {
		return this.requestedPath.size() > 0 || this.objectToDropId != Pair.ERROR_VAL || this.requestedInteraction != null;
	}

	public int getMaxMana() {
		return this.maxMana;
	}
}
