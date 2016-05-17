package com.github.tiwindetea.dungeonoflegend.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by maxime on 5/17/16.
 */
public class InformationsDisplayer extends Parent {
	private static InformationsDisplayer ourInstance = new InformationsDisplayer();

	private static final int WIDTH = 300;

	public static InformationsDisplayer getInstance() {
		return ourInstance;
	}

	private InformationsDisplayer() {
		Pane pane = new Pane();
		pane.setMinWidth(WIDTH);
		pane.setMaxWidth(WIDTH);
		pane.setMinHeight(50);
		pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		getChildren().add(pane);
		getChildren().add(new Label("//TODO"));
		//TODO: Real information displayer
	}
}
