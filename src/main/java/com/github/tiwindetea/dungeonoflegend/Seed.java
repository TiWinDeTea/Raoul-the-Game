package com.github.tiwindetea.dungeonoflegend;

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

    public Seed(int alphaSeed, int betaSeed) {
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
}
