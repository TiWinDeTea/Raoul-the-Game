package com.github.tiwindetea.raoulthegame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by maxime on 9/18/16.
 */
public class ImageViewWrapper {

	private final ImageView imageView;
	private final double initial_width;
	private final double initial_height;
	private double width;
	private double height;

	public ImageViewWrapper(Image image) {
		this.imageView = new ImageView(image);
		this.initial_width = image.getWidth();
		this.initial_height = image.getHeight();
		this.width = this.initial_width;
		this.height = this.initial_height;
	}

	public void setFitWidth(double width) {
		this.width = width;
		this.imageView.setFitWidth(this.width);
	}

	public void setFitHeight(double height) {
		this.height = height;
		this.imageView.setFitHeight(height);
	}

	public double getInitial_width() {
		return this.initial_width;
	}

	public double getInitial_height() {
		return this.initial_height;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public ImageView getImageView() {
		return this.imageView;
	}

	public void setPreserveRatio(boolean value) {
		this.imageView.setPreserveRatio(value);
	}
}
