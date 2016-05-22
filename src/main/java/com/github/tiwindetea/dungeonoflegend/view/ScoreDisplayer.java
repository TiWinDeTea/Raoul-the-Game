package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntity;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The type ScoreDisplayer.
 *
 * @author Maxime PINARD
 */
public class ScoreDisplayer extends Parent {
	private static final Vector2i MAIN_PANE_SIZE = new Vector2i(300, 60);
	private static final int PADDING = 10;
	private static final Font LABEL_FONT = Font.font("Serif", FontWeight.NORMAL, 15);
	private static final Color LABEL_TEXT_COLOR = Color.WHITE;

	private final Rectangle mainRectangle = new Rectangle(MAIN_PANE_SIZE.x, MAIN_PANE_SIZE.y, Color.rgb(0x2E, 0x26, 0x25));
	private final StaticEntity staticEntity = new StaticEntity(StaticEntityType.LIT_BULB, "");
	private final Label label = new Label("0");
	private final HBox hBox = new HBox();
	private int score = 0;

	/**
	 * Instantiates a new ScoreDisplayer.
	 */
	public ScoreDisplayer() {
		getChildren().add(this.mainRectangle);

		getChildren().add(this.hBox);
		this.hBox.prefWidthProperty().bind(this.mainRectangle.widthProperty());
		this.hBox.prefHeightProperty().bind(this.mainRectangle.heightProperty());
		this.hBox.setAlignment(Pos.CENTER);
		this.hBox.setPadding(new Insets(PADDING));

		this.hBox.getChildren().add(this.staticEntity);

		this.hBox.getChildren().add(this.label);
		this.label.setPadding(new Insets(0, 0, 0, PADDING));
		this.label.setFont(LABEL_FONT);
		this.label.setTextFill(LABEL_TEXT_COLOR);
	}

	/**
	 * Increase score.
	 */
	public void increaseScore() {
		++this.score;
		updateScore();
	}

	/**
	 * Decrease score.
	 */
	public void decreaseScore() {
		--this.score;
		updateScore();
	}

	/**
	 * Sets score.
	 *
	 * @param score the score
	 */
	public void setScore(int score) {
		this.score = score;
		updateScore();
	}

	private void updateScore() {
		this.label.setText(Integer.toString(this.score));
	}
}
