package com.github.tiwindetea.dungeonoflegend.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by maxime on 5/17/16.
 */
public class InformationsDisplayer extends Parent {
	private static final int WIDTH = 300;
	public static final Font TEXT_FONT = Font.font("Arial", FontWeight.NORMAL, 20);
	public static final Color TEXT_COLOR = Color.WHITE;

	private static final StackPane MAIN_PANE = new StackPane();
	private static final Label MAIN_LABEL = new Label();

	private static InformationsDisplayer ourInstance = new InformationsDisplayer();

	public static InformationsDisplayer getInstance() {
		return ourInstance;
	}

	private InformationsDisplayer() {
		MAIN_PANE.setMinWidth(WIDTH);
		MAIN_PANE.setMaxWidth(WIDTH);
		MAIN_PANE.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		getChildren().add(MAIN_PANE);
		MAIN_LABEL.prefWidthProperty().bind(MAIN_PANE.widthProperty());
		MAIN_LABEL.setFont(TEXT_FONT);
		MAIN_LABEL.setTextFill(TEXT_COLOR);
		MAIN_LABEL.setWrapText(true);
		MAIN_PANE.getChildren().add(MAIN_LABEL);
		//MAIN_PANE.prefHeightProperty().bind(MAIN_LABEL.heightProperty());

		//TODO: Real information displayer
	}

	public static void setText(String text) {
		MAIN_LABEL.setText(text);
		MAIN_PANE.setPrefWidth(MAIN_LABEL.getWidth());
	}

	public static void clear() {
		MAIN_LABEL.setText("");
	}
}
