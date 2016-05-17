package com.github.tiwindetea.dungeonoflegend.view;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by maxime on 5/13/16.
 */
public class ScoreDisplayer extends Parent {
	public ScoreDisplayer() {
		Rectangle rect = new Rectangle(300, 100, Color.CADETBLUE);
		getChildren().add(rect);
		//TODO: Real score displayer
	}
}
