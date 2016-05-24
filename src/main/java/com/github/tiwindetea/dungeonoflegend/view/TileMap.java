package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.events.tilemap.TileClickEvent;
import com.github.tiwindetea.dungeonoflegend.events.tilemap.TileDragEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.tilemap.TileMapListener;
import com.github.tiwindetea.dungeonoflegend.model.Tile;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.entities.Entity;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;


/**
 * The type TileMap.
 *
 * @author Maxime PINARD
 */
public class TileMap extends Parent {

	private static final int CANVAS_MAX_WIDTH = 2048;
	private static final int CANVAS_MAX_HEIGHT = 2048;
	private static final int RCANVAS_MAX_WIDTH = (int) (Math.floor(CANVAS_MAX_WIDTH / ViewPackage.spritesSize.x) * ViewPackage.spritesSize.x);
	private static final int RCANVAS_MAX_HEIGHT = (int) (Math.floor(CANVAS_MAX_HEIGHT / ViewPackage.spritesSize.y) * ViewPackage.spritesSize.y);

	private static final Tile EMPTY_TILE = Tile.UNKNOWN;

	private static final double MAX_SCALE = 10.0d;
	private static final double MIN_SCALE = .1d;
	private static final double DELTA = 1.1d;
	private final DoubleProperty scale = new SimpleDoubleProperty(1.0);

	private final List<TileMapListener> listeners = new ArrayList<>();

	private double leftMouseAnchorX;
	private double leftMouseAnchorY;
	private double translateAnchorX;
	private double translateAnchorY;
	private double rightMouseAnchorX;
	private double rightMouseAnchorY;

	private Canvas[][] canvases;

	private final List<Entity> entities = new ArrayList<>();

	private Tile[][] realTileMap;
	private boolean[][] visibleTiles;
	private boolean[][] foggedTiles;

	private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {

			// left mouse button
			if(event.isPrimaryButtonDown()) {
				TileMap.this.leftMouseAnchorX = event.getSceneX();
				TileMap.this.leftMouseAnchorY = event.getSceneY();

				TileMap.this.translateAnchorX = getTranslateX();
				TileMap.this.translateAnchorY = getTranslateY();
			}

			// right mouse button
			if(event.isSecondaryButtonDown()) {
				TileMap.this.rightMouseAnchorX = event.getX();
				TileMap.this.rightMouseAnchorY = event.getY();
			}

		}

	};

	private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {

			// right mouse button
			if(event.getButton() == MouseButton.SECONDARY) {
				Vector2i tilePosition = new Vector2i((int) Math.floor(event.getX() / ViewPackage.spritesSize.x), (int) Math.floor(event.getY() / ViewPackage.spritesSize.y));
				if(((int) Math.floor(TileMap.this.rightMouseAnchorX / ViewPackage.spritesSize.x) == tilePosition.x) && ((int) Math.floor(event.getY() / ViewPackage.spritesSize.y) == tilePosition.y)) {
					fireTileClickEvent(new TileClickEvent(tilePosition));
				}
			}
		}
	};

	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {

			// right mouse button
			if(!event.isPrimaryButtonDown())
				return;

			double newTranslateX = TileMap.this.translateAnchorX + event.getSceneX() - TileMap.this.leftMouseAnchorX;
			double newTranslateY = TileMap.this.translateAnchorY + event.getSceneY() - TileMap.this.leftMouseAnchorY;

			TileMap tileMap = TileMap.this;
			Pane parent = (Pane) (tileMap.getParent());

			double width = getWidth() * getScale();
			double height = getHeight() * getScale();

			//translateXProperty().addListener((observable, oldValue, newValue) ->);
			double realOldTranslateX = getTranslateX() + (getWidth() - width) / 2;
			double realOldTranslateY = getTranslateY() + (getHeight() - height) / 2;
			double realNewTranslateX = newTranslateX + (getWidth() - width) / 2;
			double realNewTranslateY = newTranslateY + (getHeight() - height) / 2;

			if(width > parent.getWidth()) {
				if(newTranslateX < tileMap.getTranslateX()) {
					if(realOldTranslateX + width > parent.getWidth()) {
						TileMap.this.setTranslateX(newTranslateX);
					}
				}
				else {
					if(realOldTranslateX < 0) {
						TileMap.this.setTranslateX(newTranslateX);
					}
				}
			}
			else {
				if(newTranslateX > tileMap.getTranslateX()) {
					if(realOldTranslateX + width < parent.getWidth()) {
						TileMap.this.setTranslateX(newTranslateX);
					}
				}
				else {
					if(realOldTranslateX > 0) {
						TileMap.this.setTranslateX(newTranslateX);
					}
				}
			}

			if(height > parent.getHeight()) {
				if(newTranslateY < tileMap.getTranslateY()) {
					if(realOldTranslateY + height > parent.getHeight()) {
						TileMap.this.setTranslateY(newTranslateY);
					}
				}
				else {
					if(realOldTranslateY < 0) {
						TileMap.this.setTranslateY(newTranslateY);
					}
				}
			}
			else {
				if(newTranslateY > tileMap.getTranslateY()) {
					if(realOldTranslateY + height < parent.getHeight()) {
						TileMap.this.setTranslateY(newTranslateY);
					}
				}
				else {
					if(realOldTranslateY > 0) {
						TileMap.this.setTranslateY(newTranslateY);
					}
				}
			}

			event.consume();
		}
	};

	private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

		@Override
		public void handle(ScrollEvent event) {

			double delta = DELTA;

			double scale = TileMap.this.scale.get();
			double oldScale = scale;

			if(event.getDeltaY() < 0)
				scale /= delta;
			else
				scale *= delta;

			scale = clamp(scale, MIN_SCALE, MAX_SCALE);

			double f = (scale / oldScale) - 1;

			double dx = (event.getSceneX() - (TileMap.this.getBoundsInParent().getWidth() / 2 + TileMap.this.getBoundsInParent().getMinX()));
			double dy = (event.getSceneY() - (TileMap.this.getBoundsInParent().getHeight() / 2 + TileMap.this.getBoundsInParent().getMinY()));

			TileMap.this.scale.set(scale);

			// note: pivot value must be untransformed, i. e. without scaling
			TileMap.this.setPivot(f * dx, f * dy);

			event.consume();
		}
	};

	/**
	 * Gets scale.
	 *
	 * @return the scale
	 */
	public double getScale() {
		return this.scale.get();
	}

	/**
	 * Gets width.
	 *
	 * @return the width
	 */
	public double getWidth() {
		return (this.canvases.length - 1) * RCANVAS_MAX_WIDTH + this.canvases[this.canvases.length - 1][0].getWidth();
	}

	/**
	 * Gets height.
	 *
	 * @return the height
	 */
	public double getHeight() {
		return (this.canvases[0].length - 1) * RCANVAS_MAX_HEIGHT + this.canvases[0][this.canvases[0].length - 1].getHeight();
	}

	/**
	 * Sets scaling pivot.
	 *
	 * @param x the x pivot position
	 * @param y the y pivot position
	 */
	public void setPivot(double x, double y) {
		setTranslateX(getTranslateX() - x);
		setTranslateY(getTranslateY() - y);
	}

	/**
	 * Instantiates a new TileMap.
	 */
	public TileMap() {

		scaleXProperty().bind(this.scale);
		scaleYProperty().bind(this.scale);
		setOnMousePressed(this.onMousePressedEventHandler);
		setOnMouseReleased(this.onMouseReleasedEventHandler);
		setOnMouseDragged(this.onMouseDraggedEventHandler);
		setOnScroll(this.onScrollEventHandler);
		setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (event.getGestureSource() != TileMap.this && event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.COPY);
				}
				event.consume();
			}
		});
		setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				System.out.println("lol");
				boolean success = false;
				Dragboard db = event.getDragboard();
				Vector2i tilePosition = new Vector2i((int) Math.floor(event.getX() / ViewPackage.spritesSize.x), (int) Math.floor(event.getY() / ViewPackage.spritesSize.y));
				if (db.hasString()) {
					try {
						fireTileDragEvent(new TileDragEvent(tilePosition, Long.parseLong(db.getString())));
						success = true;
					} catch (Exception e) {
						System.out.println("Cannot parse " + db.getString() + " to a long");
					}
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});
	}

	/**
	 * Sets the real map, create and place canevases, draw the real map onthe canevases.
	 * Multiple canevases are used for old graphical cards that can't manage large textures.
	 *
	 * @param map the map
	 */
	public void setMap(Tile[][] map) {
		if(map == null || map.length == 0) {
			return;
		}

		this.realTileMap = map.clone();

		int mapWidth = map.length;
		int mapHeight = map[0].length;

		int horizontalCanevasNumbers = (int) (Math.floor((mapWidth * ViewPackage.spritesSize.x) / RCANVAS_MAX_WIDTH)) + 1;
		int verticalCanevasNumbers = (int) (Math.floor((mapHeight * ViewPackage.spritesSize.y) / RCANVAS_MAX_HEIGHT)) + 1;
		this.canvases = new Canvas[horizontalCanevasNumbers][verticalCanevasNumbers];

		for(int i = 0; i < horizontalCanevasNumbers - 1; i++) {
			for(int j = 0; j < verticalCanevasNumbers - 1; j++) {
				this.canvases[i][j] = new Canvas(RCANVAS_MAX_WIDTH, RCANVAS_MAX_HEIGHT);
				this.canvases[i][j].setTranslateX(i * RCANVAS_MAX_WIDTH);
				this.canvases[i][j].setTranslateY(j * RCANVAS_MAX_HEIGHT);
			}
			int canevasesY = verticalCanevasNumbers - 1;
			this.canvases[i][canevasesY] = new Canvas(RCANVAS_MAX_WIDTH, mapHeight * ViewPackage.spritesSize.y - (verticalCanevasNumbers - 1) * RCANVAS_MAX_HEIGHT);
			this.canvases[i][canevasesY].setTranslateX(i * RCANVAS_MAX_WIDTH);
			this.canvases[i][canevasesY].setTranslateY(canevasesY * RCANVAS_MAX_HEIGHT);
		}
		for(int i = 0; i < verticalCanevasNumbers - 1; i++) {
			int canevasesX = horizontalCanevasNumbers - 1;
			this.canvases[canevasesX][i] = new Canvas(mapWidth * ViewPackage.spritesSize.x - (horizontalCanevasNumbers - 1) * RCANVAS_MAX_WIDTH, RCANVAS_MAX_HEIGHT);
			this.canvases[canevasesX][i].setTranslateX(canevasesX * RCANVAS_MAX_WIDTH);
			this.canvases[canevasesX][i].setTranslateY(i * RCANVAS_MAX_HEIGHT);
		}
		int canevasesX = this.canvases.length - 1;
		int canevasesY = this.canvases[0].length - 1;
		this.canvases[canevasesX][canevasesY] = new Canvas(mapWidth * ViewPackage.spritesSize.x - (horizontalCanevasNumbers - 1) * RCANVAS_MAX_WIDTH, mapHeight * ViewPackage.spritesSize.y - (verticalCanevasNumbers - 1) * RCANVAS_MAX_HEIGHT);
		this.canvases[canevasesX][canevasesY].setTranslateX(canevasesX * RCANVAS_MAX_WIDTH);
		this.canvases[canevasesX][canevasesY].setTranslateY(canevasesY * RCANVAS_MAX_HEIGHT);

		Image image = ViewPackage.objectsImage;
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				drawImage(image, map[i][j].getSpritePosition(i + j).x, map[i][j].getSpritePosition(i + j).y, i, j);
			}
		}

		getChildren().clear();
		for(Canvas[] canvas : this.canvases) {
			getChildren().addAll(canvas);
		}
		getChildren().addAll(this.entities);

		//TODO:
		this.visibleTiles = new boolean[this.realTileMap.length][this.realTileMap[0].length];
		this.foggedTiles = new boolean[this.realTileMap.length][this.realTileMap[0].length];
		setAllTilesFogged(false);
		setAllTilesVisibility(false);
		updateEntitiesVisibility();
	}

	private void drawRealTile(Vector2i tilePosition) {
		Tile tile = this.realTileMap[tilePosition.x][tilePosition.y];
		drawImage(ViewPackage.objectsImage, tile.getSpritePosition(tilePosition.x + tilePosition.y).x, tile.getSpritePosition(tilePosition.x + tilePosition.y).y, tilePosition.x, tilePosition.y);
	}

	private void drawEmptyTile(Vector2i tilePosition) {
		drawImage(ViewPackage.objectsImage, EMPTY_TILE.getSpritePosition(tilePosition.x + tilePosition.y).x, EMPTY_TILE.getSpritePosition(tilePosition.x + tilePosition.y).y, tilePosition.x, tilePosition.y);
	}

	private void drawFoggedTile(Vector2i tilePosition) {
		drawRealTile(tilePosition);
		drawImage(ViewPackage.objectsImage, ViewPackage.fogSpritePosition.x, ViewPackage.fogSpritePosition.y, tilePosition.x, tilePosition.y);
	}

	/**
	 * Draw a tile.
	 * Draw the real tile is the tile is visible,
	 * the fogged tile if the tile isn't visible and is fogged
	 * and the unknow tile if the tile isn't visible and fogged.
	 *
	 * @param tilePosition the tile position
	 */
	public void drawTile(Vector2i tilePosition) {
		if(this.visibleTiles[tilePosition.x][tilePosition.y]) {
			drawRealTile(tilePosition);
		}
		else {
			if(this.foggedTiles[tilePosition.x][tilePosition.y]) {
				drawFoggedTile(tilePosition);
			}
			else {
				drawEmptyTile(tilePosition);
			}
		}
	}

	/**
	 * Determine if a tile is visible.
	 *
	 * @param tilePosition the tile position
	 * @return True if visible, False otherwise
	 */
	public boolean isVisible(Vector2i tilePosition) {
		return this.visibleTiles[tilePosition.x][tilePosition.y];
	}

	/**
	 * Determine if a tile is fogged.
	 *
	 * @param tilePosition the tile position
	 * @return True if fogged, False otherwise
	 */
	public boolean isFogged(Vector2i tilePosition) {
		return this.foggedTiles[tilePosition.x][tilePosition.y];
	}

	/**
	 * Add a sprite on a tile.
	 *
	 * @param image          the image
	 * @param spritePosition the sprite position
	 * @param tilePosition   the tile position
	 */
	public void addOnTile(Image image, Vector2i spritePosition, Vector2i tilePosition) {
		drawImage(image, spritePosition.x, spritePosition.y, tilePosition.x, tilePosition.y);
	}

	/**
	 * Draw real tiles.
	 *
	 * @param tilesPosition the tiles position
	 */
	public void drawRealTiles(List<Vector2i> tilesPosition) {
		tilesPosition.forEach(this::drawRealTile);
	}

	/**
	 * Sets real tile and draw it.
	 *
	 * @param tileType     the tile type
	 * @param tilePosition the tile position
	 */
	public void setTile(Tile tileType, Vector2i tilePosition) {
		if(tilePosition.x < this.realTileMap.length && tilePosition.y < this.realTileMap[0].length) {
			this.realTileMap[tilePosition.x][tilePosition.y] = tileType;
			drawImage(ViewPackage.objectsImage, tileType.getSpritePosition(tilePosition.x + tilePosition.y).x, tileType.getSpritePosition(tilePosition.x + tilePosition.y).y, tilePosition.x, tilePosition.y);
		}
	}

	/**
	 * Sets real tiles and draw them.
	 *
	 * @param tileType      the tile type
	 * @param tilesPosition the tiles position
	 */
	public void setTiles(Tile tileType, List<Vector2i> tilesPosition) {
		for(Vector2i tilePosition : tilesPosition) {
			setTile(tileType, tilePosition);
		}
	}

	/**
	 * Sets all real tiles and draw them.
	 *
	 * @param tileType the tile type
	 */
	public void setAllTiles(Tile tileType) {
		for(int i = 0; i < this.realTileMap.length; i++) {
			for(int j = 0; j < this.realTileMap[i].length; j++) {
				this.realTileMap[i][j] = tileType;
				addOnTile(ViewPackage.objectsImage, this.realTileMap[i][j].getSpritePosition(i + j), new Vector2i(i, j));
			}
		}
	}

	/**
	 * Gets grid size(the size of the map in tiles).
	 *
	 * @return the grid size
	 */
	public Vector2i getGridSize() {
		return new Vector2i(this.realTileMap.length, this.realTileMap[0].length);
	}

	/**
	 * Sets tiles visibility and update the canevases.
	 *
	 * @param visibleTiles the visible tiles
	 */
	public void setVisibleTiles(boolean[][] visibleTiles) {
		if(visibleTiles.length != this.realTileMap.length || visibleTiles[0].length != this.realTileMap[0].length) {
			System.out.println("~NOPE");
			System.out.println("visibleTiles.length = " + visibleTiles.length);
			System.out.println("realTileMap.length = " + this.realTileMap.length);
			System.out.println("visibleTiles[0].length = " + visibleTiles[0].length);
			System.out.println("realTileMap[0].length = " + this.realTileMap[0].length);
		}
		else {
			updateVisibleTiles(visibleTiles);
			this.visibleTiles = visibleTiles;
			updateEntitiesVisibility();
		}
	}

	/**
	 * Sets all tiles visibility.
	 *
	 * @param visibility true for visible, false for not visible
	 */
	public void setAllTilesVisibility(boolean visibility) {
		this.visibleTiles = new boolean[this.realTileMap.length][this.realTileMap[0].length];
		if(visibility) {
			for(int i = 0; i < this.realTileMap.length; i++) {
				for(int j = 0; j < this.realTileMap[i].length; j++) {
					this.visibleTiles[i][j] = true;
				}
			}
		}
		redrawTiles();
		updateEntitiesVisibility();
	}

	/**
	 * Sets all tiles fogged.
	 *
	 * @param fogged true for fogged, false for not fogged
	 */
	public void setAllTilesFogged(boolean fogged) {
		this.foggedTiles = new boolean[this.realTileMap.length][this.realTileMap[0].length];
		if(fogged) {
			for(int i = 0; i < this.realTileMap.length; i++) {
				for(int j = 0; j < this.realTileMap[i].length; j++) {
					this.foggedTiles[i][j] = true;
				}
			}
		}
		redrawTiles();
		updateEntitiesVisibility();
	}

	/**
	 * Add an entity and set it visibility.
	 *
	 * @param entity the entity
	 */
	public void addEntity(Entity entity) {
		this.entities.add(entity);
		getChildren().add(entity);
		updateEntitiesVisibility();
	}

	/**
	 * Remove an entity.
	 *
	 * @param entity the entity
	 */
	public void removeEntity(Entity entity) {
		this.entities.remove(entity);
		getChildren().remove(entity);
	}

	private void redrawTiles() {
		for(int i = 0; i < this.realTileMap.length; i++) {
			for(int j = 0; j < this.realTileMap[i].length; j++) {
				if(this.visibleTiles[i][j]) {
					drawRealTile(new Vector2i(i, j));
				}
				else {
					if(this.foggedTiles[i][j]) {
						drawFoggedTile(new Vector2i(i, j));
					}
					else {
						drawEmptyTile(new Vector2i(i, j));
					}
				}
			}
		}
	}

	private void updateVisibleTiles(boolean[][] newVisibleTiles) {
		for(int i = 0; i < this.realTileMap.length; i++) {
			for(int j = 0; j < this.realTileMap[i].length; j++) {
				if(this.visibleTiles[i][j] != newVisibleTiles[i][j]) {
					if(newVisibleTiles[i][j]) {
						drawRealTile(new Vector2i(i, j));
						//System.out.println("TileMap: tile " + i + " " + j + " visible");
					}
					else {
						if(this.foggedTiles[i][j]) {
							drawFoggedTile(new Vector2i(i, j));
							//System.out.println("TileMap: tile " + i + " " + j + " fogged");
						}
						else {
							drawEmptyTile(new Vector2i(i, j));
							//System.out.println("TileMap: tile " + i + " " + j + " empty");
						}
					}
				}
				//******************************************************************************************************************************************************************
				//to display the complete map
				//******************************************************************************************************************************************************************
				//drawRealTile(new Vector2i(i, j));
			}
		}
	}

	private void updateEntitiesVisibility() {
		for(Entity entity : this.entities) {
			if(this.visibleTiles[entity.getPosition().x][entity.getPosition().y]) {
				entity.setVisible(true);
			}
			else {
				if(this.foggedTiles[entity.getPosition().x][entity.getPosition().y]) {
					entity.setVisible(entity.isVisibleOnFog());
				}
				else {
					entity.setVisible(false);
				}
			}
		}
	}

	/**
	 * Add fogged tiles.
	 *
	 * @param fogCenterPosition the fogged tiles array center position on the map
	 * @param newFoggedTiles    the new fogged tiles
	 */
	public void addFoggedTiles(Vector2i fogCenterPosition, boolean[][] newFoggedTiles) {
		if(newFoggedTiles == null || newFoggedTiles.length == 0)
			return;
		if((newFoggedTiles.length & 1) == 0) {
			System.out.println("Warning: newFoggedTiles width not odd: " + newFoggedTiles.length);
		}
		if((newFoggedTiles[0].length & 1) == 0) {
			System.out.println("Warning: newFoggedTiles height not odd: " + newFoggedTiles[0].length);
		}
		int fogPosX = fogCenterPosition.x - ((newFoggedTiles.length - 1) / 2);
		int fogPosY = fogCenterPosition.y - ((newFoggedTiles[0].length - 1) / 2);
		for(int i = 0; i < newFoggedTiles.length; i++) {
			for(int j = 0; j < newFoggedTiles[i].length; j++) {
				if(((fogPosX + i >= 0) && (fogPosX + i < this.visibleTiles.length))
				  && ((fogPosY + j >= 0) && (fogPosY + j < this.visibleTiles[0].length))) {
					if(this.foggedTiles[fogPosX + i][fogPosY + j] |= newFoggedTiles[i][j]) {
						drawTile(new Vector2i(fogPosX + i, fogPosY + j));
					}
				}
			}
		}
		updateEntitiesVisibility();
	}

	private void drawImage(Image img, double sx, double sy, double dx, double dy) {
		int canvasesX = (int) Math.floor((dx * ViewPackage.spritesSize.x) / RCANVAS_MAX_WIDTH);
		int canvasesY = (int) Math.floor((dy * ViewPackage.spritesSize.y) / RCANVAS_MAX_HEIGHT);
		this.canvases[canvasesX][canvasesY].getGraphicsContext2D().drawImage(img, sx * ViewPackage.spritesSize.x, sy * ViewPackage.spritesSize.y, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y, dx * ViewPackage.spritesSize.x - canvasesX * RCANVAS_MAX_WIDTH, dy * ViewPackage.spritesSize.y - canvasesY * RCANVAS_MAX_HEIGHT, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y);
	}

	private double clamp(double value, double min, double max) {

		if(Double.compare(value, min) < 0)
			return min;

		if(Double.compare(value, max) > 0)
			return max;

		return value;
	}

	/**
	 * Add a TileMapListener.
	 *
	 * @param listener the listener
	 */
	public void addTileMapListener(TileMapListener listener) {
		this.listeners.add(listener);
	}

	private TileMapListener[] getTileMapListeners() {
		return this.listeners.toArray(new TileMapListener[this.listeners.size()]);
	}

	private void fireTileClickEvent(TileClickEvent event) {
		System.out.println(event.tilePosition);
		for(TileMapListener listener : this.getTileMapListeners()) {
			listener.tileClicked(event);
		}
	}

	private void fireTileDragEvent(TileDragEvent event) {
		System.out.println(event.tilePosition);
		for (TileMapListener listener : this.getTileMapListeners()) {
			listener.tileDragged(event);
		}
	}

	/**
	 * Center the view on a tile.
	 *
	 * @param tilePosition the tile position
	 */
	public void centerViewOnTile(Vector2i tilePosition) {

		Pane parent = (Pane) (getParent());

		double Xdiff = (parent.getWidth() / 2) - (ViewPackage.spritesSize.x / 2) - (tilePosition.x * ViewPackage.spritesSize.x) - getTranslateX();
		double Ydiff = (parent.getHeight() / 2) - (ViewPackage.spritesSize.y / 2) - (tilePosition.y * ViewPackage.spritesSize.y) - getTranslateY();

		this.scale.set(1);

		setTranslateX(getTranslateX() + Xdiff);
		setTranslateY(getTranslateY() + Ydiff);
	}
}
