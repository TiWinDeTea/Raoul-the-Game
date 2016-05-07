package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Created by maxime on 5/5/16.
 */
public class PlayerHUD extends Parent {
	private static final Vector2i mainPaneSize = new Vector2i(400, 100);
	private static final Vector2i playerPictureSize = new Vector2i(64, 64);
	private static final int healthRectanglesHeight = 20;
	private static final int manaRectanglesHeight = 15;
	private static final double manaRectanglesWidth = 0.75; //relative to healthRectangleWidth (auto computed)
	private static final int space = 20;

	private static final Color maxHealthRectangleColor = Color.DARKRED;
	private static final Color actualHealthRectangleColor = Color.RED;
	private static final Color maxManaRectangleColor = Color.DARKBLUE;
	private static final Color actualManaRectangleColor = Color.BLUE;
	private static final Color backGroundColor = Color.PURPLE;

	private static final Duration AnimationDuration = Duration.millis(1000);

	public static final Font healthLabelFont = Font.font("Serif", FontWeight.NORMAL, 15);
	public static final Color healthLabelTextColor = Color.WHITE;
	public static final int healthLabelTranslateY = 33;
	public static final Font manaLabelFont = Font.font("Serif", FontWeight.NORMAL, 10);
	public static final Color manaLabelTextColor = Color.WHITE;
	public static final int manaLabelTranslateY = 53;
	public static final int labelsOffset = 5;

	private final ImageView playerPicture;
	private final Rectangle maxHealthRectangle = new Rectangle();
	private final Rectangle actualHealthRectangle = new Rectangle();
	private final Rectangle maxManaRectangle = new Rectangle();
	private final Rectangle actualManaRectangle = new Rectangle();
	private final Rectangle backGroundRectangle = new Rectangle(mainPaneSize.x, mainPaneSize.y, backGroundColor);

	private final Label healthLabel = new Label();
	private final Label manaLabel = new Label();
	private final SimpleStringProperty healthString = new SimpleStringProperty();
	private final SimpleStringProperty manaString = new SimpleStringProperty();

	private int maxHealth;
	private int actualHealth;
	private int maxMana;
	private int actualMana;

	public PlayerHUD(ImageView playerPicture, int maxHealth, int actualHealth, int maxMana, int actualMana) {
		this.playerPicture = playerPicture;
		this.maxHealth = maxHealth;
		this.actualHealth = actualHealth;
		this.maxMana = maxMana;
		this.actualMana = actualMana;
		this.init();
	}

	private void init() {
		this.getChildren().add(this.backGroundRectangle);

		this.getChildren().add(this.playerPicture);
		double originalWidth = this.playerPicture.getViewport().getWidth();
		double originalHeight = this.playerPicture.getViewport().getHeight();
		this.playerPicture.setScaleX(playerPictureSize.x / this.playerPicture.getViewport().getWidth());
		this.playerPicture.setScaleY(playerPictureSize.y / this.playerPicture.getViewport().getHeight());
		this.playerPicture.setLayoutX(this.playerPicture.getLayoutX() - (originalWidth - playerPictureSize.x) / 2);
		this.playerPicture.setLayoutY(this.playerPicture.getLayoutY() - (originalHeight - playerPictureSize.y) / 2);
		this.playerPicture.setTranslateX(space);
		this.playerPicture.setTranslateY((mainPaneSize.y - playerPictureSize.y) / 2.0);

		this.getChildren().add(this.maxHealthRectangle);
		this.maxHealthRectangle.setTranslateX(2 * space + playerPictureSize.x);
		this.maxHealthRectangle.setTranslateY((mainPaneSize.y - healthRectanglesHeight - manaRectanglesHeight) / 2);
		this.maxHealthRectangle.setWidth(mainPaneSize.x - 3 * space - playerPictureSize.x);
		this.maxHealthRectangle.setHeight(healthRectanglesHeight);
		this.maxHealthRectangle.setFill(maxHealthRectangleColor);

		this.getChildren().add(this.actualHealthRectangle);
		this.actualHealthRectangle.setTranslateX(this.maxHealthRectangle.getTranslateX());
		this.actualHealthRectangle.setTranslateY(this.maxHealthRectangle.getTranslateY());
		this.actualHealthRectangle.setWidth((double) (this.actualHealth) / this.maxHealth * this.maxHealthRectangle.getWidth());
		this.actualHealthRectangle.setHeight(healthRectanglesHeight);
		this.actualHealthRectangle.setFill(actualHealthRectangleColor);

		this.getChildren().add(this.maxManaRectangle);
		this.maxManaRectangle.setTranslateX(2 * space + playerPictureSize.x);
		this.maxManaRectangle.setTranslateY((mainPaneSize.y - healthRectanglesHeight - manaRectanglesHeight) / 2 + healthRectanglesHeight);
		this.maxManaRectangle.setWidth(manaRectanglesWidth * this.maxHealthRectangle.getWidth());
		this.maxManaRectangle.setHeight(manaRectanglesHeight);
		this.maxManaRectangle.setFill(maxManaRectangleColor);

		this.getChildren().add(this.actualManaRectangle);
		this.actualManaRectangle.setTranslateX(this.maxManaRectangle.getTranslateX());
		this.actualManaRectangle.setTranslateY(this.maxManaRectangle.getTranslateY());
		this.actualManaRectangle.setWidth((double) (this.actualMana) / this.maxMana * this.maxManaRectangle.getWidth());
		this.actualManaRectangle.setHeight(manaRectanglesHeight);
		this.actualManaRectangle.setFill(actualManaRectangleColor);

		this.getChildren().add(this.healthLabel);
		this.healthLabel.setFont(healthLabelFont);
		this.healthLabel.setTextFill(healthLabelTextColor);
		this.healthLabel.textProperty().bind(this.healthString);
		this.healthString.set(this.maxHealth + " / " + this.actualHealth);
		this.healthLabel.setTranslateX(this.maxHealthRectangle.getTranslateX() + labelsOffset);
		this.healthLabel.setTranslateY(healthLabelTranslateY);

		this.getChildren().add(this.manaLabel);
		this.manaLabel.setFont(manaLabelFont);
		this.manaLabel.setTextFill(manaLabelTextColor);
		this.manaLabel.textProperty().bind(this.manaString);
		this.manaString.set(this.maxHealth + " / " + this.actualHealth);
		this.manaLabel.setTranslateX(this.maxHealthRectangle.getTranslateX() + labelsOffset);
		this.manaLabel.setTranslateY(manaLabelTranslateY);
	}

	public static Vector2i getSize() {
		return mainPaneSize;
	}

	private void updateHealth() {
		//this.actualHealthRectangle.setWidth((double) (this.actualHealth) / this.maxHealth * this.maxHealthRectangle.getWidth());
		RectangleSizeTransition transition = new RectangleSizeTransition(this.actualHealthRectangle, (double) (this.actualHealth) / this.maxHealth * this.maxHealthRectangle.getWidth(), AnimationDuration);
		this.healthString.set(this.actualHealth + " / " + this.maxHealth);
		transition.play();
	}

	private void updateMana() {
		//this.actualManaRectangle.setWidth((double) (this.actualMana) / this.maxMana * this.maxManaRectangle.getWidth());
		RectangleSizeTransition transition = new RectangleSizeTransition(this.actualManaRectangle, (double) (this.actualMana) / this.maxMana * this.maxManaRectangle.getWidth(), AnimationDuration);
		this.manaString.set(this.actualMana + " / " + this.maxMana);
		transition.play();
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		this.updateHealth();
	}

	public void setActualHealth(int actualHealth) {
		this.actualHealth = actualHealth;
		this.updateHealth();
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
		this.updateMana();
	}

	public void setActualMana(int actualMana) {
		this.actualMana = actualMana;
		this.updateMana();
	}

	public int getActualMana() {
		return this.actualMana;
	}

	public int getMaxHealth() {
		return this.maxHealth;
	}

	public int getActualHealth() {
		return this.actualHealth;
	}

	public int getMaxMana() {
		return this.maxMana;
	}
}
