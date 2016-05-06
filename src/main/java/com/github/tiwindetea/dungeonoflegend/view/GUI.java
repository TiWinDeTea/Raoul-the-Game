package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.model.events.EntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.EntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.EntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntity;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;
import com.github.tiwindetea.dungeonoflegend.view.listeners.EntityListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxime on 5/2/16.
 */
public class GUI implements EntityListener {
	private Map<Integer, StaticEntity> entityMap = new HashMap<>();
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
		this.rPane.setMinHeight(100);
		this.rPane.setMinWidth(300);
		//this.rPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		Rectangle rect = new Rectangle(50, 50, Color.BLACK);
		this.rPane.getChildren().add(rect);
		this.borderPane.setRight(this.rPane);
		this.bPane.setBackground(new Background(new BackgroundFill(Color.PURPLE, CornerRadii.EMPTY, Insets.EMPTY)));
		this.borderPane.setBottom(this.bPane);
		this.cPane.setBackground(new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.borderPane.setCenter(this.cPane);
		this.borderPane.setMinWidth(500);
		this.borderPane.setMinHeight(500);
		//center: map -> caneva, TilePane ?
		//right: inventory
		//bottom: life and mana

		PlayerHUD player1HUD = new PlayerHUD(new StaticEntity(StaticEntityType.PLAYER1, new Vector2i()).getImageView(), 100, 70, 100, 30);
		PlayerHUD player2HUD = new PlayerHUD(new StaticEntity(StaticEntityType.PLAYER2, new Vector2i()).getImageView(), 100, 70, 100, 30);

		TilePane tilePane = new TilePane();
		tilePane.getChildren().addAll(player1HUD.getMainGroup(), player2HUD.getMainGroup());

		Pane miniMapPain = new Pane();
		miniMapPain.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		miniMapPain.setMinWidth(300);
		miniMapPain.setMaxWidth(300);

		HBox hBox = new HBox();
		hBox.getChildren().addAll(tilePane, miniMapPain);
		hBox.setMinWidth(2 * PlayerHUD.getSize().x + miniMapPain.getMinWidth());
		this.bPane.getChildren().add(hBox);

		this.bPane.widthProperty().addListener(e -> {
			if((this.bPane.getWidth() - this.rPane.getWidth()) < (2 * PlayerHUD.getSize().x)) {
				player1HUD.setActualHealth(10);
				player1HUD.setActualMana(10);
				player2HUD.setActualHealth(100);
				player2HUD.setActualMana(100);
				this.bPane.setMinHeight(2 * PlayerHUD.getSize().y);
			}
			else {
				player1HUD.setActualHealth(100);
				player1HUD.setActualMana(100);
				player2HUD.setActualHealth(10);
				player2HUD.setActualMana(10);
				this.bPane.setMinHeight(PlayerHUD.getSize().y);
			}
			tilePane.setMaxWidth(this.bPane.getWidth() - this.rPane.getWidth());
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
		StaticEntity newStaticEntity = new StaticEntity(e.type, e.position);
		this.entityMap.put(e.entityId, newStaticEntity);
		//this.root.getChildren().add(newStaticEntity.getImageView());
	}

	@Override
	public void deleteEntity(EntityDeletionEvent e) {
		//this.root.getChildren().remove(this.entityMap.get(e.entityId).getImageView());
		this.entityMap.remove(e.entityId);
	}

}
