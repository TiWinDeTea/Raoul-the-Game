package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Created by maxime on 5/2/16.
 */
public class Entity {
	private static final Vector2i spriteSize = new Vector2i(Integer.parseInt(MainPackage.spriteSheetBundle.getString("xresolution")), Integer.parseInt(MainPackage.spriteSheetBundle.getString("yresolution")));
	private EntityType entityType;
	private ImageView imageView = new ImageView();
	private Vector2i position;
	private boolean animated;
	private Animation animation;

	public Entity(EntityType entityType, Vector2i position) {
		this.setPosition(position);
		this.entityType = entityType;
		this.imageView.setImage(entityType.getImage());
		this.imageView.setViewport(new Rectangle2D(entityType.getSpritesPosition().x * spriteSize.x, entityType.getSpritesPosition().y * spriteSize.y, spriteSize.x, spriteSize.y));

		if(entityType.getSpritesNumber() > 1) {
			this.animate();
		}
	}

	private void animate() {
		this.animated = true;
		this.animation = new SpriteAnimation(this.imageView, this.entityType.getSpritesNumber(), this.entityType.getSpritesPosition(), spriteSize, Duration.millis(this.entityType.getAnimationSpeed()));
		this.animation.setCycleCount(Animation.INDEFINITE);
		this.animation.play();
	}

	public Vector2i getPosition() {
		return this.position;
	}

	public void setPosition(Vector2i position) {
		this.position = new Vector2i(position);
		this.imageView.setTranslateX(position.x);
		this.imageView.setTranslateY(position.y);
	}

	public ImageView getImageView() {
		return this.imageView;
	}
}
