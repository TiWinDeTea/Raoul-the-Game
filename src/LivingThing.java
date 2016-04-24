/**
 * Created by maxime on 4/23/16.
 */
public abstract class LivingThing {
	private int level;
	private int maxHitPoints;
	private int hitPoints;
	private int attackPower;
	private int defensePower;
	private Vector2i position;

	public void print() {
		//TODO
	}

	public int getLevel() {
		//TODO
		return 0;
	}

	public int getMaxHitPoints() {
		//TODO
		return 0;
	}

	public int getHitPoints() {
		//TODO
		return 0;
	}

	public int getAttackPower() {
		//TODO
		return 0;
	}

	public int getDefensePower() {
		//TODO
		return 0;
	}

	public Vector2i getPosition() {
		//TODO
		return null;
	}

	public void damage() {
		//TODO

	}

	public boolean isAlive() {
		//TODO
		return false;
	}

	public void live() {
		//TODO
	}

	public LivingThingType getType() {
		//TODO
		return null;
	}
}
