//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import java.util.Random;

/**
 * Seed (levels generation)
 *
 * @author Lucas LAZARE
 */
public class Seed {
    private long alphaSeed;
    private long betaSeed;

    /**
     * Instantiates a new Seed with random subseeds.
     */
    public Seed() {
        Random random = new Random();
        this.alphaSeed = random.nextLong();
        this.betaSeed = random.nextLong();
    }

    /**
     * Instantiates a new Seed with given subseeds.
     *
     * @param alphaSeed alpha seed
     * @param betaSeed  beta seed
     */
    public Seed(long alphaSeed, long betaSeed) {
        this.alphaSeed = alphaSeed;
        this.betaSeed = betaSeed;
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
        int alphaSeedIdx = str.indexOf("alphaSeed=") + 10;
        int betaSeedIdx = str.indexOf("betaSeed=", alphaSeedIdx) + 9;
        return new Seed(
                Long.parseLong(str.substring(alphaSeedIdx, str.indexOf(',', alphaSeedIdx))),
                Long.parseLong(str.substring(betaSeedIdx, str.indexOf(',', betaSeedIdx)))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "seed={alphaSeed=" + this.alphaSeed + ",betaSeed=" + this.betaSeed + ",}";
    }

    /**
     * Copies the seed
     * @return A copy of this seed
     */
    public Seed copy() {
        return new Seed(this.alphaSeed, this.betaSeed);
    }
}
