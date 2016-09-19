//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The type ImageViewWrapper.
 *
 * @author Maxime PINARD.
 */
public class ImageViewWrapper {

	private final ImageView imageView;
	private final double initial_width;
	private final double initial_height;
	private double width;
	private double height;

	/**
	 * Instantiates a new ImageViewWrapper.
	 *
	 * @param image the image
	 */
	public ImageViewWrapper(Image image) {
		this.imageView = new ImageView(image);
		this.initial_width = image.getWidth();
		this.initial_height = image.getHeight();
		this.width = this.initial_width;
		this.height = this.initial_height;
	}

	/**
	 * Sets fit width.
	 *
	 * @param width the width
	 */
	public void setFitWidth(double width) {
		this.width = width;
		this.imageView.setFitWidth(this.width);
	}

	/**
	 * Sets fit height.
	 *
	 * @param height the height
	 */
	public void setFitHeight(double height) {
		this.height = height;
		this.imageView.setFitHeight(height);
	}

	/**
	 * Gets initial width.
	 *
	 * @return the initial width
	 */
	public double getInitial_width() {
		return this.initial_width;
	}

	/**
	 * Gets initial height.
	 *
	 * @return the initial height
	 */
	public double getInitial_height() {
		return this.initial_height;
	}

	/**
	 * Gets width.
	 *
	 * @return the width
	 */
	public double getWidth() {
		return this.width;
	}

	/**
	 * Gets height.
	 *
	 * @return the height
	 */
	public double getHeight() {
		return this.height;
	}

	/**
	 * Gets image view.
	 *
	 * @return the image view
	 */
	public ImageView getImageView() {
		return this.imageView;
	}

	/**
	 * Sets preserve ratio.
	 *
	 * @param value the value
	 */
	public void setPreserveRatio(boolean value) {
		this.imageView.setPreserveRatio(value);
	}
}
