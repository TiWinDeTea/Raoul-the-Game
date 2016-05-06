package com.github.tiwindetea.dungeonoflegend.view;

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
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntity;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntityType;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntity;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxime on 5/2/16.
 */
public class GUI implements GameListener {
	private final List<RequestListener> listeners = new ArrayList<>();

	public static final Color bottomBackgroundColor = Color.GREEN;
	public static final Color rightBackgroundColor = Color.CRIMSON;
	public static final Color centerBackgroundColor = Color.BLACK;

	private final BorderPane borderPane = new BorderPane();
	private final Scene scene = new Scene(this.borderPane);
	private final Pane rPane = new Pane();
	private final Pane cPane = new Pane();

	private final Pane bPane = new Pane();
	private final HBox bHBox = new HBox();
	private final TilePane blTilePane = new TilePane();
	private final Pane brMiniMapPain = new Pane();

	private final List<LivingEntity> enemies = new ArrayList<>();
	private final List<LivingEntity> players = new ArrayList<>();
	private final List<StaticEntity> staticEntities = new ArrayList<>();

	private final List<PlayerHUD> playersHUD = new ArrayList<>();
	private final int maxPlayersNumber = 2;

	public GUI() {
		this.init();
	}

	private void init() {

		//Main pane
		//this.rPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		this.borderPane.setRight(this.rPane);
		this.borderPane.setBottom(this.bPane);
		this.borderPane.setCenter(this.cPane);
		this.borderPane.setMinWidth(500);
		this.borderPane.setMinHeight(500);

		//Center pane
		this.cPane.setBackground(new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY)));

		//Right pane
		this.rPane.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
		this.rPane.setMaxWidth(300);
		this.rPane.setMinWidth(300);

		this.brMiniMapPain.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.brMiniMapPain.setMinWidth(300);
		this.brMiniMapPain.setMaxWidth(300);

		this.bHBox.getChildren().addAll(this.blTilePane, this.brMiniMapPain);
		this.bHBox.setMinWidth(2 * PlayerHUD.getSize().x + this.brMiniMapPain.getMinWidth());

		this.bPane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.bPane.setMinHeight(this.playersHUD.size() * PlayerHUD.getSize().y);
		this.bPane.getChildren().add(this.bHBox);
		this.bPane.widthProperty().addListener(e -> {
			if(this.playersHUD.size() > 1) {
				if((this.bPane.getWidth() - this.rPane.getWidth()) < (this.playersHUD.size() * PlayerHUD.getSize().x)) {
					this.bPane.setMinHeight(this.playersHUD.size() * PlayerHUD.getSize().y);
				}
				else {
					this.bPane.setMinHeight(PlayerHUD.getSize().y);
				}
			}
			this.blTilePane.setMaxWidth(this.bPane.getWidth() - this.rPane.getWidth());
		});
	}

	public Scene getScene() {
		return this.scene;
	}

	public void addRequestListener(RequestListener listener) {
		this.listeners.add(listener);
	}

	public RequestListener[] getRequestListener() {
		return this.listeners.toArray(new RequestListener[this.listeners.size()]);
	}

	private void fireDropRequestEvent(DropRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.requestDrop(event);
		}
	}

	private void fireUsageRequestEvent(UsageRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.requestUsage(event);
		}
	}

	private void fireComplexMoveRequestEvent(ComplexMoveRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.requestComplexMove(event);
		}
	}

	private void fireSimpleMoveRequestEvent(SimpleMoveRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.requestSimpleMove(event);
		}
	}

	private void fireInteractionRequestEvent(InteractionRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.requestInteraction(event);
		}
	}

	@Override
	public void addInventory(InventoryAdditionEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void deleteInventory(InventoryDeletionEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void createLivingEntity(LivingEntityCreationEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void deleteLivingEntity(LivingEntityDeletionEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void defineLivingEntityLOS(LivingEntityLOSDefinitionEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void modifieLivingEntityLOS(LivingEntityLOSModificationEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void moveLivingEntity(LivingEntityMoveEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void createPlayer(PlayerCreationEvent e) {
		if(e == null) {
			return;
		}
		if(this.players.size() < this.maxPlayersNumber) {
			LivingEntityType livingEntityType;
			if(e.playerNumber == 2) {
				livingEntityType = LivingEntityType.PLAYER2;
			}
			else {
				livingEntityType = LivingEntityType.PLAYER1;
			}
			this.players.add(new LivingEntity(livingEntityType, e.position, e.direction));

			ImageView imageView = new ImageView(livingEntityType.getImage());
			Vector2i spritePosition = livingEntityType.getSpritePosition(e.direction);
			imageView.setViewport(new Rectangle2D(spritePosition.x * ViewPackage.spritesSize.x, spritePosition.y * ViewPackage.spritesSize.y, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y));
			PlayerHUD playerHUD = new PlayerHUD(imageView, e.maxHealth, e.maxHealth, e.maxMana, e.maxMana);
			this.playersHUD.add(playerHUD);
			this.blTilePane.getChildren().add(playerHUD.getMainGroup());
			if(this.playersHUD.size() > 1) {
				if((this.bPane.getWidth() - this.rPane.getWidth()) < (this.playersHUD.size() * PlayerHUD.getSize().x)) {
					this.bPane.setMinHeight(this.playersHUD.size() * PlayerHUD.getSize().y);
				}
				else {
					this.bPane.setMinHeight(PlayerHUD.getSize().y);
				}
			}

			//TODO: inventory
		}
	}

	@Override
	public void changePlayerStat(PlayerStatEvent e) {
		if(e == null) {
			return;
		}
		if(e.playerNumber > 0 && e.playerNumber <= this.players.size()) {
			int value = e.value;
			if(value < 0) {
				value = 0;
			}
			switch(e.statType) {
			case HEALTH:
				switch(e.valueType) {
				case ACTUAL:
					this.playersHUD.get(e.playerNumber - 1).setActualHealth(value);
					break;
				case MAX:
					this.playersHUD.get(e.playerNumber - 1).setMaxHealth(value);
					break;
				}
				break;
			case MANA:
				switch(e.valueType) {
				case ACTUAL:
					this.playersHUD.get(e.playerNumber - 1).setActualMana(value);
					break;
				case MAX:
					this.playersHUD.get(e.playerNumber - 1).setMaxMana(value);
					break;
				}
				break;
			}
		}
	}

	@Override
	public void createStaticEntity(StaticEntityCreationEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void deleteStaticEntity(StaticEntityDeletionEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void defineStaticEntityLOS(StaticEntityLOSDefinitionEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void createMap(MapCreationEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}
}
