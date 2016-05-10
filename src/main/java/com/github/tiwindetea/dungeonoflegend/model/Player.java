package com.github.tiwindetea.dungeonoflegend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.*;

/**
 * Created by maxime on 4/23/16.
 */
public class Player extends LivingThing {
	private List<Pair<? extends StorableObject>> inventory;
	private static ArmorType[] equipedArmor = {HELMET, BREAST_PLATE, GLOVES, PANTS, BOOTS};
	private int maxStorageCapacity;
	private int maxMana;
	private int mana;
	private Pair<Armor>[] armors;
	private Pair<Weapon> weapon;
	private String name;
	private int hitPointsPerLevel;
	private int manaPerLevel;
	private int attackPowerPerLevel;
	private int defensePowerPerLevel;
	private Stack<Vector2i> path;
	private Vector2i requestMove;
	private boolean requestInteraction;
	private int floor;
	private int xp;

	public Player(String name, int level, int maxStorageCapacity, int baseHealth, int baseMana, int baseAttack, int baseDef, int healthPerLevel,
				  int manaPerLevel, int attackPowerPerLevel, int defensePowerPerLevel, int floor) {
		super();
		this.inventory = new ArrayList<>();
		this.armors = new Pair[5];
		for (int i = 0; i < this.armors.length; i++) {
			this.armors[i] = new Pair<>(null);
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
		this.path = new Stack<>();
		this.requestMove = null;
		this.requestInteraction = false;
		this.floor = floor;
	}

	private Player() {
		super();
		this.inventory = new ArrayList<>();
		this.armors = new Pair[5];
		for (int i = 0; i < this.armors.length; i++) {
			this.armors[i] = new Pair<>(null);
		}
		this.path = new Stack<>();
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

	public void addToInventory(Pair<? extends StorableObject> storable) {
		if (this.inventory.size() < this.maxStorageCapacity) {
			this.inventory.add(storable);
		}
	}

	public Pair<? extends StorableObject> removeFromInventory(int index) {
		Pair<? extends StorableObject> obj = this.inventory.get(index);
		this.inventory.remove(index);
		return obj;
	}

	public StorableObject removeFromInventory(long id) {
		int i = this.inventory.indexOf(new Pair<StorableObject>(id));
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
				if (this.armors[i] != null) {
					addToInventory(this.armors[i]);
					removedArmor = this.armors[i];
				}
				this.armors[i] = armor;
				return removedArmor;
			}
		}
		return null;
	}

	public Pair<Weapon> equipWithWeapon(Pair<Weapon> weapon) {
		Pair<Weapon> removedWeapon = this.weapon;
		this.weapon = weapon;
		if (removedWeapon != null) {
			this.addToInventory(removedWeapon);
		}
		return removedWeapon;
	}

	public void setPath(Stack<Vector2i> path) {
		this.path = path;
	}

	public void setInteractionRequested() {
		this.requestInteraction = true;
	}

	public Pair<Armor>[] getEquipedArmor() {
		return this.armors;
	}

	public Pair<Weapon> getEquipedWeapon() {
		return this.weapon;
	}

	public Pair<StorableObject>[] getInventory() {
		return (Pair<StorableObject>[]) this.inventory.toArray();
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

		int name = str.indexOf("name=") + 5;
		int capa = str.indexOf("capacity=", name) + 9;
		int mhp = str.indexOf("maxHitPoints=", capa) + 13;
		int mm = str.indexOf("maxMana=", mhp) + 8;
		int hp = str.indexOf("hitPoints=", mm) + 10;
		int mana = str.indexOf("mana=", hp) + 5;
		int hppl = str.indexOf("hitPointsPerLevel=", mana) + 18;
		int mpl = str.indexOf("manaPerLevel=", hppl) + 13;
		int level = str.indexOf("level=", mpl) + 6;
		int ap = str.indexOf("attackPower=", level) + 12;
		int dp = str.indexOf("defensePower=", ap) + 13;
		int appl = str.indexOf("attackPowerPerLevel=", dp) + 20;
		int adpl = str.indexOf("defensePowerPerLevel=", appl) + 21;

		Player p = new Player();

		p.name = str.substring(name, str.indexOf(',', name));
		p.maxStorageCapacity = Integer.parseInt(str.substring(capa, str.indexOf(',', capa)));
		p.maxHitPoints = Integer.parseInt(str.substring(mhp, str.indexOf(',', mhp)));
		p.maxMana = Integer.parseInt(str.substring(mm, str.indexOf(',', mm)));
		p.hitPoints = Integer.parseInt(str.substring(hp, str.indexOf(',', hp)));
		p.mana = Integer.parseInt(str.substring(mana, str.indexOf(',', mana)));
		p.hitPointsPerLevel = Integer.parseInt(str.substring(hppl, str.indexOf(',', hppl)));
		p.manaPerLevel = Integer.parseInt(str.substring(mpl, str.indexOf(',', mpl)));
		p.level = Integer.parseInt(str.substring(level, str.indexOf(',', level)));
		p.attackPower = Integer.parseInt(str.substring(ap, str.indexOf(',', ap)));
		p.defensePower = Integer.parseInt(str.substring(dp, str.indexOf(',', dp)));
		p.attackPowerPerLevel = Integer.parseInt(str.substring(appl, str.indexOf(',', appl)));
		p.defensePowerPerLevel = Integer.parseInt(str.substring(adpl, str.indexOf(',', adpl)));

		String weapon = str.substring(str.indexOf("weapon={"));
		weapon = weapon.substring(0, weapon.indexOf('}') + 1);
		p.weapon = new Pair(Weapon.parseWeapon(weapon));

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
			p.armors[i] = new Pair<>(Armor.parseArmor(armor));
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
				+ ",capacity=" + this.maxStorageCapacity
				+ ",maxHitPoints=" + this.maxHitPoints
				+ ",maxMana=" + this.maxMana
				+ ",hitPoints=" + this.hitPoints
				+ ",mana=" + this.mana
				+ ",hitPointsPerLevel=" + this.hitPointsPerLevel
				+ ",manaPerLevel=" + this.manaPerLevel
				+ ",level=" + this.level
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
		for (Pair<? extends StorableObject> storableObjectPair : this.inventory) {
			ans += storableObjectPair.object + ",";
		}
		return ans.substring(0, ans.length() - 1) + ",},}";
	}

	@Override
	public void live() {
		//TODO
	}

	@Override
	public LivingThingType getType() {
		return LivingThingType.PLAYER;
	}
}
