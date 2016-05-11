package com.github.tiwindetea.dungeonoflegend.model;

import java.util.Random;

/**
 * Created by organic-code on 4/30/16.
 */
public class Seed {
    private long alphaSeed;
    private long betaSeed;

    public Seed() {
        Random random = new Random();
        this.alphaSeed = random.nextLong();
        this.betaSeed = random.nextLong();
    }

    public Seed(long alphaSeed, long betaSeed) {
        this.alphaSeed = alphaSeed;
        this.betaSeed = betaSeed;
    }

    public long getAlphaSeed() {
        return this.alphaSeed;
    }

    public long getBetaSeed() {
        return this.betaSeed;
    }

    public Random getRandomizer(int level) {
        return new Random(this.alphaSeed + this.betaSeed * level);
    }

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

    @Override
    public String toString() {
        return "seed={alphaSeed=" + this.alphaSeed + ",betaSeed=" + this.betaSeed + ",}";
    }
}
