//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.view;

import com.github.tiwindetea.raoulthegame.model.Pair;
import com.github.tiwindetea.raoulthegame.model.Tile;
import com.github.tiwindetea.raoulthegame.model.Vector2i;
import com.github.tiwindetea.raoulthegame.view.entities.StaticEntity;
import com.github.tiwindetea.raoulthegame.view.entities.StaticEntityType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * The type ScoreDisplayer.
 *
 * @author Maxime PINARD
 */
public class ScoreDisplayer extends Parent {
	private static final Vector2i MAIN_PANE_SIZE = new Vector2i(300, 60);
	private static final int PADDING = 10;
	private static final int SPACING = 50;
	private static final Font LABEL_FONT = ViewPackage.getMainFont(20);
	private static final Color LABEL_TEXT_COLOR = Color.WHITE;

	private final Rectangle mainRectangle = new Rectangle(MAIN_PANE_SIZE.x, MAIN_PANE_SIZE.y, Color.rgb(0x2E, 0x26, 0x25));
	private final StaticEntity scoreStaticEntity = new StaticEntity(StaticEntityType.LIT_BULB, "", Pair.ERROR_VAL);
	private final ImageView levelImageView = new ImageView(ViewPackage.OBJECTS_IMAGE);
	private final Label scoreLabel = new Label("0");
	private final Label levelLabel = new Label("0");
	private final StackPane stackPane = new StackPane();
	private final HBox hBox = new HBox();
	private int score = 0;
	private int level = 0;

	/**
	 * Instantiates a new ScoreDisplayer.
	 */
	public ScoreDisplayer() {
		getChildren().add(this.mainRectangle);

		getChildren().add(this.stackPane);
		this.stackPane.prefWidthProperty().bind(this.mainRectangle.widthProperty());
		this.stackPane.prefHeightProperty().bind(this.mainRectangle.heightProperty());

		this.stackPane.getChildren().addAll(this.hBox);
		this.hBox.maxWidthProperty().bind(this.scoreLabel.widthProperty().add(this.levelLabel.widthProperty()).add(2 * ViewPackage.SPRITES_SIZE.x + 4 * PADDING));
		this.hBox.setMaxHeight(ViewPackage.SPRITES_SIZE.y);
		this.hBox.setAlignment(Pos.CENTER);
		this.hBox.setSpacing(PADDING);

		this.hBox.getChildren().add(this.scoreStaticEntity);

		this.hBox.getChildren().add(this.scoreLabel);
		this.scoreLabel.setPadding(new Insets(0, SPACING, 0, 0));
		this.scoreLabel.setFont(LABEL_FONT);
		this.scoreLabel.setTextFill(LABEL_TEXT_COLOR);

		this.hBox.getChildren().add(this.levelImageView);
		this.levelImageView.setViewport(new Rectangle2D(Tile.STAIR_DOWN.getSpritePosition(0).x * ViewPackage.SPRITES_SIZE.x, Tile.STAIR_DOWN.getSpritePosition(0).y * ViewPackage.SPRITES_SIZE.y, ViewPackage.SPRITES_SIZE.x, ViewPackage.SPRITES_SIZE.y));

		this.hBox.getChildren().add(this.levelLabel);
		this.levelLabel.setFont(LABEL_FONT);
		this.levelLabel.setTextFill(LABEL_TEXT_COLOR);
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
	 * Increase level.
	 */
	public void increaseLevel() {
		++this.level;
		updateLevel();
	}

	/**
	 * Decrease level.
	 */
	public void decreaseLevel() {
		--this.level;
		updateLevel();
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

	/**
	 * Sets level.
	 *
	 * @param level the level
	 */
	public void setLevel(int level) {
		this.level = level;
		updateLevel();
	}

	private void updateScore() {
		this.scoreLabel.setText(Integer.toString(this.score));
	}

	private void updateLevel() {
		this.levelLabel.setText(Integer.toString(this.level));
	}
}
