package com.github.tiwindetea.dungeonoflegend.events.playerinventory;

/**
 * Created by maxime on 5/18/16.
 */
public class ObjectClickEvent extends PlayerInventoryEvent {
	public Long objectId;

	public ObjectClickEvent(Long objectId) {
		this.objectId = objectId;
	}
}
