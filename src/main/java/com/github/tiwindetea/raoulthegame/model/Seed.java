//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.model;

import java.math.BigInteger;
import java.util.Random;

/**
 * Seed (levels generation)
 *
 * @author Lucas LAZARE
 */
public class Seed {
    private long alphaSeed;
    private long betaSeed;
    private final String seed;

    /**
     * Instantiates a new Seed with random subseeds.
     */
    public Seed() {
        this.seed = new BigInteger(256, new Random()).toString(Character.MAX_RADIX);
        Random random = new Random(this.seed.hashCode());
        this.alphaSeed = random.nextLong();
        this.betaSeed = random.nextLong();
    }

    /**
     * Instantiates a new Seed with given subseeds.
     *
     * @param seed the base seed
     */
    public Seed(String seed) {
        this.seed = seed;
        Random random = new Random(this.seed.hashCode());
        this.alphaSeed = random.nextLong();
        this.betaSeed = random.nextLong();
    }

    /**
     * Gets alpha seed.
     *
     * @return the alpha seed
     */
    public long getAlphaSeed() {
        return this.alphaSeed;
    }

    /**
     * Gets beta seed.
     *
     * @return the beta seed
     */
    public long getBetaSeed() {
        return this.betaSeed;
    }

    /**
     * Gets randomizer.
     *
     * @param level level
     * @return a Random instance corresponding to the level
     */
    public Random getRandomizer(int level) {
        return new Random(this.alphaSeed + this.betaSeed * level);
    }

    /**
     * Parses a Seed.
     *
     * @param str a Seed's String
     * @return the Seed
     */
    public static Seed parseSeed(String str) {
        if (!str.substring(0, 5).equals("seed=")) {
            throw new IllegalArgumentException("Invoking Seed.parseSeed with input string: \"" + str + "\"");
        }
        int seedIdx = str.indexOf("{") + 1;
        return new Seed(str.substring(seedIdx, str.indexOf(',', seedIdx)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "seed={" + this.seed + ",}";
    }

    /**
     * Copies the seed
     * @return A copy of this seed
     */
    public Seed copy() {
        return new Seed(this.seed);
    }
}
