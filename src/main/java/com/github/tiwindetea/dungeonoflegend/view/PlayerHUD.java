//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * The type PlayerHUD.
 *
 * @author Maxime PINARD
 */
public class PlayerHUD extends Parent {
	private static final Vector2i MAIN_PANE_SIZE = new Vector2i(400, 100);
	private static final Vector2i PLAYER_PICTURE_SIZE = new Vector2i(64, 64);
	private static final double HEALTH_RECTANGLES_HEIGHT = 20;
	private static final double MANA_RECTANGLES_HEIGHT = 15;
	private static final double MANA_RECTANGLES_WIDTH = 0.75; //relative to healthRectangleWidth (auto computed)
	private static final double XP_RECTANGLES_HEIGHT = 5;
	private static final double XP_RECTANGLES_WIDTH = 0.9; //relative to healthRectangleWidth (auto computed)
	private static final int SPACE = 20;

	private static final Color MASK_COLOR = Color.rgb(0, 0, 0, 0.5);
	private static final Color MAX_HEALTH_RECTANGLE_COLOR = Color.DARKRED;
	private static final Color ACTUAL_HEALTH_RECTANGLE_COLOR = Color.CRIMSON;
	private static final Color MAX_MANA_RECTANGLE_COLOR = Color.DARKBLUE;
	private static final Color ACTUAL_MANA_RECTANGLE_COLOR = Color.BLUE;
	private static final Color MAX_XP_RECTANGLE_COLOR = Color.DARKGREEN;
	private static final Color ACTUAL_XP_RECTANGLE_COLOR = Color.CHARTREUSE;
	private static final Color BACKGROUND_COLOR = Color.PURPLE;

	private static final Duration ANIMATION_DURATION = Duration.millis(1000);

	private static final Font HEALTH_LABEL_FONT = ViewPackage.getMainFont(15);
	private static final Color HEALTH_LABEL_TEXT_COLOR = Color.WHITE;
	private static final int HEALTH_LABEL_TRANSLATE_Y = 37;
	private static final Font MANA_LABEL_FONT = ViewPackage.getMainFont(10);
	private static final Color MANA_LABEL_TEXT_COLOR = Color.WHITE;
	private static final int MANA_LABEL_TRANSLATE_Y = 57;
	private static final int LABELS_OFFSET = 5;

	private static final Font LEVEL_LABEL_FONT = ViewPackage.getMainFont(20);
	private static final Color LEVEL_LABEL_TEXT_COLOR = Color.WHITE;
	private static final int LEVEL_LABEL_TRANSLATE_Y = 5;

	private final ImageView playerPicture;
	private final Rectangle maxHealthRectangle = new Rectangle();
	private final Rectangle actualHealthRectangle = new Rectangle();
	private final Rectangle maxManaRectangle = new Rectangle();
	private final Rectangle actualManaRectangle = new Rectangle();
	private final Rectangle maxXPRectangle = new Rectangle();
	private final Rectangle actualXPRectangle = new Rectangle();
	private final Rectangle backGroundRectangle = new Rectangle(MAIN_PANE_SIZE.x, MAIN_PANE_SIZE.y, BACKGROUND_COLOR);
	private final Rectangle maskRectangle = new Rectangle(MAIN_PANE_SIZE.x, MAIN_PANE_SIZE.y, BACKGROUND_COLOR);

	private final Label healthLabel = new Label();
	private final Label manaLabel = new Label();
	private final Label levelLabel = new Label();
	private final SimpleStringProperty healthString = new SimpleStringProperty();
	private final SimpleStringProperty manaString = new SimpleStringProperty();
	private final SimpleStringProperty levelString = new SimpleStringProperty();

	private final ImageView backgroundImage = new ImageView(ViewPackage.HUD_IMAGE);

	private String description;
	private String playerName;
	private int maxHealth;
	private int actualHealth;
	private int maxMana;
	private int actualMana;
	private int maxXP;
	private int actualXP;
	private int actualLevel;
	private int actualDamages;
	private int actualArmor;
	private int actualRange;
	private int actualPowerGrade;

	/**
	 * Instantiates a new PlayerHUD.
	 *
	 * @param playerPicture the player picture
	 * @param actualHealth  the actual health
	 * @param maxHealth     the max health
	 * @param actualMana    the actual mana
	 * @param maxMana       the max mana
	 * @param actualXP      the actual xp
	 * @param maxXP         the max xp
	 * @param actualLevel   the actual level
	 */
	public PlayerHUD(ImageView playerPicture, int actualHealth, int maxHealth, int actualMana, int maxMana, int actualXP, int maxXP, int actualLevel, String playerName) {
		this.playerPicture = playerPicture;
		this.actualHealth = actualHealth;
		this.maxHealth = maxHealth;
		this.actualMana = actualMana;
		this.maxMana = maxMana;
		this.actualXP = actualXP;
		this.maxXP = maxXP;
		this.actualLevel = actualLevel;
		this.playerName = playerName;
		this.init();
	}

	private void init() {
		this.getChildren().add(this.backGroundRectangle);
		this.getChildren().add(this.backgroundImage);

		this.getChildren().add(this.playerPicture);
		double originalWidth = this.playerPicture.getViewport().getWidth();
		double originalHeight = this.playerPicture.getViewport().getHeight();
		this.playerPicture.setScaleX(PLAYER_PICTURE_SIZE.x / this.playerPicture.getViewport().getWidth());
		this.playerPicture.setScaleY(PLAYER_PICTURE_SIZE.y / this.playerPicture.getViewport().getHeight());
		this.playerPicture.setLayoutX(this.playerPicture.getLayoutX() - (originalWidth - PLAYER_PICTURE_SIZE.x) / 2);
		this.playerPicture.setLayoutY(this.playerPicture.getLayoutY() - (originalHeight - PLAYER_PICTURE_SIZE.y) / 2);
		this.playerPicture.setTranslateX(SPACE);
		this.playerPicture.setTranslateY((MAIN_PANE_SIZE.y - PLAYER_PICTURE_SIZE.y) / 2.0);

		this.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.setText(PlayerHUD.this.description);
			}
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.clear();
			}
		});

		this.getChildren().add(this.maxHealthRectangle);
		this.maxHealthRectangle.setTranslateX(2 * SPACE + PLAYER_PICTURE_SIZE.x);
		this.maxHealthRectangle.setTranslateY((MAIN_PANE_SIZE.y - HEALTH_RECTANGLES_HEIGHT - MANA_RECTANGLES_HEIGHT - XP_RECTANGLES_HEIGHT) / 2.0d + XP_RECTANGLES_HEIGHT);
		this.maxHealthRectangle.setWidth(MAIN_PANE_SIZE.x - 3 * SPACE - PLAYER_PICTURE_SIZE.x);
		this.maxHealthRectangle.setHeight(HEALTH_RECTANGLES_HEIGHT);
		this.maxHealthRectangle.setFill(MAX_HEALTH_RECTANGLE_COLOR);

		this.getChildren().add(this.actualHealthRectangle);
		this.actualHealthRectangle.setTranslateX(this.maxHealthRectangle.getTranslateX());
		this.actualHealthRectangle.setTranslateY(this.maxHealthRectangle.getTranslateY());
		this.actualHealthRectangle.setWidth((double) (this.actualHealth) / this.maxHealth * this.maxHealthRectangle.getWidth());
		this.actualHealthRectangle.setHeight(HEALTH_RECTANGLES_HEIGHT);
		this.actualHealthRectangle.setFill(ACTUAL_HEALTH_RECTANGLE_COLOR);

		this.getChildren().add(this.maxManaRectangle);
		this.maxManaRectangle.setTranslateX(2 * SPACE + PLAYER_PICTURE_SIZE.x);
		this.maxManaRectangle.setTranslateY((MAIN_PANE_SIZE.y - HEALTH_RECTANGLES_HEIGHT - MANA_RECTANGLES_HEIGHT - XP_RECTANGLES_HEIGHT) / 2.0d + XP_RECTANGLES_HEIGHT + HEALTH_RECTANGLES_HEIGHT);
		this.maxManaRectangle.setWidth(MANA_RECTANGLES_WIDTH * this.maxHealthRectangle.getWidth());
		this.maxManaRectangle.setHeight(MANA_RECTANGLES_HEIGHT);
		this.maxManaRectangle.setFill(MAX_MANA_RECTANGLE_COLOR);

		this.getChildren().add(this.actualManaRectangle);
		this.actualManaRectangle.setTranslateX(this.maxManaRectangle.getTranslateX());
		this.actualManaRectangle.setTranslateY(this.maxManaRectangle.getTranslateY());
		this.actualManaRectangle.setWidth((double) (this.actualMana) / this.maxMana * this.maxManaRectangle.getWidth());
		this.actualManaRectangle.setHeight(MANA_RECTANGLES_HEIGHT);
		this.actualManaRectangle.setFill(ACTUAL_MANA_RECTANGLE_COLOR);

		this.getChildren().add(this.maxXPRectangle);
		this.maxXPRectangle.setTranslateX(2 * SPACE + PLAYER_PICTURE_SIZE.x);
		this.maxXPRectangle.setTranslateY((MAIN_PANE_SIZE.y - HEALTH_RECTANGLES_HEIGHT - MANA_RECTANGLES_HEIGHT - XP_RECTANGLES_HEIGHT) / 2.0d);
		this.maxXPRectangle.setWidth(XP_RECTANGLES_WIDTH * this.maxHealthRectangle.getWidth());
		this.maxXPRectangle.setHeight(XP_RECTANGLES_HEIGHT);
		this.maxXPRectangle.setFill(MAX_XP_RECTANGLE_COLOR);

		this.getChildren().add(this.actualXPRectangle);
		this.actualXPRectangle.setTranslateX(this.maxXPRectangle.getTranslateX());
		this.actualXPRectangle.setTranslateY(this.maxXPRectangle.getTranslateY());
		this.actualXPRectangle.setWidth(((double) (this.actualXP) / this.maxXP) * this.maxXPRectangle.getWidth());
		this.actualXPRectangle.setHeight(XP_RECTANGLES_HEIGHT);
		this.actualXPRectangle.setFill(ACTUAL_XP_RECTANGLE_COLOR);

		this.getChildren().add(this.healthLabel);
		this.healthLabel.setFont(HEALTH_LABEL_FONT);
		this.healthLabel.setTextFill(HEALTH_LABEL_TEXT_COLOR);
		this.healthLabel.textProperty().bind(this.healthString);
		this.healthString.set(this.maxHealth + " / " + this.actualHealth);
		this.healthLabel.setTranslateX(this.maxHealthRectangle.getTranslateX() + LABELS_OFFSET);
		this.healthLabel.setTranslateY(HEALTH_LABEL_TRANSLATE_Y);

		this.getChildren().add(this.manaLabel);
		this.manaLabel.setFont(MANA_LABEL_FONT);
		this.manaLabel.setTextFill(MANA_LABEL_TEXT_COLOR);
		this.manaLabel.textProperty().bind(this.manaString);
		this.manaString.set(this.maxHealth + " / " + this.actualHealth);
		this.manaLabel.setTranslateX(this.maxHealthRectangle.getTranslateX() + LABELS_OFFSET);
		this.manaLabel.setTranslateY(MANA_LABEL_TRANSLATE_Y);

		this.getChildren().add(this.levelLabel);
		this.levelLabel.setFont(LEVEL_LABEL_FONT);
		this.levelLabel.setTextFill(LEVEL_LABEL_TEXT_COLOR);
		this.levelLabel.textProperty().bind(this.levelString);
		this.levelString.set(Integer.toString(this.actualLevel));
		this.levelLabel.setTranslateX(2 * SPACE + PLAYER_PICTURE_SIZE.x);
		this.levelLabel.setTranslateY(LEVEL_LABEL_TRANSLATE_Y);

		getChildren().add(this.maskRectangle);
		this.maskRectangle.setFill(MASK_COLOR);
		this.maskRectangle.setVisible(false);
	}

	/**
	 * Gets the main Pane size.
	 *
	 * @return the size
	 */
	public static Vector2i getSize() {
		return MAIN_PANE_SIZE;
	}

	private void updateHealth() {
		RectangleSizeTransition transition = new RectangleSizeTransition(this.actualHealthRectangle, (double) (this.actualHealth) / this.maxHealth * this.maxHealthRectangle.getWidth(), ANIMATION_DURATION);
		this.healthString.set(this.actualHealth + " / " + this.maxHealth);
		transition.play();
	}

	private void updateMana() {
		RectangleSizeTransition transition = new RectangleSizeTransition(this.actualManaRectangle, (double) (this.actualMana) / this.maxMana * this.maxManaRectangle.getWidth(), ANIMATION_DURATION);
		this.manaString.set(this.actualMana + " / " + this.maxMana);
		transition.play();
	}

	private void updateXP() {
		RectangleSizeTransition transition = new RectangleSizeTransition(this.actualXPRectangle, (double) (this.actualXP) / this.maxXP * this.maxXPRectangle.getWidth(), ANIMATION_DURATION);
		transition.play();
	}

	private void updateDescription() {
		this.description =
		  this.playerName +
			"\n" +
			"\nLevel: " + this.actualLevel +
			"\nXP to next level: " + (this.maxXP - this.actualXP) +
			"\nHealth: " + this.actualHealth + " / " + this.maxHealth +
			"\nMana: " + this.actualMana + " / " + this.maxMana +
			"\n" +
			"\nDamages: " + this.actualDamages +
			"\nArmor: " + this.actualArmor +
		    "\nRange: " + this.actualRange +
		    "\n" +
		    "\nPower grade: " + this.actualPowerGrade;
	}

	/**
	 * Sets masked (with a transparent black rectangle over).
	 *
	 * @param masked true for masked, false for not masked
	 */
	public void setMasked(boolean masked) {
		this.maskRectangle.setVisible(masked);
	}

	/**
	 * Sets max health.
	 *
	 * @param maxHealth the max health
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		this.updateHealth();
		updateDescription();
	}

	/**
	 * Sets actual health.
	 *
	 * @param actualHealth the actual health
	 */
	public void setActualHealth(int actualHealth) {
		this.actualHealth = actualHealth;
		this.updateHealth();
		updateDescription();
	}

	/**
	 * Sets max mana.
	 *
	 * @param maxMana the max mana
	 */
	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
		this.updateMana();
		updateDescription();
	}

	/**
	 * Sets actual mana.
	 *
	 * @param actualMana the actual mana
	 */
	public void setActualMana(int actualMana) {
		this.actualMana = actualMana;
		this.updateMana();
		updateDescription();
	}

	/**
	 * Sets max xp.
	 *
	 * @param maxXP the max xp
	 */
	public void setMaxXP(int maxXP) {
		this.maxXP = maxXP;
		this.updateXP();
		updateDescription();
	}

	/**
	 * Sets actual xp.
	 *
	 * @param actualXP the actual xp
	 */
	public void setActualXP(int actualXP) {
		this.actualXP = actualXP;
		this.updateXP();
		updateDescription();
	}

	/**
	 * Sets actual level.
	 *
	 * @param actualLevel the actual level
	 */
	public void setActualLevel(int actualLevel) {
		this.actualLevel = actualLevel;
		this.levelString.set(Integer.toString(this.actualLevel));
		updateDescription();
	}

	/**
	 * Sets actual damages.
	 *
	 * @param actualDamages the actual damages
	 */
	public void setActualDamages(int actualDamages) {
		this.actualDamages = actualDamages;
		updateDescription();
	}

	/**
	 * Sets actual armor.
	 *
	 * @param actualArmor the actual armor
	 */
	public void setActualArmor(int actualArmor) {
		this.actualArmor = actualArmor;
		updateDescription();
	}

	/**
	 * Sets actual range.
	 *
	 * @param actualRange the actual range
	 */
	public void setActualRange(int actualRange) {
		this.actualRange = actualRange;
		updateDescription();
	}

	/**
	 * Sets actual power grade.
	 *
	 * @param actualPowerGrade the actual power grade
	 */
	public void setActualPowerGrade(int actualPowerGrade) {
		this.actualPowerGrade = actualPowerGrade;
		updateDescription();
	}

	/**
	 * Gets max health.
	 *
	 * @return the max health
	 */
	public int getMaxHealth() {
		return this.maxHealth;
	}

	/**
	 * Gets actual health.
	 *
	 * @return the actual health
	 */
	public int getActualHealth() {
		return this.actualHealth;
	}

	/**
	 * Gets max mana.
	 *
	 * @return the max mana
	 */
	public int getMaxMana() {
		return this.maxMana;
	}

	/**
	 * Gets actual mana.
	 *
	 * @return the actual mana
	 */
	public int getActualMana() {
		return this.actualMana;
	}

	/**
	 * Gets max xp.
	 *
	 * @return the max xp
	 */
	public int getMaxXP() {
		return this.maxXP;
	}

	/**
	 * Gets actual xp.
	 *
	 * @return the actual xp
	 */
	public int getActualXP() {
		return this.actualXP;
	}

	/**
	 * Gets actual level.
	 *
	 * @return the actual level
	 */
	public int getActualLevel() {
		return this.actualLevel;
	}

	/**
	 * Gets actual damages.
	 *
	 * @return the actual damages
	 */
	public int getActualDamages() {
		return this.actualDamages;
	}

	/**
	 * Gets actual armor.
	 *
	 * @return the actual armor
	 */
	public int getActualArmor() {
		return this.actualArmor;
	}

	/**
	 * Gets actual range.
	 *
	 * @return the actual range
	 */
	public int getActualRange() {
		return this.actualRange;
	}

	/**
	 * Gets actual power grade.
	 *
	 * @return the actual power grade
	 */
	public int getActualPowerGrade() {
		return this.actualPowerGrade;
	}
}
