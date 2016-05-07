package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Map;
import com.github.tiwindetea.dungeonoflegend.model.Seed;
import com.github.tiwindetea.dungeonoflegend.model.Tile;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.util.Random;

/**
 * Created by maxime on 5/6/16.
 */
public class TileMap extends Parent {


	public static final int CANVAS_MAX_WIDTH = 1024;
	public static final int CANVAS_MAX_HEIGHT = 1024;
	public static final int RCANVAS_MAX_WIDTH = (int) (Math.floor(CANVAS_MAX_WIDTH / ViewPackage.spritesSize.x) * ViewPackage.spritesSize.x);
	public static final int RCANVAS_MAX_HEIGHT = (int) (Math.floor(CANVAS_MAX_HEIGHT / ViewPackage.spritesSize.y) * ViewPackage.spritesSize.y);

	private static final double MAX_SCALE = 10.0d;
	private static final double MIN_SCALE = .1d;
	private static final double DELTA = 1.1d;
	DoubleProperty scale = new SimpleDoubleProperty(1.0);
	double mouseAnchorX;
	double mouseAnchorY;
	double translateAnchorX;
	double translateAnchorY;

	//Canvas canvas = new Canvas();
	Canvas[][] canvases;

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
			Pane prent = (Pane) (tileMap.getParent());

			/*if(newTranslateX < tileMap.getTranslateX()) {
				if(tileMap.getTranslateX() + (tileMap.getWidth() * tileMap.getScale()) > prent.getWidth()) {
					TileMap.this.setTranslateX(newTranslateX);
				}
			}
			else {
				if(tileMap.getTranslateX() < 0) {
					TileMap.this.setTranslateX(newTranslateX);
				}
			}

			if(newTranslateY < tileMap.getTranslateY()) {
				if(tileMap.getTranslateY() + (tileMap.getHeight() * tileMap.getScale()) > prent.getHeight()) {
					TileMap.this.setTranslateY(newTranslateY);
				}
			}
			else {
				if(tileMap.getTranslateY() < 0) {
					TileMap.this.setTranslateY(newTranslateY);
				}
			}*/

			TileMap.this.setTranslateX(newTranslateX);
			TileMap.this.setTranslateY(newTranslateY);

			//TileMap.this.setTranslateX(0);
			//TileMap.this.setTranslateY(0);

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

			//double originalWidth = getWidth();
			//double originalHeight = getHeight();

			TileMap.this.scale.set(scale);

			// note: pivot value must be untransformed, i. e. without scaling
			TileMap.this.setPivot(f * dx, f * dy);

			event.consume();

			//double newWidth = getWidth() * getScale();
			//double newHeight = getHeight() * getScale();

			/*if(newWidth > originalWidth) {
				setLayoutX(getLayoutX() - (originalWidth - newWidth) / 2);
				setLayoutY(getLayoutY() - (originalHeight - newHeight) / 2);
				System.out.println("++");
			}
			else {
				setLayoutX(getLayoutX() + (originalWidth - newWidth) / 2);
				setLayoutY(getLayoutY() + (originalHeight - newHeight) / 2);
				System.out.println("--");
			}*/
			/*Rectangle rect = new Rectangle(50, 50, Color.CYAN);
			rect.setTranslateX(getLayoutX());
			rect.setTranslateY(getLayoutY());
			Rectangle rect2 = new Rectangle(50, 50, Color.PURPLE);
			rect2.setTranslateX(originalWidth);
			rect2.setTranslateY(originalHeight);
			Rectangle rect3 = new Rectangle(50, 50, Color.GREEN);
			rect3.setTranslateX(newWidth);
			rect3.setTranslateY(newHeight);
			getChildren().add(rect);
			getChildren().add(rect2);
			getChildren().add(rect3);*/
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
		setLayoutX(getWidth() / 2);
		setLayoutY(getHeight() / 2);
		setTranslateX(-getWidth() / 2);
		setTranslateY(-getHeight() / 2);
	}

	public void setMap(Tile[][] map) {
		if(map == null || map.length == 0) {
			return;
		}

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
				drawImage(image, map[i][j].getSpritePosition().x, map[i][j].getSpritePosition().y, i, j);
			}
		}

		for(Canvas[] canvas : this.canvases) {
			getChildren().addAll(canvas);
		}
	}

	public void drawImage(Image img, double sx, double sy, double dx, double dy) {
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
