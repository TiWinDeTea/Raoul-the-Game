//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Pair<T extends Object>
 *
 * @param <T> the type parameter
 * @author Lucas LAZARE
 */
public class Pair<T extends Object> {
    private static long currID = Long.MIN_VALUE;
    public static final long ERROR_VAL = Long.MAX_VALUE;
    private long id;
    public T object;

    /**
     * Instantiates a new Pair with a unique id.
     *
     * @param object the object
     */
    public Pair(T object) {
        if (currID == Long.MAX_VALUE) {
            throw new RuntimeException("Reached max ID");
        }
        this.object = object;
        this.id = currID++;
    }

    /**
     * Generate a pair using the given ID (/!\no checks done on the id)
     *
     * @param id     the id
     * @param object the object
     */
    public Pair(long id, T object) {
        this.id = id;
        this.object = object;
    }

    /**
     * Instantiates a new Pair. (for research purposes)
     *
     * @param id the id
     */
    public Pair(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        /* Asserting o to be a Pair<T> */
        return ((Pair<T>) o).getId() == this.getId();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return this.id;
    }
}
