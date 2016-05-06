package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.MapCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerStatEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryAdditionEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.InteractionRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.DropRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.UsageRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.moves.ComplexMoveRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.moves.SimpleMoveRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.game.GameListener;
import com.github.tiwindetea.dungeonoflegend.listeners.request.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxime on 4/24/16.
 */
public class Game implements RequestListener {
	private final List<GameListener> listeners = new ArrayList<>();

	public void addGameListener(GameListener listener) {
		this.listeners.add(listener);
	}

	public GameListener[] getGameListeners() {
		return this.listeners.toArray(new GameListener[this.listeners.size()]);
	}

	private void fireLivingEntityCreationEvent(LivingEntityCreationEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.createLivingEntity(event);
		}
	}

	private void fireLivingEntityDeletionEvent(LivingEntityDeletionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.deleteLivingEntity(event);
		}
	}

	private void fireLivingEntityLOSDefinitionEvent(LivingEntityLOSDefinitionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.defineLivingEntityLOS(event);
		}
	}

	private void fireLivingEntityLOSModificationEvent(LivingEntityLOSModificationEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.modifieLivingEntityLOS(event);
		}
	}

	private void fireLivingEntityMoveEvent(LivingEntityMoveEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.moveLivingEntity(event);
		}
	}

	private void fireMapCreationEvent(MapCreationEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.createMap(event);
		}
	}

	private void fireInventoryAdditionEvent(InventoryAdditionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.addInventory(event);
		}
	}

	private void fireInventoryDeletionEvent(InventoryDeletionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.deleteInventory(event);
		}
	}

	private void firePlayerCreationEvent(PlayerCreationEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.createPlayer(event);
		}
	}

	private void firePlayerStatEvent(PlayerStatEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.changePlayerStat(event);
		}
	}

	private void fireStaticEntityCreationEvent(StaticEntityCreationEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.createStaticEntity(event);
		}
	}

	private void fireStaticEntityDeletionEvent(StaticEntityDeletionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.deleteStaticEntity(event);
		}
	}

	private void fireStaticEntityLOSDefinitionEvent(StaticEntityLOSDefinitionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.defineStaticEntityLOS(event);
		}
	}

	@Override
	public void requestComplexMove(ComplexMoveRequestEvent e) {

	}

	@Override
	public void requestDrop(DropRequestEvent e) {

	}

	@Override
	public void requestInteraction(InteractionRequestEvent e) {

	}

	@Override
	public void requestSimpleMove(SimpleMoveRequestEvent e) {

	}

	@Override
	public void requestUsage(UsageRequestEvent e) {

	}

	public Game() {
		//TODO
	}

	public void launch(byte numberOfPlayers) {
		//TODO
	}

	private void nextTick() {
		//TODO
	}

	private boolean loadSave() {
		//TODO
		return false;
	}

	private void save() {
		//TODO
	}
}
