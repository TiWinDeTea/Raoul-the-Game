//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame;

import com.github.tiwindetea.raoulthegame.view.ViewPackage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxime on 9/17/16.
 */
public class Tutorial {

	private static final Color BACKGROUND_COLOR = Color.DARKGRAY;
	private final StackPane tutoStackPane = new StackPane();
	private final Scene scene = new Scene(this.tutoStackPane);
	private final List<ImageViewWrapper> imageViewWrapperList = new ArrayList<>();
	private final ImageViewWrapper tuto1 = new ImageViewWrapper(ViewPackage.TUTORIAL_1_IMAGE);
	private final ImageViewWrapper tuto2 = new ImageViewWrapper(ViewPackage.TUTORIAL_2_IMAGE);

	private int imageIndex = 0;

	public Tutorial() {

		this.tutoStackPane.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

		this.imageViewWrapperList.add(this.tuto1);
		this.tuto1.setPreserveRatio(true);
		this.imageViewWrapperList.add(this.tuto2);
		this.tuto2.setPreserveRatio(true);

		this.tutoStackPane.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				for(ImageViewWrapper imageViewWrapper : Tutorial.this.imageViewWrapperList) {
					if(Tutorial.this.tutoStackPane.getWidth() / Tutorial.this.tutoStackPane.getHeight() < imageViewWrapper.getInitial_width() / imageViewWrapper.getInitial_height()) {
						imageViewWrapper.setFitWidth(Tutorial.this.tutoStackPane.getWidth());
						imageViewWrapper.setFitHeight(imageViewWrapper.getInitial_width() * Tutorial.this.tutoStackPane.getHeight() / Tutorial.this.tutoStackPane.getWidth());
					}
					else {
						imageViewWrapper.setFitHeight(Tutorial.this.tutoStackPane.getHeight());
						imageViewWrapper.setFitWidth(imageViewWrapper.getInitial_height() * Tutorial.this.tutoStackPane.getWidth() / Tutorial.this.tutoStackPane.getHeight());
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
					if(Tutorial.this.imageIndex > 0) {
						Tutorial.this.tutoStackPane.getChildren().clear();
						Tutorial.this.tutoStackPane.getChildren().add(Tutorial.this.imageViewWrapperList.get(--Tutorial.this.imageIndex).getImageView());
					}
					break;
				case RIGHT:
					if(Tutorial.this.imageIndex < Tutorial.this.imageViewWrapperList.size() - 1) {
						Tutorial.this.tutoStackPane.getChildren().clear();
						Tutorial.this.tutoStackPane.getChildren().add(Tutorial.this.imageViewWrapperList.get(++Tutorial.this.imageIndex).getImageView());
					}
					break;
				}
			}
		});
	}

	public Scene getScene() {
		return this.scene;
	}

	public void reset() {
		this.imageIndex = 0;
		Tutorial.this.tutoStackPane.getChildren().clear();
		this.tutoStackPane.getChildren().add(this.imageViewWrapperList.get(this.imageIndex).getImageView());
	}
}
