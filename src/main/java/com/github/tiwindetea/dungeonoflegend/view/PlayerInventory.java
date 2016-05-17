package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntity;
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

import java.util.HashMap;

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

	private final HashMap<Long, StaticEntity> inventoryItems = new HashMap<>();
	private final HashMap<Long, StaticEntity> equipedItems = new HashMap<>();

	private final VBox mainVBox = new VBox();
	private final HBox topHBox = new HBox();
	private final HBox equipedItemsHBox = new HBox();
	private final TilePane inventoryItemsTilePane = new TilePane();

	public PlayerInventory(ImageView playerPicture) {

		this.playerPicture = playerPicture;

		getChildren().add(this.mainVBox);
		this.mainVBox.setBackground(new Background(new BackgroundFill(Color.SALMON, CornerRadii.EMPTY, Insets.EMPTY)));
		this.mainVBox.prefWidthProperty().bind(this.mainRectangle.widthProperty());

		this.mainVBox.getChildren().add(this.topHBox);
		this.topHBox.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.topHBox.setPadding(new Insets(SPACE));
		this.topHBox.getChildren().add(playerPicture);

		this.topHBox.getChildren().add(this.equipedItemsHBox);
		this.equipedItemsHBox.setBackground(new Background(new BackgroundFill(Color.FUCHSIA, CornerRadii.EMPTY, Insets.EMPTY)));
		this.equipedItemsHBox.setPadding(new Insets(0, 0, 0, SPACE));

		this.mainVBox.getChildren().add(this.inventoryItemsTilePane);
		this.inventoryItemsTilePane.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.inventoryItemsTilePane.setMaxWidth(this.mainRectangle.getWidth() - (2 * SPACE));
		this.inventoryItemsTilePane.setPrefWidth(Double.MAX_VALUE);
		this.inventoryItemsTilePane.setPadding(new Insets(SPACE));
		this.inventoryItemsTilePane.maxWidthProperty().bind(this.mainVBox.widthProperty());


		//For test
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.SECONDARY) {

					PlayerInventory.this.inventoryItemsTilePane.getChildren().add(new StaticEntity(StaticEntityType.SUPER_POT, new Vector2i(), "SUPER_POT"));
				}
				else {

					PlayerInventory.this.inventoryItemsTilePane.getChildren().clear();
				}
			}
		});

	}

	public void addInventoryItem(Long entityId, StaticEntity staticEntity) {
		this.inventoryItems.put(entityId, staticEntity);
		this.inventoryItemsTilePane.getChildren().add(staticEntity);
	}

	public void removeInventoryItem(Long entityId) {
		this.inventoryItemsTilePane.getChildren().remove(this.inventoryItems.get(entityId));
		this.inventoryItems.remove(entityId);
	}

	public void addEquipedItem(Long entityId, StaticEntity staticEntity) {
		this.equipedItems.put(entityId, staticEntity);
		this.equipedItemsHBox.getChildren().add(staticEntity);
	}

	public void removeEquipedItem(Long entityId) {
		this.equipedItemsHBox.getChildren().remove(this.equipedItems.get(entityId));
		this.equipedItems.remove(entityId);
	}
}
