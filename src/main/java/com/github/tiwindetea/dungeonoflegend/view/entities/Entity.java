package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.scene.image.ImageView;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class Entity {
	protected static final Vector2i spriteSize = new Vector2i(Integer.parseInt(MainPackage.spriteSheetBundle.getString("xresolution")), Integer.parseInt(MainPackage.spriteSheetBundle.getString("yresolution")));
	protected ImageView imageView = new ImageView();
	protected Vector2i position;

	public ImageView getImageView() {
		return this.imageView;
	}

	public Vector2i getPosition() {
		return this.position;
	}

	public abstract void setPosition(Vector2i position);
}
