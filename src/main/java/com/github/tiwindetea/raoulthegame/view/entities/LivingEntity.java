//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.view.entities;

import com.github.tiwindetea.raoulthegame.model.space.Direction;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.view.RectangleSizeTransition;
import com.github.tiwindetea.raoulthegame.view.ViewPackage;
import com.sun.javafx.tk.Toolkit;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type LivingEntity.
 *
 * @author Maxime PINARD.
 */
public class LivingEntity extends Entity {
	private static final double HEALTH_RECTANGLES_HEIGHT = 1;
	private static final double HEALTH_RECTANGLES_WIDTH_RATIO = 0.8;
	private static final double HEALTH_RECTANGLES_WIDTH = HEALTH_RECTANGLES_WIDTH_RATIO * ViewPackage.SPRITES_SIZE.x;
	private static final Color MAX_HEALTH_RECTANGLE_COLOR = Color.MAROON;
	private static final Color ACTUAL_HEALTH_RECTANGLE_COLOR = Color.RED;
	private static final Duration ANIMATION_DURATION = Duration.millis(500);

	private static final Font TEXT_FONT = ViewPackage.getMainFont(14);
	private static final Font SMALL_TEXT_FONT = ViewPackage.getMainFont(8);
	private static final Color HEALTH_POSITIVE_MODIFICATION_TEXT_COLOR = Color.LIME;
	private static final Color HEALTH_NEGATIVE_MODIFICATION_TEXT_COLOR = Color.RED;
	private static final Color MANA_POSITIVE_MODIFICATION_TEXT_COLOR = Color.ROYALBLUE;
	private static final Color MANA_NEGATIVE_MODIFICATION_TEXT_COLOR = Color.ROYALBLUE;
	private static final Color XP_POSITIVE_MODIFICATION_TEXT_COLOR = Color.WHITE;
	private static final Color XP_NEGATIVE_MODIFICATION_TEXT_COLOR = Color.WHITE;
	private static final Duration TRANSITION_DURATION = new Duration(1500);
	private static final double TRANSITION_INITIAL_Y_POSITION = -12;
	private static final double TRANSITION_FINAL_Y_POSITION = -60;

	private final Rectangle maxHealthRectangle = new Rectangle(HEALTH_RECTANGLES_WIDTH, HEALTH_RECTANGLES_HEIGHT,MAX_HEALTH_RECTANGLE_COLOR);
	private final Rectangle actualHealthRectangle = new Rectangle(HEALTH_RECTANGLES_WIDTH, HEALTH_RECTANGLES_HEIGHT,ACTUAL_HEALTH_RECTANGLE_COLOR);

	private double healthProportion = 1;
	private LivingEntityType livingEntityType;
	private Direction direction;

	private int centerHealthLabelNumber = 0;
	private int centerManaLabelNumber = 0;
	private int centerXpLabelNumber = 0;

	private enum LabelPosistion {
		LEFT,
		CENTER,
		RIGHT
	}

	private enum LabelType {
		MANA,
		HEALTH,
		XP
	}

	/**
	 * Instantiates a new LivingEntity.
	 *
	 * @param livingEntityType the living entity type
	 * @param position         the position
	 * @param direction        the direction
	 * @param description      the description
	 */
	public LivingEntity(LivingEntityType livingEntityType, Vector2i position, Direction direction, String description) {
		super(position, description);
		this.livingEntityType = livingEntityType;
		this.imageView.setImage(livingEntityType.getImage());
		this.imageView.setTranslateX(0);
		this.imageView.setTranslateY(0);
		this.setDirection(direction);

		getChildren().add(this.imageView);

		this.maxHealthRectangle.setTranslateX((ViewPackage.SPRITES_SIZE.x - HEALTH_RECTANGLES_WIDTH) / 2);
		this.maxHealthRectangle.setTranslateY(ViewPackage.SPRITES_SIZE.y);
		this.actualHealthRectangle.setTranslateX((ViewPackage.SPRITES_SIZE.x - HEALTH_RECTANGLES_WIDTH) / 2);
		this.actualHealthRectangle.setTranslateY(ViewPackage.SPRITES_SIZE.y);

		getChildren().add(this.maxHealthRectangle);
		getChildren().add(this.actualHealthRectangle);
	}

	/**
	 * Sets position and direction.
	 * Use super class function to only set position
	 *
	 * @param position  the position
	 * @param direction the direction
	 */
	public void setPosition(Vector2i position, Direction direction) {
		this.setPosition(position);
		this.setDirection(direction);
	}

	/**
	 * Sets direction.
	 *
	 * @param direction the direction
	 */
	public void setDirection(Direction direction) {
		if(direction != this.direction) {
			this.direction = direction;
			Vector2i spritePosition = this.livingEntityType.getSpritePosition(this.direction);
			this.imageView.setViewport(new Rectangle2D(spritePosition.x * spriteSize.x, spritePosition.y * spriteSize.y, spriteSize.x, spriteSize.y));
		}
	}

	private void updateHealth() {
		RectangleSizeTransition transition = new RectangleSizeTransition(this.actualHealthRectangle, this.healthProportion * this.maxHealthRectangle.getWidth(), ANIMATION_DURATION);
		transition.play();
	}

	public double getHealthProportion() {
		return this.healthProportion;
	}

	public void setHealthProportion(double healthProportion) {
		this.healthProportion = healthProportion;
		updateHealth();
	}

	public void setHealthBarVisible(boolean visibility){
		this.actualHealthRectangle.setVisible(visibility);
		this.maxHealthRectangle.setVisible(visibility);
	}

	/**
	 * Gets direction.
	 *
	 * @return the direction
	 */
	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public boolean isVisibleOnFog() {
		return false;
	}

	public void displayHealthModification(int value) {
		if(value != 0) {
			Label label = new Label();
			label.setFont(TEXT_FONT);
			if(value > 0) {
				label.setText("+" + Integer.toString(value));
				label.setTextFill(HEALTH_POSITIVE_MODIFICATION_TEXT_COLOR);
			}
			else {
				label.setText(Integer.toString(value));
				label.setTextFill(HEALTH_NEGATIVE_MODIFICATION_TEXT_COLOR);
			}
			displaLabel(label, LabelType.HEALTH, LabelPosistion.CENTER);
		}
	}

	public void displayManaModification(int value) {
		if(value != 0) {
			Label label = new Label();
			label.setFont(SMALL_TEXT_FONT);
			if(value > 0) {
				label.setText("+" + Integer.toString(value));
				label.setTextFill(MANA_POSITIVE_MODIFICATION_TEXT_COLOR);
			}
			else {
				label.setText(Integer.toString(value));
				label.setTextFill(MANA_NEGATIVE_MODIFICATION_TEXT_COLOR);
			}
			displaLabel(label, LabelType.MANA, LabelPosistion.LEFT);
		}
	}

	public void displayXpModification(int value) {
		if(value != 0) {
			Label label = new Label();
			label.setFont(SMALL_TEXT_FONT);
			if(value > 0) {
				label.setText("+" + Integer.toString(value));
				label.setTextFill(XP_POSITIVE_MODIFICATION_TEXT_COLOR);
			}
			else {
				label.setText(Integer.toString(value));
				label.setTextFill(XP_NEGATIVE_MODIFICATION_TEXT_COLOR);
			}
			displaLabel(label, LabelType.XP, LabelPosistion.RIGHT);
		}
	}

	private void displaLabel(Label label, LabelType labelType, LabelPosistion labelPosistion) {
		getChildren().add(label);

		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setNode(label);
		fadeTransition.setDuration(TRANSITION_DURATION);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.setCycleCount(1);
		fadeTransition.setAutoReverse(false);

		TranslateTransition translateTransition = new TranslateTransition();
		translateTransition.setNode(label);
		translateTransition.setDuration(TRANSITION_DURATION);
		translateTransition.setFromY(TRANSITION_INITIAL_Y_POSITION);
		translateTransition.setToY(TRANSITION_FINAL_Y_POSITION);
		translateTransition.setCycleCount(1);
		translateTransition.setAutoReverse(false);

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Runnable runnable;

		switch(labelType) {
		default:
		case HEALTH: {
			label.setTranslateX((ViewPackage.SPRITES_SIZE.x - Toolkit.getToolkit().getFontLoader().computeStringWidth(label.getText(), label.getFont())) / 2);
			++this.centerHealthLabelNumber;
			runnable = new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
					--LivingEntity.this.centerHealthLabelNumber;
					getChildren().remove(label);
				}
			};
			break;
		}
		case MANA: {
			if(this.centerHealthLabelNumber < 1 && this.centerXpLabelNumber < 1) {
				label.setTranslateX((ViewPackage.SPRITES_SIZE.x - Toolkit.getToolkit().getFontLoader().computeStringWidth(label.getText(), label.getFont())) / 2);
				++this.centerManaLabelNumber;
				runnable = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
						--LivingEntity.this.centerManaLabelNumber;
						getChildren().remove(label);
					}
				};
			}
			else {
				label.setTranslateX((ViewPackage.SPRITES_SIZE.x - Toolkit.getToolkit().getFontLoader().computeStringWidth(label.getText(), label.getFont())) / 2 - 20);
				runnable = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
						getChildren().remove(label);
					}
				};
			}
			break;
		}
		case XP: {
			if(this.centerHealthLabelNumber < 1 && this.centerManaLabelNumber < 1) {
				label.setTranslateX((ViewPackage.SPRITES_SIZE.x - Toolkit.getToolkit().getFontLoader().computeStringWidth(label.getText(), label.getFont())) / 2);
				++this.centerXpLabelNumber;
				runnable = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
						--LivingEntity.this.centerXpLabelNumber;
						getChildren().remove(label);
					}
				};
			}
			else {
				label.setTranslateX((ViewPackage.SPRITES_SIZE.x - Toolkit.getToolkit().getFontLoader().computeStringWidth(label.getText(), label.getFont())) / 2 + 20);
				runnable = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
						getChildren().remove(label);
					}
				};
			}
			break;
		}
		}

		fadeTransition.play();
		translateTransition.play();

		executorService.submit(runnable);
		executorService.shutdown();
	}
}
