package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.model.events.EntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.EntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.EntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.view.listeners.EntityListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxime on 5/2/16.
 */
public class GUI implements EntityListener {
	private Map<Integer, Entity> entityMap = new HashMap<>();
	private final BorderPane borderPane = new BorderPane();
	private final Scene scene = new Scene(this.borderPane);
	private final Pane rPane = new Pane();

	private final Pane bPane = new Pane();
	private final SplitPane bSplitPane = new SplitPane();

	private final Pane cPane = new Pane();

	public GUI() {
		this.init();
	}

	private void init() {


		this.rPane.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
		this.rPane.setMaxWidth(300);
		//rPane.setMaxHeight(300);
		this.rPane.setMinHeight(100);
		this.rPane.setMinWidth(100);
		this.rPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		Rectangle rect = new Rectangle(50, 50, Color.BLACK);
		this.rPane.getChildren().add(rect);
		//this.rGroup.getChildren().add(rPane);
		this.borderPane.setRight(this.rPane);
		//this.borderPane.setRight(this.rGroup);
		this.bPane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
		//bPane.setMinHeight(300);
		this.borderPane.setBottom(this.bPane);
		this.cPane.setBackground(new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.borderPane.setCenter(this.cPane);
		this.borderPane.setMinWidth(500);
		this.borderPane.setMinHeight(500);
		//center: map -> caneva, TilePane ?
		//right: inventory
		//bottom: life and mana
		SplitPane splitPane = new SplitPane();
		splitPane.setOrientation(Orientation.HORIZONTAL);

		PlayerHUD player1HUD = new PlayerHUD(new Entity(EntityType.PLAYER1, new Vector2i()).getImageView(), 100, 70, 100, 30);
		//bPane.getChildren().add(player1HUD.getMainGroup());
		PlayerHUD player2HUD = new PlayerHUD(new Entity(EntityType.PLAYER2, new Vector2i()).getImageView(), 100, 70, 100, 30);
		//bPane.getChildren().add(player2HUD.getMainGroup());
		splitPane.getItems().addAll(player1HUD.getMainGroup(), player2HUD.getMainGroup());
		this.bPane.getChildren().add(splitPane);

		this.bPane.widthProperty().addListener(e -> {
			if((this.bPane.getWidth() - this.rPane.getWidth()) < (2 * PlayerHUD.getSize().x)) {
				splitPane.setOrientation(Orientation.VERTICAL);
				player1HUD.setActualHealth(10);
				player1HUD.setActualMana(10);

			}
			else {
				splitPane.setOrientation(Orientation.HORIZONTAL);
				player1HUD.setActualHealth(100);
				player1HUD.setActualMana(100);
			}
		});
	}

	public Scene getScene() {
		return this.scene;
	}

	@Override
	public void moveEntity(EntityMoveEvent e) {
		this.entityMap.get(e.entityId).setPosition(e.newPosition);
	}

	@Override
	public void createEntity(EntityCreationEvent e) {
		Entity newEntity = new Entity(e.type, e.position);
		this.entityMap.put(e.entityId, newEntity);
		//this.root.getChildren().add(newEntity.getImageView());
	}

	@Override
	public void deleteEntity(EntityDeletionEvent e) {
		//this.root.getChildren().remove(this.entityMap.get(e.entityId).getImageView());
		this.entityMap.remove(e.entityId);
	}

}
