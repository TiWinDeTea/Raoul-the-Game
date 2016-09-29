//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.soundplayer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by organic-code on 5/30/16.
 */
public class UnifiedPlayers<K> {

    private Map<K, MediaPlayer> players;
    private boolean stopped = false;


    /**
     * Constructs an empty <tt>UnifiedPlayers</tt> with an hashmap of the default initial
     * capacity (5) and the default load factor (2).
     */
    public UnifiedPlayers() {
        this(5, 2);
    }

    /**
     * Constructs an empty <tt>UnifiedPlayers</tt> with an hashmap of the specified initial
     * capacity and the default load factor (2).
     *
     * @param initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     */
    public UnifiedPlayers(int initialCapacity) {
        this(initialCapacity, 2);
    }

    /**
     * Constructs an empty <tt>UnifiedPlayers</tt> with an hashmap of the specified initial
     * capacity and load factor.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *                                  or the load factor is nonpositive
     */
    public UnifiedPlayers(int initialCapacity, float loadFactor) {
        this.players = new HashMap<>(initialCapacity, loadFactor);
    }

    /**
     * Removes the mapping for the specified key from the map if present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public MediaPlayer remove(K key) {
        return this.players.remove(key);
    }

    /**
     * Removes the entry for the specified key only if it is currently
     * mapped to the specified value.
     *
     * @param key    key with which the specified value is associated
     * @param player player expected to be associated with the specified key
     * @return {@code true} if the value was removed
     */
    public boolean remove(K key, MediaPlayer player) {
        return this.players.remove(key, player);
    }

    /**
     * Removes the mapping for the specified keys from the map if present.
     *
     * @param keys keys whose mapping is to be removed from the map
     */
    public void removeAll(K... keys) {
        for (K key : keys) {
            this.remove(key);
        }
    }

    /**
     * Removes the mapping for the specified keys from the map if present.
     *
     * @param keys keys whose mapping is to be removed from the map
     */
    public void removeAll(Collection<K> keys) {
        keys.forEach(this::remove);
    }

    /**
     * Stops a player and removes the mapping for the specified key from the map if present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public MediaPlayer stopAndRemove(K key) {
        MediaPlayer p = this.players.get(key);
        if (p != null) {
            p.stop();
            return this.players.remove(key);
        } else {
            return null;
        }
    }

    /**
     * Stops a player
     *
     * @param key key with which the player is associated
     * @throws NoSuchElementException If the specified key has no associated
     *                                value or this value is <tt>null</tt>
     */
    public void stop(K key) {
        if (!this.stopped) {
            MediaPlayer p = this.players.get(key);
            if (p != null) {
                p.stop();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Stops a player and removes the entry for the specified key only if it is currently
     * mapped to the specified value.
     *
     * @param key    key with which the specified value is associated
     * @param player player expected to be associated with the specified key
     * @return {@code true} if the value was removed
     */
    public boolean stopAndRemove(K key, MediaPlayer player) {
        MediaPlayer local = this.players.get(key);
        if (local != null && local.equals(player)) {
            local.stop();
            return this.players.remove(key, player);
        } else {
            return false;
        }
    }

    /**
     * Stops players and removes the mapping for the specified keys from the map if present.
     *
     * @param keys keys whose mapping is to be removed from the map
     */
    public void stopAndRemoveAll(K... keys) {
        for (K key : keys) {
            this.stopAndRemove(key);
        }
    }

    /**
     * Stops players and removes the mapping for the specified keys from the map if present.
     *
     * @param keys keys whose mapping is to be removed from the map
     */
    public void stopAndRemoveAll(Collection<K> keys) {
        keys.forEach(this::stopAndRemove);
    }

    /**
     * Stops all players and removes all of the mappings from the map.
     * The map will be empty after this call returns.
     */
    public void stopAndClear() {
        this.players.forEach((k, player) -> {
            try {
                if (player != null) {
                    player.stop();
                }
                // javafx sucks
            } catch (NullPointerException e) {

            }
        });
        this.players.clear();
    }

    /**
     * Stops all specified players
     *
     * @param keys keys with which players are associated
     * @throws NoSuchElementException If a specified key has no associated
     *                                value or this value is <tt>null</tt>
     *                                (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public void stopAll(K... keys) {
        for (K key : keys) {
            stop(key);
        }
    }

    /**
     * Stops all specified players
     *
     * @param keys keys with which players are associated
     * @throws NoSuchElementException If a specified key has no associated
     *                                value or this value is <tt>null</tt>
     *                                (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public void stopAll(Collection<K> keys) {
        keys.forEach(this::stop);
    }

    /**
     * Stops any players of the map
     */
    public void stopAny() {
        this.players.forEach((k, player) -> player.stop());
    }

    /**
     * Associates a MediaPlayer targetting the file with the specified
     * key in the map. If the map previously contained a mapping for the
     * key, the old value is replaced and returned.
     *
     * @param key  key with which the specified value is to be associated
     * @param file path to the target file
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public MediaPlayer put(K key, URL file) {
        return this.players.put(key, new MediaPlayer(new Media(file.toString())));
    }

    public MediaPlayer put(K key, URL file, boolean isLooping) {
        MediaPlayer ret = new MediaPlayer(new Media(file.toString()));
        if (isLooping) {
            ret.setOnEndOfMedia(() -> ret.seek(Duration.ZERO));
        }
        return this.players.put(key, ret);
    }

    /**
     * Associates a MediaPlayer with the specified key in the map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced and returned.
     *
     * @param key    key with which the specified value is to be associated
     * @param player MediaPlayer to be associated with the specied key.
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public MediaPlayer put(K key, MediaPlayer player) {
        return this.players.put(key, player);
    }

    /**
     * Adds the file to the map and plays it.
     *
     * @param key  key with which the specified value is to be associated
     * @param file URL to the target file
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>.)
     *
     * @see UnifiedPlayers#put(Object, URL)
     * @see UnifiedPlayers#play(Object)
     */
    public MediaPlayer putAndPlay(K key, URL file) {
        return this.putAndPlay(key, new MediaPlayer(new Media(file.toString())));
    }

    /**
     * Adds the player to the map and plays it.
     *
     * @param key    key with which the specified value is to be associated
     * @param player MediaPlayer to be associated with the specied key.
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>.)
     *
     * @see UnifiedPlayers#put(Object, MediaPlayer)
     * @see UnifiedPlayers#play(Object)
     */
    public MediaPlayer putAndPlay(K key, MediaPlayer player) {
        MediaPlayer r = this.players.put(key, player);
        if (!this.stopped) {
            player.play();
        }
        return r;
    }

    /**
     * Starts the player associated to the specified key
     *
     * @param key key with which the player is associated
     */
    public void play(K key) {
        if (!this.stopped) {
            MediaPlayer r = this.players.get(key);
            if (r != null) {
                r.seek(Duration.ZERO);
                r.play();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Specify if the player associated to the key should loop
     * or not.
     *
     * @param key  key with which the player is associated
     * @param loop <tt>true</tt> if the music should loop, false otherwise
     * @throws NoSuchElementException If the specified key has no associated
     *                                value or this value is <tt>null</tt>
     * @apiNote This should not be modified when the music is playing
     * @apiNote <tt>false</tt> by default
     */
    public void setLoop(K key, boolean loop) {
        MediaPlayer p = this.players.get(key);
        if (p != null) {
            if (loop) {
                p.setOnEndOfMedia(() -> p.seek(Duration.ZERO));
            } else {
                p.setOnEndOfMedia(() -> {
                });
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Returns <tt>true</tt> if the player associated to
     * the key is playng a music
     *
     * @param key key with which the player is associated
     * @return <tt>true</tt> if the player associated to
     * the key is playng a music
     *
     * @throws NoSuchElementException If the specified key has no associated
     *                                value or this value is null
     */
    public boolean isPlaying(K key) {
        MediaPlayer p = this.players.get(key);
        if (p != null) {
            return p.getStatus().equals(MediaPlayer.Status.PLAYING);
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Removes all of the mappings from the map.
     * The map will be empty after this call returns.
     *
     * @apiNote All players will keep running forever if looping
     * or until manually stopped
     */
    public void clear() {
        this.players.clear();
    }

    public void setStopped(boolean stopped) {
        if (!this.stopped && stopped) {
            this.stopAny();
        }
        this.stopped = stopped;
    }

    public void pause(K key) {
        if (!this.stopped) {
            MediaPlayer p = this.players.get(key);
            if (p != null) {
                p.pause();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public void resume(K key) {
        if (!this.stopped) {
            MediaPlayer p = this.players.get(key);
            if (p != null) {
                p.play();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public boolean getStopped() {
        return this.stopped;
    }
}
