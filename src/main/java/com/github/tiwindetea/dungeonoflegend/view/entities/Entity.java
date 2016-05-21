package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.InformationsDisplayer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * The type Entity.
 *
 * @author Maxime PINARD.
 */
public abstract class Entity extends Parent {
	protected static final Vector2i spriteSize = new Vector2i(Integer.parseInt(MainPackage.spriteSheetBundle.getString("xresolution")), Integer.parseInt(MainPackage.spriteSheetBundle.getString("yresolution")));
	private final Rectangle backgroundRectangle = new Rectangle(spriteSize.x, spriteSize.y);
	protected final ImageView imageView = new ImageView();
	protected Vector2i position;
	protected boolean[][] LOS;

	protected SimpleIntegerProperty XPositionProperty = new SimpleIntegerProperty();
	protected SimpleIntegerProperty YPositionProperty = new SimpleIntegerProperty();

	private String description;

	/**
	 * Instantiates a new Entity.
	 *
	 * @param position    the position
	 * @param description the description
	 */
	public Entity(Vector2i position, String description) {
		setPosition(position);
		this.description = description;

		getChildren().add(this.backgroundRectangle);
		this.backgroundRectangle.setFill(Color.TRANSPARENT);
		this.backgroundRectangle.translateXProperty().bind(this.XPositionProperty);
		this.backgroundRectangle.translateYProperty().bind(this.YPositionProperty);

		this.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.setText(description);
			}
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.clear();
			}
		});
	}

	/**
	 * Sets position.
	 *
	 * @param position the position
	 */
	public void setPosition(Vector2i position) {
		this.position = new Vector2i(position);
		this.XPositionProperty.set(position.x * spriteSize.x);
		this.YPositionProperty.set(position.y * spriteSize.y);
	}

	/**
	 * Gets image view.
	 *
	 * @return the image view
	 */
	public ImageView getImageView() {
		return this.imageView;
	}

	/**
	 * Gets position.
	 *
	 * @return the position
	 */
	public Vector2i getPosition() {
		return this.position;
	}

	/**
	 * Sets LOS.
	 *
	 * @param LOS the LOS
	 */
	public void setLOS(boolean[][] LOS) {
		if(LOS == null || LOS.length == 0)
			return;
		if((LOS.length & 1) == 0) {
			System.out.println("Warning: LOS width not odd: "+LOS.length);
		}
		if((LOS[0].length & 1) == 0) {
			System.out.println("Warning: LOS height not odd: "+LOS[0].length);
		}
		this.LOS = LOS;
	}

	/**
	 * Gets LOS.
	 *
	 * @return the LOS
	 */
	public boolean[][] getLOS() {
		return this.LOS;
	}

	/**
	 * Modifie los.
	 *
	 * @param modifiedTilesPositions the modified tiles positions
	 */
	public void modifieLOS(List<Vector2i> modifiedTilesPositions) {
		if(this.LOS != null && this.LOS.length > 0) {
			for(Vector2i modifiedTilesPosition : modifiedTilesPositions) {
				if(((modifiedTilesPosition.x >= 0) && (modifiedTilesPosition.x < this.LOS.length)) && ((modifiedTilesPosition.y >= 0) && (modifiedTilesPosition.y >= this.LOS[0].length))) {
					this.LOS[modifiedTilesPosition.x][modifiedTilesPosition.y] = !this.LOS[modifiedTilesPosition.x][modifiedTilesPosition.y];
				}
			}
		}
	}

	/**
	 * Gets visibility on fog.
	 *
	 * @return the boolean
	 */
	public abstract boolean isVisibleOnFog();
}
