package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Map;
import com.github.tiwindetea.dungeonoflegend.model.Seed;
import com.github.tiwindetea.dungeonoflegend.model.Tile;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Random;

/**
 * Created by maxime on 5/6/16.
 */
public class TileMap extends Parent {


	public static final int CANVAS_MAX_WIDTH = 1024;
	public static final int CANVAS_MAX_HEIGHT = 1024;
	public static final int RCANVAS_MAX_WIDTH = (int) (Math.floor(CANVAS_MAX_WIDTH / ViewPackage.spritesSize.x) * ViewPackage.spritesSize.x);
	public static final int RCANVAS_MAX_HEIGHT = (int) (Math.floor(CANVAS_MAX_HEIGHT / ViewPackage.spritesSize.y) * ViewPackage.spritesSize.y);

	public static final Tile emptyTile = Tile.UNKNOWN;

	private static final double MAX_SCALE = 10.0d;
	private static final double MIN_SCALE = .1d;
	private static final double DELTA = 1.1d;
	private final DoubleProperty scale = new SimpleDoubleProperty(1.0);
	private double mouseAnchorX;
	private double mouseAnchorY;
	private double translateAnchorX;
	private double translateAnchorY;

	private Canvas[][] canvases;

	private Tile[][] realTileMap;
	private boolean[][] visibleTiles;
	private boolean[][] fogedTiles;

	public double getScale() {
		return this.scale.get();
	}

	private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {

			// right mouse button
			if(!event.isPrimaryButtonDown())
				return;

			TileMap.this.mouseAnchorX = event.getSceneX();
			TileMap.this.mouseAnchorY = event.getSceneY();

			TileMap.this.translateAnchorX = getTranslateX();
			TileMap.this.translateAnchorY = getTranslateY();

		}

	};

	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {

			// right mouse button
			if(!event.isPrimaryButtonDown())
				return;

			double newTranslateX = TileMap.this.translateAnchorX + event.getSceneX() - TileMap.this.mouseAnchorX;
			double newTranslateY = TileMap.this.translateAnchorY + event.getSceneY() - TileMap.this.mouseAnchorY;

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

	public double getWidth() {
		return (this.canvases.length - 1) * RCANVAS_MAX_WIDTH + this.canvases[this.canvases.length - 1][0].getWidth();
	}

	public double getHeight() {
		return (this.canvases[0].length - 1) * RCANVAS_MAX_HEIGHT + this.canvases[0][this.canvases[0].length - 1].getHeight();
	}

	public void setPivot(double x, double y) {
		setTranslateX(getTranslateX() - x);
		setTranslateY(getTranslateY() - y);
	}

	public TileMap() {

		scaleXProperty().bind(this.scale);
		scaleYProperty().bind(this.scale);
		setOnMousePressed(this.onMousePressedEventHandler);
		setOnMouseDragged(this.onMouseDraggedEventHandler);
		setOnScroll(this.onScrollEventHandler);

		// test
		Random rand = new Random();
		Map map = new Map(new Seed(rand.nextInt(), rand.nextInt()));
		map.generateLevel(1);
		this.setMap(map.getMapCopy());
	}

	public void setMap(Tile[][] map) {
		if(map == null || map.length == 0) {
			return;
		}

		this.realTileMap = map.clone();

		getChildren().removeAll();

		int mapWidth = map.length;
		int mapHeight = map[0].length;

		int horizontalCanevasNumbers = (int) (Math.floor((mapWidth * ViewPackage.spritesSize.x) / RCANVAS_MAX_WIDTH)) + 1;
		int verticalCanevasNumbers = (int) (Math.floor((mapHeight * ViewPackage.spritesSize.y) / RCANVAS_MAX_HEIGHT)) + 1;
		this.canvases = new Canvas[horizontalCanevasNumbers][verticalCanevasNumbers];

		for(int i = 0; i < horizontalCanevasNumbers - 1; i++) {
			for(int j = 0; j < verticalCanevasNumbers - 1; j++) {
				this.canvases[i][j] = new Canvas(RCANVAS_MAX_WIDTH, RCANVAS_MAX_HEIGHT);
				this.canvases[i][j].setTranslateX(i * (RCANVAS_MAX_WIDTH - 1));
				this.canvases[i][j].setTranslateY(j * (RCANVAS_MAX_HEIGHT - 1));
			}
			int canevasesY = verticalCanevasNumbers - 1;
			this.canvases[i][canevasesY] = new Canvas(RCANVAS_MAX_WIDTH, mapHeight * ViewPackage.spritesSize.y - (verticalCanevasNumbers - 1) * RCANVAS_MAX_HEIGHT);
			this.canvases[i][canevasesY].setTranslateX(i * (RCANVAS_MAX_WIDTH - 1));
			this.canvases[i][canevasesY].setTranslateY(canevasesY * (RCANVAS_MAX_HEIGHT - 1));
		}
		for(int i = 0; i < verticalCanevasNumbers - 1; i++) {
			int canevasesX = horizontalCanevasNumbers - 1;
			this.canvases[canevasesX][i] = new Canvas(mapWidth * ViewPackage.spritesSize.x - (horizontalCanevasNumbers - 1) * RCANVAS_MAX_WIDTH, RCANVAS_MAX_HEIGHT);
			this.canvases[canevasesX][i].setTranslateX(canevasesX * (RCANVAS_MAX_WIDTH - 1));
			this.canvases[canevasesX][i].setTranslateY(i * (RCANVAS_MAX_HEIGHT - 1));
		}
		int canevasesX = this.canvases.length - 1;
		int canevasesY = this.canvases[0].length - 1;
		this.canvases[canevasesX][canevasesY] = new Canvas(mapWidth * ViewPackage.spritesSize.x - (horizontalCanevasNumbers - 1) * RCANVAS_MAX_WIDTH, mapHeight * ViewPackage.spritesSize.y - (verticalCanevasNumbers - 1) * RCANVAS_MAX_HEIGHT);
		this.canvases[canevasesX][canevasesY].setTranslateX(canevasesX * (RCANVAS_MAX_WIDTH - 1));
		this.canvases[canevasesX][canevasesY].setTranslateY(canevasesY * (RCANVAS_MAX_HEIGHT - 1));

		Image image = ViewPackage.objectsImage;
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				drawImage(image, map[i][j].getSpritePosition(i + j).x, map[i][j].getSpritePosition(i + j).y, i, j);
			}
		}

		for(Canvas[] canvas : this.canvases) {
			getChildren().addAll(canvas);
		}

		//TODO:
		this.visibleTiles = new boolean[this.realTileMap.length][this.realTileMap[0].length];
		this.fogedTiles = new boolean[this.realTileMap.length][this.realTileMap[0].length];
		setAllTilesFog(false);
		setAllTilesVisibility(true);
	}

	private void drawRealTile(Vector2i tilePosition) {
		Tile tile = this.realTileMap[tilePosition.x][tilePosition.y];
		drawImage(ViewPackage.objectsImage, tile.getSpritePosition(tilePosition.x + tilePosition.y).x, tile.getSpritePosition(tilePosition.x + tilePosition.y).y, tilePosition.x, tilePosition.y);
	}

	private void drawEmptyTile(Vector2i tilePosition) {
		drawImage(ViewPackage.objectsImage, emptyTile.getSpritePosition(tilePosition.x + tilePosition.y).x, emptyTile.getSpritePosition(tilePosition.x + tilePosition.y).y, tilePosition.x, tilePosition.y);
	}

	private void drawFoggedTile(Vector2i tilePosition) {
		drawRealTile(tilePosition);
		//TODO: fog the tile
	}

	public void drawTile(Vector2i tilePosition) {
		if(this.visibleTiles[tilePosition.x][tilePosition.y]) {
			drawRealTile(tilePosition);
		}
		else {
			if(this.fogedTiles[tilePosition.x][tilePosition.y]) {
				drawFoggedTile(tilePosition);
			}
			else {
				drawEmptyTile(tilePosition);
			}
		}
	}

	public boolean isVisible(Vector2i tilePosition) {
		return this.visibleTiles[tilePosition.x][tilePosition.y];
	}

	public boolean isFogged(Vector2i tilePosition) {
		return this.fogedTiles[tilePosition.x][tilePosition.y];
	}

	public void addOnTile(Image image, Vector2i spritePosition, Vector2i tilePosition) {
		drawImage(image, spritePosition.x, spritePosition.y, tilePosition.x, tilePosition.y);
	}

	public void drawRealTiles(List<Vector2i> tilesPosition) {
		tilesPosition.forEach(this::drawRealTile);
	}

	public void setTiles(Tile tileType, List<Vector2i> tilesPosition) {
		for(Vector2i tilePosition : tilesPosition) {
			drawImage(ViewPackage.objectsImage, tileType.getSpritePosition(tilePosition.x + tilePosition.y).x, tileType.getSpritePosition(tilePosition.x + tilePosition.y).y, tilePosition.x, tilePosition.y);
		}
	}

	public void setAllTiles(Tile tileType) {
		for(int i = 0; i < this.realTileMap.length; i++) {
			for(int j = 0; j < this.realTileMap[i].length; j++) {
				addOnTile(ViewPackage.objectsImage, this.realTileMap[i][j].getSpritePosition(i + j), new Vector2i(i, j));
			}
		}
	}

	public Vector2i getGridSize() {
		return new Vector2i(this.realTileMap.length, this.realTileMap[0].length);
	}

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
		}
	}

	public void setAllTilesVisibility(boolean visibility) {
		this.visibleTiles = new boolean[this.realTileMap.length][this.realTileMap[0].length];
		for(int i = 0; i < this.realTileMap.length; i++) {
			for(int j = 0; j < this.realTileMap[i].length; j++) {
				this.visibleTiles[i][j] = visibility;
			}
		}
		redrawTiles();
	}

	public void setAllTilesFog(boolean fog) {
		this.fogedTiles = new boolean[this.realTileMap.length][this.realTileMap[0].length];
		for(int i = 0; i < this.realTileMap.length; i++) {
			for(int j = 0; j < this.realTileMap[i].length; j++) {
				this.fogedTiles[i][j] = fog;
			}
		}
		redrawTiles();
	}

	private void redrawTiles() {
		for(int i = 0; i < this.realTileMap.length; i++) {
			for(int j = 0; j < this.realTileMap[i].length; j++) {
				if(this.visibleTiles[i][j]) {
					drawRealTile(new Vector2i(i, j));
				}
				else {
					if(this.fogedTiles[i][j]) {
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
					}
					else {
						if(this.fogedTiles[i][j]) {
							drawFoggedTile(new Vector2i(i, j));
						}
						else {
							drawEmptyTile(new Vector2i(i, j));
						}
					}
				}
			}
		}
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
}
