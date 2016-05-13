//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.events.players.PlayerStatEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.game.entities.players.PlayerStatListener;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

import java.util.ArrayList;

/**
 * Created by maxime on 4/23/16.
 */
public class Pot implements Consumable {

	private int turns;
	private int heal;
	private int defensePowerModifier;
	private int attackPowerModifier;
	private int mana_heal;
	private int healthModifier;
	private int manaModifier;
	private Player target;
	private StaticEntityType gtype;
	private static ArrayList<PlayerStatListener> listeners = new ArrayList<>();

	public Pot(int turns, int heal, int mana_heal, int defensePowerModifier, int attackPowerModifier,
			   int healthModifier, int manaModifier) {
		this.turns = turns;
		this.heal = heal;
		this.mana_heal = mana_heal;
		this.defensePowerModifier = defensePowerModifier;
		this.attackPowerModifier = attackPowerModifier;
		this.healthModifier = healthModifier;
		this.manaModifier = manaModifier;

		if (defensePowerModifier > 0 || attackPowerModifier > 0 || healthModifier > 0 || manaModifier > 0) {
			this.gtype = StaticEntityType.SUPER_POT;
		} else if (heal == 0) {
			this.gtype = StaticEntityType.HEALING_POT;
		} else {
			this.gtype = StaticEntityType.MANA_POT;
		}
	}

	private Pot() {
	}

	public static void addPlayerListener(PlayerStatListener listener) {
		listeners.add(listener);
	}

	private PlayerStatListener[] getPlayersListeners() {
		return this.listeners.toArray(new PlayerStatListener[this.listeners.size()]);
	}

	private void firePlayerStatEvent(PlayerStatEvent event) {
		for (PlayerStatListener listener : getPlayersListeners()) {
			listener.changePlayerStat(event);
		}
	}

	public static Pot parsePot(String str) {
		if (!str.substring(0, 4).equals("pot=")) {
			throw new IllegalArgumentException("Invoking Pot.parsePot with input string: \"" + str + "\"");
		}
		if (str.equals("pot={null}")) {
			return null;
		}
		int SEType = str.indexOf("SEType=") + 7;
		int turns = str.indexOf("turns=", SEType) + 6;
		int heal = str.indexOf("heal=", turns) + 5;
		int defenseMod = str.indexOf("defenseMod=", heal) + 11;
		int attackMod = str.indexOf("attackMod=", defenseMod) + 10;
		int mana = str.indexOf("mana=", attackMod) + 5;
		int healthMod = str.indexOf("healthMod=", mana) + 10;
		int manaMod = str.indexOf("manaMod=", healthMod) + 8;

		Pot pot = new Pot();
		pot.gtype = StaticEntityType.parseStaticEntityType(str.substring(SEType, str.indexOf(',', SEType)));
		pot.turns = Integer.parseInt(str.substring(turns, str.indexOf(',', turns)));
		pot.heal = Integer.parseInt(str.substring(heal, str.indexOf(',', heal)));
		pot.defensePowerModifier = Integer.parseInt(str.substring(defenseMod, str.indexOf(',', defenseMod)));
		pot.attackPowerModifier = Integer.parseInt(str.substring(attackMod, str.indexOf(',', attackMod)));
		pot.mana_heal = Integer.parseInt(str.substring(mana, str.indexOf(',', mana)));
		pot.healthModifier = Integer.parseInt(str.substring(healthMod, str.indexOf(',', healthMod)));
		pot.manaModifier = Integer.parseInt(str.substring(manaMod, str.indexOf(',', manaMod)));
		return pot;
	}

	public void trigger(LivingThing livingThing) {
		if (livingThing.getType() == LivingThingType.PLAYER) {
			this.target = (Player) livingThing;
			this.trigger(this.target);
		} else {
			throw new IllegalArgumentException("Expecting a player");
		}
	}

	public void trigger(Player player) {
		this.target = player;
		this.healTarget();
		this.manaHealTarget();
		if (this.attackPowerModifier != 0) {
			player.increaseAttack(this.attackPowerModifier);
		}
		if (this.defensePowerModifier != 0) {
			player.increaseDefense(this.defensePowerModifier);
		}
		if (this.healthModifier != 0) {
			player.increaseHP(this.healthModifier);
			this.firePlayerStatEvent(new PlayerStatEvent(player.getNumber(), PlayerStatEvent.StatType.HEALTH,
					PlayerStatEvent.ValueType.MAX, player.getMaxHitPoints()));
		}
		if (this.manaModifier != 0) {
			player.increaseMana(this.manaModifier);
			this.firePlayerStatEvent(new PlayerStatEvent(player.getNumber(), PlayerStatEvent.StatType.MANA,
					PlayerStatEvent.ValueType.MAX, player.getMaxMana()));
		}
	}

	public boolean nextTick() {
		if (this.turns != 0) {
			--this.turns;
			this.healTarget();
			this.manaHealTarget();
			this.target.addMana(this.mana_heal);
			return this.turns <= 0;
		}
		return true;
	}

	public ConsumableType getConsumableType() {
		return ConsumableType.POT;
	}

	@Override
	public StorableObjectType getType() {
		return StorableObjectType.CONSUMABLE;
	}

	public StaticEntityType getGType() {
		return this.gtype;
	}

	@Override
	public String toString() {
		return "pot={SEType=" + this.gtype
				+ ",turns=" + this.turns
				+ ",heal=" + this.heal
				+ ",defenseMod=" + this.defensePowerModifier
				+ ",attackMod=" + this.attackPowerModifier
				+ ",mana=" + this.mana_heal
				+ ",healthMod=" + this.healthModifier
				+ ",manaMod=" + this.manaModifier
				+ ",}";
	}

	private void manaHealTarget() {
		if (this.mana_heal != 0) {
			this.target.addMana(this.mana_heal);
			this.firePlayerStatEvent(new PlayerStatEvent(this.target.getNumber(), PlayerStatEvent.StatType.MANA,
					PlayerStatEvent.ValueType.ACTUAL, this.target.getMana()));
		}
	}

	private void healTarget() {
		if (this.heal != 0) {
			this.target.heal(this.heal);
			this.firePlayerStatEvent(new PlayerStatEvent(this.target.getNumber(), PlayerStatEvent.StatType.HEALTH,
					PlayerStatEvent.ValueType.ACTUAL, this.target.getHitPoints()));
		}
	}
}
