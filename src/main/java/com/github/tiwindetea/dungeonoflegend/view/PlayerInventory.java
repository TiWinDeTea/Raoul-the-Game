//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.events.playerinventory.ObjectClickEvent;
import com.github.tiwindetea.dungeonoflegend.events.playerinventory.ObjectDragEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.playerinventory.PlayerInventoryListener;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntity;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type PlayerInventory.
 *
 * @author Maxime PINARD
 */
public class PlayerInventory extends Parent {

	public enum Place {
		EQUIPPED_ITEMS,
		UNEQUIPPED_ITEMS,
		NOWHERE
	}

	private final List<PlayerInventoryListener> listeners = new ArrayList<>();

	private static final Vector2i MAIN_PANE_SIZE = new Vector2i(300, 400);
	private static final Vector2i PLAYER_PICTURE_SIZE = new Vector2i(32, 32);
	private static final Vector2i ITEM_PICTURE_SIZE = new Vector2i(64, 64);
	private static final int SPACE = 20;

	private static final Color BACKGROUND_COLOR = Color.rgb(53, 53, 53);
	private static final Color IVENTORY_ITEMS_BACKGROUND_COLOR = Color.rgb(53, 53, 53);


	private final Rectangle mainRectangle = new Rectangle(MAIN_PANE_SIZE.x, MAIN_PANE_SIZE.y, BACKGROUND_COLOR);
	private final ImageView backgroundImage = new ImageView(ViewPackage.INVENTORY_IMAGE);

	private final ImageView playerPicture;

	private final HashMap<Long, StaticEntity> inventoryItems = new HashMap<>();
	private final HashMap<Long, StaticEntity> equipedItems = new HashMap<>();

	private final VBox mainVBox = new VBox();
	private final HBox topHBox = new HBox();
	private final HBox equipedItemsHBox = new HBox();
	private final TilePane inventoryItemsTilePane = new TilePane();

	private EventHandler<MouseEvent> onMouseReleaseEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {

			// right mouse button
			if(event.getButton() == MouseButton.SECONDARY) {
				StaticEntity staticEntity = (StaticEntity) event.getSource();
				if(PlayerInventory.this.inventoryItems.containsValue(staticEntity)) {
					for(Map.Entry<Long, StaticEntity> entry : PlayerInventory.this.inventoryItems.entrySet()) {
						if(entry.getValue().equals(staticEntity)) {
							fireObjectClickEvent(new ObjectClickEvent(entry.getKey()));
						}
					}
				}
				if(PlayerInventory.this.equipedItems.containsValue(staticEntity)) {
					for(Map.Entry<Long, StaticEntity> entry : PlayerInventory.this.equipedItems.entrySet()) {
						if(entry.getValue().equals(staticEntity)) {
							fireObjectClickEvent(new ObjectClickEvent(entry.getKey()));
						}
					}
				}
			}
		}
	};

	private EventHandler<DragEvent> onDragOverEquipementPanelEventHandler = new EventHandler<DragEvent>() {
		@Override
		public void handle(DragEvent event) {
			Dragboard db = event.getDragboard();
			if (db.hasString() && db.hasContent(StaticEntity.place) && db.getContent(StaticEntity.place) != Place.EQUIPPED_ITEMS) {
				event.acceptTransferModes(TransferMode.COPY);
			}
			event.consume();
		}
	};

	private EventHandler<DragEvent> onDragDroppedEquipementPanelEventHandler = new EventHandler<DragEvent>() {
		@Override
		public void handle(DragEvent event) {
			boolean success = false;
			Dragboard db = event.getDragboard();
			if (db.hasString() && db.hasContent(StaticEntity.place)) {
				try {
					fireObjectDragEvent(new ObjectDragEvent(Long.parseLong(db.getString())));
					success = true;
				} catch (Exception e) {
					System.out.println("Cannot parse " + db.getString() + " to a long");
				}
			}
			event.setDropCompleted(success);
			event.consume();
		}
	};

	private EventHandler<DragEvent> onDragOverInventoryPanelEventHandler = new EventHandler<DragEvent>() {
		@Override
		public void handle(DragEvent event) {
			Dragboard db = event.getDragboard();
			if (db.hasString() && db.hasContent(StaticEntity.place) && db.getContent(StaticEntity.place) != Place.UNEQUIPPED_ITEMS) {
				event.acceptTransferModes(TransferMode.COPY);
			}
			event.consume();
		}
	};

	private EventHandler<DragEvent> onDragDroppedInventoryPanelEventHandler = this.onDragDroppedEquipementPanelEventHandler;


	/**
	 * Instantiates a new PlayerInventory.
	 *
	 * @param playerPicture the player picture
	 */
	public PlayerInventory(ImageView playerPicture) {

		this.playerPicture = playerPicture;

		getChildren().add(this.backgroundImage);

		getChildren().add(this.mainVBox);
		this.mainVBox.prefWidthProperty().bind(this.mainRectangle.widthProperty());

		this.mainVBox.getChildren().add(this.topHBox);
		this.topHBox.setPadding(new Insets(SPACE));
		this.topHBox.getChildren().add(playerPicture);

		this.topHBox.getChildren().add(this.equipedItemsHBox);
		this.equipedItemsHBox.setPadding(new Insets(0, 0, 0, SPACE));

		this.mainVBox.getChildren().add(this.inventoryItemsTilePane);
		this.inventoryItemsTilePane.setBackground(new Background(new BackgroundFill(IVENTORY_ITEMS_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		this.inventoryItemsTilePane.setMaxWidth(this.mainRectangle.getWidth() - (2 * SPACE));
		this.inventoryItemsTilePane.setPrefWidth(Double.MAX_VALUE);
		this.inventoryItemsTilePane.setPadding(new Insets(SPACE));
		this.inventoryItemsTilePane.maxWidthProperty().bind(this.mainVBox.widthProperty());

		this.topHBox.setOnDragDropped(this.onDragDroppedEquipementPanelEventHandler);
		this.topHBox.setOnDragOver(this.onDragOverEquipementPanelEventHandler);

		this.inventoryItemsTilePane.setOnDragDropped(this.onDragDroppedInventoryPanelEventHandler);
		this.inventoryItemsTilePane.setOnDragOver(this.onDragOverInventoryPanelEventHandler);
	}

	/**
	 * Add a PlayerInventoryListener.
	 *
	 * @param listener the listener
	 */
	public void addPlayerInventoryListener(PlayerInventoryListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Get a copy of the list of PlayerInventoryListener as an array.
	 *
	 * @return the PlayerInventoryListener array
	 */
	public PlayerInventoryListener[] getPlayerInventoryListener() {
		return this.listeners.toArray(new PlayerInventoryListener[this.listeners.size()]);
	}

	private void fireObjectClickEvent(ObjectClickEvent event) {
		for(PlayerInventoryListener listener : this.getPlayerInventoryListener()) {
			listener.objectClicked(event);
		}
	}

	private void fireObjectDragEvent(ObjectDragEvent event) {
		for (PlayerInventoryListener listener : getPlayerInventoryListener()) {
			listener.objectDragged(event);
		}
	}

	/**
	 * Add a inventory item (a StaticEntity to the inventory).
	 *
	 * @param entityId     the entity id
	 * @param staticEntity the static entity
	 */
	public void addInventoryItem(Long entityId, StaticEntity staticEntity) {
		this.inventoryItems.put(entityId, staticEntity);
		this.inventoryItemsTilePane.getChildren().add(staticEntity);
		staticEntity.setOnMouseReleased(this.onMouseReleaseEventHandler);
		staticEntity.setWhere(Place.UNEQUIPPED_ITEMS);
	}

	/**
	 * Remove a inventory item.
	 *
	 * @param entityId the entity id
	 */
	public void removeInventoryItem(Long entityId) {
		StaticEntity a = this.inventoryItems.get(entityId);
		a.setWhere(Place.NOWHERE);
		this.inventoryItemsTilePane.getChildren().remove(a);
		this.inventoryItems.remove(entityId);
	}

	/**
	 * Add a staticEntity as equiped item.
	 *
	 * @param entityId     the entity id
	 * @param staticEntity the static entity
	 */
	public void addEquipedItem(Long entityId, StaticEntity staticEntity) {
		this.equipedItems.put(entityId, staticEntity);
		this.equipedItemsHBox.getChildren().add(staticEntity);
		staticEntity.setWhere(Place.EQUIPPED_ITEMS);
		staticEntity.setOnMouseReleased(this.onMouseReleaseEventHandler);
	}

	/**
	 * Remove a equiped item.
	 *
	 * @param entityId the entity id
	 */
	public void removeEquipedItem(Long entityId) {
		StaticEntity a = this.equipedItems.get(entityId);
		a.setWhere(Place.NOWHERE);
		this.equipedItemsHBox.getChildren().remove(a);
		this.equipedItems.remove(entityId);
	}

	/**
	 * Remove an item, equiped or not.
	 *
	 * @param entityId the entity id
	 */
	public void removeItem(Long entityId) {
		if(this.equipedItems.containsKey(entityId)) {
			removeEquipedItem(entityId);
		}
		else {
			removeInventoryItem(entityId);
		}
	}
}
