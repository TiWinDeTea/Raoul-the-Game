package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by organic-code on 5/9/16.
 */
public class Pair<T extends Object> {
    private static long currID = Long.MIN_VALUE;
    public long id;
    public T object;

    public Pair(T object) {
        if (currID == Long.MAX_VALUE) {
            throw new RuntimeException("Reached max ID");
        }
        this.object = object;
        this.id = currID++;
    }

    @Override
    public boolean equals(Object o) {
        /* Asserting o to be a Pair<T> */
        return ((Pair<T>) o).id == this.id;
    }
}
