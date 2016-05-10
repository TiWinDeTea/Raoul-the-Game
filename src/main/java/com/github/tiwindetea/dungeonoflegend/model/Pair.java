package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by organic-code on 5/9/16.
 */
public class Pair<T extends Object> {
    private static long currID = Long.MIN_VALUE;
    public static final long ERROR_VAL = Long.MAX_VALUE;
    private long id;
    public T object;

    public Pair() {

    }

    public Pair(T object) {
        if (currID == Long.MAX_VALUE) {
            throw new RuntimeException("Reached max ID");
        }
        this.object = object;
        this.id = currID++;
    }

    /* Generate a pair using the given ID /!\(no checks done on the id) */
    public Pair(long id, T object) {
        this.id = id;
        this.object = object;
    }

    /* For research purposes */
    public Pair(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        /* Asserting o to be a Pair<T> */
        return ((Pair<T>) o).getId() == this.getId();
    }

    public long getId() {
        return this.id;
    }
}
