package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by maxime on 5/2/16.
 */
public class Entity {
	private static final int xsize = Integer.parseInt(MainPackage.spriteSheetBundle.getString("xresolution"));
	private static final int ysize = Integer.parseInt(MainPackage.spriteSheetBundle.getString("yresolution"));
	private EntityType entityType;
	private Image texture;
	private ImageView sprite = new ImageView();

	public Entity(EntityType entityType, int spriteVersion) {
		this.entityType = entityType;
		Image texture = new Image(MainPackage.path + "/" + MainPackage.spriteSheetBundle.getString("players.file"));
		this.sprite.setImage(texture);
		this.sprite.setViewport(new Rectangle2D(entityType.getSpritePosition(spriteVersion).x, entityType.getSpritePosition(spriteVersion).y, xsize, ysize));
	}
}
