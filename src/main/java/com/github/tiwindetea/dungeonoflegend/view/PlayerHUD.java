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
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Created by maxime on 5/5/16.
 */
public class PlayerHUD extends Parent {
	private static final Vector2i MAIN_PANE_SIZE = new Vector2i(400, 100);
	private static final Vector2i PLAYER_PICTURE_SIZE = new Vector2i(64, 64);
	private static final int HEALTH_RECTANGLES_HEIGHT = 20;
	private static final int MANA_RECTANGLES_HEIGHT = 15;
	private static final double MANA_RECTANGLES_WIDTH = 0.75; //relative to healthRectangleWidth (auto computed)
	private static final int SPACE = 20;

	private static final Color MASK_COLOR = Color.rgb(0, 0, 0, 0.5);
	private static final Color MAX_HEALTH_RECTANGLE_COLOR = Color.DARKRED;
	private static final Color ACTUAL_HEALTH_RECTANGLE_COLOR = Color.RED;
	private static final Color MAX_MANA_RECTANGLE_COLOR = Color.DARKBLUE;
	private static final Color ACTUAL_MANA_RECTANGLE_COLOR = Color.BLUE;
	private static final Color BACKGROUND_COLOR = Color.PURPLE;

	private static final Duration ANIMATION_DURATION = Duration.millis(1000);

	public static final Font HEALTH_LABEL_FONT = Font.font("Serif", FontWeight.NORMAL, 15);
	public static final Color HEALTH_LABEL_TEXT_COLOR = Color.WHITE;
	public static final int HEALTH_LABEL_TRANSLATE_Y = 33;
	public static final Font MANA_LABEL_FONT = Font.font("Serif", FontWeight.NORMAL, 10);
	public static final Color MANA_LABEL_TEXT_COLOR = Color.WHITE;
	public static final int MANA_LABEL_TRANSLATE_Y = 53;
	public static final int LABELS_OFFSET = 5;

	private final ImageView playerPicture;
	private final Rectangle maxHealthRectangle = new Rectangle();
	private final Rectangle actualHealthRectangle = new Rectangle();
	private final Rectangle maxManaRectangle = new Rectangle();
	private final Rectangle actualManaRectangle = new Rectangle();
	private final Rectangle backGroundRectangle = new Rectangle(MAIN_PANE_SIZE.x, MAIN_PANE_SIZE.y, BACKGROUND_COLOR);
	private final Rectangle maskRectangle = new Rectangle(MAIN_PANE_SIZE.x, MAIN_PANE_SIZE.y, BACKGROUND_COLOR);

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
		this.playerPicture.setScaleX(PLAYER_PICTURE_SIZE.x / this.playerPicture.getViewport().getWidth());
		this.playerPicture.setScaleY(PLAYER_PICTURE_SIZE.y / this.playerPicture.getViewport().getHeight());
		this.playerPicture.setLayoutX(this.playerPicture.getLayoutX() - (originalWidth - PLAYER_PICTURE_SIZE.x) / 2);
		this.playerPicture.setLayoutY(this.playerPicture.getLayoutY() - (originalHeight - PLAYER_PICTURE_SIZE.y) / 2);
		this.playerPicture.setTranslateX(SPACE);
		this.playerPicture.setTranslateY((MAIN_PANE_SIZE.y - PLAYER_PICTURE_SIZE.y) / 2.0);

		this.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.setText("TODO");
				//TODO: display real informations
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
		this.maxHealthRectangle.setTranslateY((MAIN_PANE_SIZE.y - HEALTH_RECTANGLES_HEIGHT - MANA_RECTANGLES_HEIGHT) / 2);
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
		this.maxManaRectangle.setTranslateY((MAIN_PANE_SIZE.y - HEALTH_RECTANGLES_HEIGHT - MANA_RECTANGLES_HEIGHT) / 2 + HEALTH_RECTANGLES_HEIGHT);
		this.maxManaRectangle.setWidth(MANA_RECTANGLES_WIDTH * this.maxHealthRectangle.getWidth());
		this.maxManaRectangle.setHeight(MANA_RECTANGLES_HEIGHT);
		this.maxManaRectangle.setFill(MAX_MANA_RECTANGLE_COLOR);

		this.getChildren().add(this.actualManaRectangle);
		this.actualManaRectangle.setTranslateX(this.maxManaRectangle.getTranslateX());
		this.actualManaRectangle.setTranslateY(this.maxManaRectangle.getTranslateY());
		this.actualManaRectangle.setWidth((double) (this.actualMana) / this.maxMana * this.maxManaRectangle.getWidth());
		this.actualManaRectangle.setHeight(MANA_RECTANGLES_HEIGHT);
		this.actualManaRectangle.setFill(ACTUAL_MANA_RECTANGLE_COLOR);

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

		getChildren().add(this.maskRectangle);
		this.maskRectangle.setFill(MASK_COLOR);
		this.maskRectangle.setVisible(false);
	}

	public static Vector2i getSize() {
		return MAIN_PANE_SIZE;
	}

	private void updateHealth() {
		//this.actualHealthRectangle.setWidth((double) (this.actualHealth) / this.maxHealth * this.maxHealthRectangle.getWidth());
		RectangleSizeTransition transition = new RectangleSizeTransition(this.actualHealthRectangle, (double) (this.actualHealth) / this.maxHealth * this.maxHealthRectangle.getWidth(), ANIMATION_DURATION);
		this.healthString.set(this.actualHealth + " / " + this.maxHealth);
		transition.play();
	}

	private void updateMana() {
		//this.actualManaRectangle.setWidth((double) (this.actualMana) / this.maxMana * this.maxManaRectangle.getWidth());
		RectangleSizeTransition transition = new RectangleSizeTransition(this.actualManaRectangle, (double) (this.actualMana) / this.maxMana * this.maxManaRectangle.getWidth(), ANIMATION_DURATION);
		this.manaString.set(this.actualMana + " / " + this.maxMana);
		transition.play();
	}

	public void setMasked(boolean masked) {
		this.maskRectangle.setVisible(masked);
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
