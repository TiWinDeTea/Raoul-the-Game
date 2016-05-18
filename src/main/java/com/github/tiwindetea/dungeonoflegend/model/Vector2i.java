//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Vector2i
 *
 * @author Lucas LAZARE
 * @author Maxime PINARD
 */
public class Vector2i {
    public int x, y;

	/**
	 * Instantiates a new Vector 2 i.
	 */
	public Vector2i() {
	}

	/**
	 * Copy Constructor
	 *
	 * @param v A instance of Vector2i
	 */
	public Vector2i(Vector2i v) {
		this.x = v.x;
		this.y = v.y;
	}

	/**
	 * Instantiates a new Vector 2 i.
	 *
	 * @param x x
	 * @param y y
	 */
	public Vector2i(int x, int y) {

		this.x = x;
		this.y = y;
	}

	/**
	 * Sets a Vector2i.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Equals.
	 *
	 * @param vect an instance of Vector2i
	 * @return true if both vectors are equals, false otherwise
	 */
	public boolean equals(Vector2i vect) {
		return this.x == vect.x && this.y == vect.y;
	}

	/**
	 * Adds two vector2i.
	 *
	 * @param vect an instance of Vector2i
	 * @return this
	 */
	public Vector2i add(Vector2i vect) {
		this.x += vect.x;
		this.y += vect.y;
		return this;
	}

	/**
	 * Adds a vector2i and a direction.
	 *
	 * @param dir a direction (not nullable)
	 * @return this
	 */
	public Vector2i add(Direction dir) {
		switch(dir) {
		case UP:
			--this.y;
			break;
		case DOWN:
			++this.y;
			break;
		case LEFT:
			--this.x;
			break;
		case RIGHT:
			++this.x;
			break;
		}
		return this;
	}

	public double squaredDistance(Vector2i that) {
		return Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2);
	}

	/**
	 * Multiplies a Vector2i by a scalar.
	 *
	 * @param a the scalar
	 * @return this
	 */
	public Vector2i multiply(int a) {
		this.x *= a;
		this.y *= a;
		return this;
	}

	/**
	 * Copies this.
	 *
	 * @return A copy of this.
	 */
	public Vector2i copy() {
		return new Vector2i(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Vector2i{" +
				"x=" + this.x +
				", y=" + this.y +
				'}';
	}
}
