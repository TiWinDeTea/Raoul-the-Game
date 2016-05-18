package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

/**
 * Created by maxime on 5/2/16.
 */
public class StaticEntity extends Entity {
	private StaticEntityType staticEntityType;
	private boolean animated;
	private Animation animation;

	public StaticEntity(StaticEntityType staticEntityType, Vector2i position, String description) {
		super(description);
		this.setPosition(position);
		this.staticEntityType = staticEntityType;
		this.imageView.setImage(staticEntityType.getImage());
		this.imageView.setViewport(new Rectangle2D(staticEntityType.getSpritesPosition().x * spriteSize.x, staticEntityType.getSpritesPosition().y * spriteSize.y, spriteSize.x, spriteSize.y));

		if(staticEntityType.getSpritesNumber() > 1) {
			this.animate();
		}

		getChildren().add(this.imageView);
	}

	public StaticEntity(StaticEntityType staticEntityType, String description) {
		this(staticEntityType, new Vector2i(), description);
	}

	private void animate() {
		this.animated = true;
		this.animation = new SpriteAnimation(this.imageView, this.staticEntityType.getSpritesNumber(), this.staticEntityType.getSpritesPosition(), spriteSize, Duration.millis(this.staticEntityType.getAnimationSpeed()));
		this.animation.setCycleCount(Animation.INDEFINITE);
		this.animation.play();
	}

	@Override
	public void setPosition(Vector2i position) {
		this.position = new Vector2i(position);
		this.imageView.setTranslateX(position.x * spriteSize.x);
		this.imageView.setTranslateY(position.y * spriteSize.y);
	}

	public boolean isAnimated() {
		return this.animated;
	}
}
