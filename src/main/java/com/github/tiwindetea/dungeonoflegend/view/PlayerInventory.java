package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxime on 5/11/16.
 */
public class PlayerInventory extends Parent {
	private static final Vector2i MAIN_PANE_SIZE = new Vector2i(300, 400);
	private static final Vector2i PLAYER_PICTURE_SIZE = new Vector2i(32, 32);
	private static final Vector2i ITEM_PICTURE_SIZE = new Vector2i(64, 64);
	private static final int SPACE = 20;

	private static final Color BACKGROUND_COLOR = Color.PURPLE;

	private final Rectangle mainRectangle = new Rectangle(MAIN_PANE_SIZE.x, MAIN_PANE_SIZE.y, BACKGROUND_COLOR);

	private final ImageView playerPicture;

	private final List<InventoryItem> inventoryItems = new ArrayList<>();
	private final List<InventoryItem> equipedItems = new ArrayList<>();

	private final HBox equipedItemsHBox = new HBox();
	private final TilePane itemsTilePane = new TilePane();

	private final String playerName = "GILBERT";

	public PlayerInventory(ImageView playerPicture) {

		this.playerPicture = playerPicture;

		this.equipedItemsHBox.getChildren().add(new InventoryItem(StaticEntityType.BOW2, "BOW2"));
		this.equipedItemsHBox.getChildren().add(new InventoryItem(StaticEntityType.HELMET1, "HELMET1"));
		this.equipedItemsHBox.getChildren().add(new InventoryItem(StaticEntityType.BREAST_PLATE1, "BREAST_PLATE1"));
		this.equipedItemsHBox.getChildren().add(new InventoryItem(StaticEntityType.PANTS1, "PANTS1"));
		this.equipedItemsHBox.getChildren().add(new InventoryItem(StaticEntityType.BOOTS1, "BOOTS1"));
		this.equipedItemsHBox.getChildren().add(new InventoryItem(StaticEntityType.GLOVES1, "GLOVES1"));

		this.itemsTilePane.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.itemsTilePane.setMinWidth(this.mainRectangle.getWidth() - (2 * SPACE));
		this.itemsTilePane.setMaxWidth(this.mainRectangle.getWidth() - (2 * SPACE));
		for(int i = 0; i < 17; ++i) {
			this.itemsTilePane.getChildren().add(new InventoryItem(StaticEntityType.BOW2, "BOW2"));
		}

		HBox hBox = new HBox();
		hBox.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
		hBox.setPadding(new Insets(SPACE));
		hBox.getChildren().add(playerPicture);
		this.equipedItemsHBox.setPadding(new Insets(0, 0, 0, SPACE));
		this.equipedItemsHBox.setBackground(new Background(new BackgroundFill(Color.FUCHSIA, CornerRadii.EMPTY, Insets.EMPTY)));
		hBox.getChildren().add(this.equipedItemsHBox);
		VBox mainVbox = new VBox();
		mainVbox.getChildren().addAll(hBox);
		this.itemsTilePane.setPadding(new Insets(SPACE));
		this.itemsTilePane.setPrefWidth(Double.MAX_VALUE);
		this.itemsTilePane.maxWidthProperty().bind(mainVbox.widthProperty());
		mainVbox.getChildren().add(this.itemsTilePane);
		mainVbox.setBackground(new Background(new BackgroundFill(Color.SALMON, CornerRadii.EMPTY, Insets.EMPTY)));
		mainVbox.prefWidthProperty().bind(this.mainRectangle.widthProperty());

		getChildren().add(mainVbox);


		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.SECONDARY) {

					PlayerInventory.this.itemsTilePane.getChildren().add(new InventoryItem(StaticEntityType.BOW2, "BOW2"));
				}
				else {

					PlayerInventory.this.itemsTilePane.getChildren().clear();
				}
			}
		});

	}
}
