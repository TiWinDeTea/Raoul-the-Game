//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.view.entities;

import com.github.tiwindetea.raoulthegame.model.Vector2i;
import com.github.tiwindetea.raoulthegame.view.InformationsDisplayer;
import com.github.tiwindetea.raoulthegame.view.ViewPackage;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;

/**
 * The type Entity.
 *
 * @author Maxime PINARD.
 */
public abstract class Entity extends Parent {
	private static final Duration ANIMATION_DURATION = new Duration(100);
	protected static final Vector2i spriteSize = ViewPackage.SPRITES_SIZE;
	private final Rectangle backgroundRectangle = new Rectangle(spriteSize.x, spriteSize.y);
	protected final ImageView imageView = new ImageView();
	protected Vector2i position;
	protected boolean[][] LOS;

	private String description;

	/**
	 * Instantiates a new Entity.
	 *
	 * @param position    the position
	 * @param description the description
	 */
	public Entity(Vector2i position, String description) {
		this.position = new Vector2i(position);
		setTranslateX(this.position.x * ViewPackage.SPRITES_SIZE.x);
		setTranslateY(this.position.y * ViewPackage.SPRITES_SIZE.y);
		this.description = description;

		getChildren().add(this.backgroundRectangle);
		this.backgroundRectangle.setFill(Color.TRANSPARENT);
		this.backgroundRectangle.setTranslateX(0);
		this.backgroundRectangle.setTranslateY(0);

		this.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.setText(description);
			}
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.clear();
			}
		});
	}

	/**
	 * Sets position.
	 *
	 * @param position the position
	 */
	public void setPosition(Vector2i position) {
		TranslateTransition translateTransition = new TranslateTransition();
		translateTransition.setNode(this);
		translateTransition.setDuration(ANIMATION_DURATION);
		translateTransition.setFromX(this.position.x * ViewPackage.SPRITES_SIZE.x);
		translateTransition.setFromY(this.position.y * ViewPackage.SPRITES_SIZE.y);
		translateTransition.setToX(position.x * ViewPackage.SPRITES_SIZE.x);
		translateTransition.setToY(position.y * ViewPackage.SPRITES_SIZE.y);
		translateTransition.setCycleCount(1);
		translateTransition.setAutoReverse(false);
		translateTransition.play();

		this.position = new Vector2i(position);
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
	 * Gets position.
	 *
	 * @return the position
	 */
	public Vector2i getPosition() {
		return this.position;
	}

	/**
	 * Sets LOS.
	 *
	 * @param LOS the LOS
	 */
	public void setLOS(boolean[][] LOS) {
		if(LOS == null || LOS.length == 0)
			return;
		if((LOS.length & 1) == 0) {
			System.out.println("Warning: LOS width not odd: "+LOS.length);
		}
		if((LOS[0].length & 1) == 0) {
			System.out.println("Warning: LOS height not odd: "+LOS[0].length);
		}
		this.LOS = LOS;
	}

	/**
	 * Gets LOS.
	 *
	 * @return the LOS
	 */
	public boolean[][] getLOS() {
		return this.LOS;
	}

	/**
	 * Modifie los.
	 *
	 * @param modifiedTilesPositions the modified tiles positions
	 */
	public void modifieLOS(List<Vector2i> modifiedTilesPositions) {
		if(this.LOS != null && this.LOS.length > 0) {
			for(Vector2i modifiedTilesPosition : modifiedTilesPositions) {
				if(((modifiedTilesPosition.x >= 0) && (modifiedTilesPosition.x < this.LOS.length)) && ((modifiedTilesPosition.y >= 0) && (modifiedTilesPosition.y >= this.LOS[0].length))) {
					this.LOS[modifiedTilesPosition.x][modifiedTilesPosition.y] = !this.LOS[modifiedTilesPosition.x][modifiedTilesPosition.y];
				}
			}
		}
	}

	/**
	 * Gets visibility on fog.
	 *
	 * @return the boolean
	 */
	public abstract boolean isVisibleOnFog();
}
