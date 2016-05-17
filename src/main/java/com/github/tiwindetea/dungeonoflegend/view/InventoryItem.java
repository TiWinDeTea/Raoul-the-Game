package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by maxime on 5/11/16.
 */
public class InventoryItem extends Parent {
	private final StaticEntityType staticEntityType;

	private final ImageView imageView;

	public InventoryItem(StaticEntityType staticEntityType) {
		this.staticEntityType = staticEntityType;
		this.imageView = new ImageView(staticEntityType.getImage());
		this.imageView.setViewport(new Rectangle2D(staticEntityType.getSpritesPosition().x * ViewPackage.spritesSize.x, staticEntityType.getSpritesPosition().y * ViewPackage.spritesSize.y, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y));

		//TODO: Delete test rectangle
		Rectangle rect = new Rectangle(32, 32, Color.YELLOW);
		getChildren().add(rect);

		getChildren().add(this.imageView);

		//TODO: On mouse over send informations to the InformationsDisplayer
	}
}
