package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

import java.util.Random;

/**
 * Created by maxime on 4/23/16.
 */
public class Scroll implements Consumable {

	private int turns;
	private int healthModifierPerTick;
	private int healthModifierModifierPerTick;
	private LivingThing target;
	private StaticEntityType gtype;

	public Scroll(int turns, int healthModifierPerTick, int healthModifierModifierPerTick) {
		this.turns = turns;
		this.healthModifierPerTick = healthModifierPerTick;
		this.healthModifierModifierPerTick = healthModifierModifierPerTick;
		Random random = new Random();
		if (random.nextBoolean()) {
			this.gtype = StaticEntityType.SCROLL1;
		} else {
			this.gtype = StaticEntityType.SCROLL2;
		}
	}

	private Scroll() {

	}

	public static Scroll parseScroll(String str) {
		if (!str.substring(0, 7).equals("scroll=")) {
			throw new IllegalArgumentException("Invoking Scroll.parseScroll with input string: \"" + str + "\"");
		}
		if (str.equals("scroll={null}")) {
			return null;
		}
		int SEType = str.indexOf("SEType=") + 7;
		int turns = str.indexOf("turns=", SEType) + 6;
		int hmpt = str.indexOf("healthMod=", turns) + 10;
		int hmmpt = str.indexOf("healthModMod=", hmpt) + 13;

		Scroll scroll = new Scroll();
		scroll.gtype = StaticEntityType.parseStaticEntityType(str.substring(SEType, str.indexOf(",", SEType)));
		scroll.turns = Integer.parseInt(str.substring(turns, str.indexOf(",", turns)));
		scroll.healthModifierPerTick = Integer.parseInt(str.substring(hmpt, str.indexOf(",", hmpt)));
		scroll.healthModifierModifierPerTick = Integer.parseInt(str.substring(hmmpt, str.indexOf(",", hmmpt)));
		return scroll;
	}

	public void trigger(LivingThing livingThing) {
		this.target = livingThing;
		livingThing.damage(this.healthModifierPerTick);
	}

	public boolean nextTick() {
		--this.turns;
		this.healthModifierModifierPerTick += this.healthModifierModifierPerTick;
		this.target.damage(this.healthModifierPerTick);
		return this.turns < 0;
	}

	public ConsumableType getConsumableType() {
		return ConsumableType.SCROLL;
	}

	@Override
	public StorableObjectType getType() {
		return StorableObjectType.CONSUMABLE;
	}

	public StaticEntityType getGtype() {
		return this.gtype;
	}

	@Override
	public String toString() {
		return "scroll={SEType=" + this.gtype
				+ ",turns=" + this.turns
				+ ",healthMod=" + this.healthModifierPerTick
				+ ",healthModMod=" + this.healthModifierModifierPerTick
				+ ",}";
	}
}
