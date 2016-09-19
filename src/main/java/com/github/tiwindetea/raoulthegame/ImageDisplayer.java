//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * The type ImageDisplayer.
 *
 * @author Maxime PINARD.
 */
public class ImageDisplayer {

	private static final Color BACKGROUND_COLOR = Color.DARKGRAY;
	private final StackPane tutoStackPane = new StackPane();
	private final Scene scene = new Scene(this.tutoStackPane);
	private final List<ImageViewWrapper> imageViewWrapperList = new ArrayList<>();

	private int imageIndex = 0;

	/**
	 * Instantiates a new ImageDisplayer.
	 *
	 * @param images the images
	 */
	public ImageDisplayer(Image... images) {

		this.tutoStackPane.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

		for(Image image : images) {
			ImageViewWrapper imageViewWrapper = new ImageViewWrapper(image);
			this.imageViewWrapperList.add(imageViewWrapper);
			imageViewWrapper.setPreserveRatio(true);
		}

		this.tutoStackPane.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				for(ImageViewWrapper imageViewWrapper : ImageDisplayer.this.imageViewWrapperList) {
					if(ImageDisplayer.this.tutoStackPane.getWidth() / ImageDisplayer.this.tutoStackPane.getHeight() < imageViewWrapper.getInitial_width() / imageViewWrapper.getInitial_height()) {
						imageViewWrapper.setFitWidth(ImageDisplayer.this.tutoStackPane.getWidth());
						//imageViewWrapper.setFitHeight(imageViewWrapper.getInitial_width() * ImageDisplayer.this.tutoStackPane.getHeight() / ImageDisplayer.this.tutoStackPane.getWidth());
					}
					else {
						imageViewWrapper.setFitHeight(ImageDisplayer.this.tutoStackPane.getHeight());
						//imageViewWrapper.setFitWidth(imageViewWrapper.getInitial_height() * ImageDisplayer.this.tutoStackPane.getWidth() / ImageDisplayer.this.tutoStackPane.getHeight());
					}
				}
			}
		});

		this.tutoStackPane.getChildren().add(this.imageViewWrapperList.get(this.imageIndex).getImageView());

		this.scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch(event.getCode()) {
				case LEFT:
					if(ImageDisplayer.this.imageIndex > 0) {
						ImageDisplayer.this.tutoStackPane.getChildren().clear();
						ImageDisplayer.this.tutoStackPane.getChildren().add(ImageDisplayer.this.imageViewWrapperList.get(--ImageDisplayer.this.imageIndex).getImageView());
					}
					break;
				case RIGHT:
					if(ImageDisplayer.this.imageIndex < ImageDisplayer.this.imageViewWrapperList.size() - 1) {
						ImageDisplayer.this.tutoStackPane.getChildren().clear();
						ImageDisplayer.this.tutoStackPane.getChildren().add(ImageDisplayer.this.imageViewWrapperList.get(++ImageDisplayer.this.imageIndex).getImageView());
					}
					break;
				}
			}
		});
	}

	/**
	 * Gets scene.
	 *
	 * @return the scene
	 */
	public Scene getScene() {
		return this.scene;
	}

	/**
	 * Return to the first image.
	 */
	public void reset() {
		this.imageIndex = 0;
		ImageDisplayer.this.tutoStackPane.getChildren().clear();
		this.tutoStackPane.getChildren().add(this.imageViewWrapperList.get(this.imageIndex).getImageView());
	}
}
