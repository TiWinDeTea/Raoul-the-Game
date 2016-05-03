package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Created by maxime on 5/2/16.
 */
public class Entity {
	private static final Vector2i spriteSize = new Vector2i(Integer.parseInt(MainPackage.spriteSheetBundle.getString("xresolution")), Integer.parseInt(MainPackage.spriteSheetBundle.getString("yresolution")));
	private EntityType entityType;
	private Image texture;
	private ImageView sprite = new ImageView();
	private Vector2i position;
	private boolean animated;
	private Animation animation;

	public Entity(EntityType entityType, Vector2i position) {
		this.position = position;
		this.entityType = entityType;
		Image texture = new Image(MainPackage.path + "/" + MainPackage.spriteSheetBundle.getString("players.file"));
		this.sprite.setImage(texture);
		this.sprite.setViewport(new Rectangle2D(entityType.getSpritesPosition().x * spriteSize.x, entityType.getSpritesPosition().y * spriteSize.y, spriteSize.x, spriteSize.y));

		if(entityType.getSpritesNumber() > 1) {
			this.animate();
		}
	}

	private void animate() {
		this.animated = true;
		this.animation = new SpriteAnimation(this.sprite, this.entityType.getSpritesNumber(), this.entityType.getSpritesPosition(), spriteSize, Duration.millis(this.entityType.getAnimationSpeed()));
		this.animation.setCycleCount(Animation.INDEFINITE);
		this.animation.play();
	}
}
