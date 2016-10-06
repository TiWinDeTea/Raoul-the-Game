package com.github.tiwindetea.raoulthegame.model.spells;

import com.github.tiwindetea.raoulthegame.model.livings.LivingThing;
import com.github.tiwindetea.raoulthegame.model.spells.passives.Berserker;
import com.github.tiwindetea.raoulthegame.model.spells.passives.BonusHP;
import com.github.tiwindetea.raoulthegame.model.spells.passives.Drainer;
import com.github.tiwindetea.raoulthegame.model.spells.passives.Regen;
import com.github.tiwindetea.raoulthegame.model.spells.passives.Savior;
import com.github.tiwindetea.raoulthegame.model.spells.useablespells.SummonDog;
import com.github.tiwindetea.raoulthegame.view.entities.SpellType;

/**
 * Created by Lucas on 05/10/2016.
 */
public class SpellConstructor {

    public static Spell construct(SpellType spell, LivingThing owner) throws ClassNotFoundException {
        switch (spell) {
            case SAVIOR:
                return new Savior(owner);
            case SAMPLE_SPELL:
                throw new UnsupportedOperationException();
            case REGEN:
                return new Regen(owner);
            case DRAINER:
                return new Drainer(owner);
            case BONUS_HP:
                return new BonusHP(owner);
            case Berserker:
                return new Berserker(owner);
            case SUMMON_DOG:
                return new SummonDog(owner);
            default:
                throw new ClassNotFoundException(spell.toString());
        }
    }
}
