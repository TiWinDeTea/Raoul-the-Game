package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.events.EntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.EntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.EntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.view.listeners.EntityListener;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxime on 5/2/16.
 */
public class GUI implements EntityListener {
	private Map<Integer, Entity> entityMap = new HashMap<>();
	private final Group root;

	public GUI(Group root) {
		this.root = root;
		init();
	}

	private void init() {
		BorderPane borderPane = new BorderPane();
		//center: map -> caneva, TilePane ?
		//right: inventory
		//bottom: life and mana
	}

	@Override
	public void moveEntity(EntityMoveEvent e) {
		this.entityMap.get(e.entityId).setPosition(e.newPosition);
	}

	@Override
	public void createEntity(EntityCreationEvent e) {
		Entity newEntity = new Entity(e.type, e.position);
		this.entityMap.put(e.entityId, newEntity);
		this.root.getChildren().add(newEntity.getImageView());
	}

	@Override
	public void deleteEntity(EntityDeletionEvent e) {
		this.root.getChildren().remove(this.entityMap.get(e.entityId).getImageView());
		this.entityMap.remove(e.entityId);
	}

}
