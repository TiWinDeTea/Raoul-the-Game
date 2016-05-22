package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

/**
 * The type StaticEntity.
 *
 * @author Maxime PINARD.
 */
public class StaticEntity extends Entity {
	private StaticEntityType staticEntityType;
	private boolean animated;
	private Animation animation;

	/**
	 * Instantiates a new StaticEntity.
	 *
	 * @param staticEntityType the static entity type
	 * @param position         the position
	 * @param description      the description
	 */
	public StaticEntity(StaticEntityType staticEntityType, Vector2i position, String description) {
		super(position, description);
		this.staticEntityType = staticEntityType;
		this.imageView.setImage(staticEntityType.getImage());
		this.imageView.setViewport(new Rectangle2D(staticEntityType.getSpritesPosition().x * spriteSize.x, staticEntityType.getSpritesPosition().y * spriteSize.y, spriteSize.x, spriteSize.y));
		this.imageView.setTranslateX(0);
		this.imageView.setTranslateY(0);

		if(staticEntityType.getSpritesNumber() > 1) {
			this.animate();
		}

		getChildren().add(this.imageView);
	}

	/**
	 * Instantiates a new StaticEntity.
	 *
	 * @param staticEntityType the static entity type
	 * @param description      the description
	 */
	public StaticEntity(StaticEntityType staticEntityType, String description) {
		this(staticEntityType, new Vector2i(), description);
	}

	private void animate() {
		this.animated = true;
		this.animation = new SpriteAnimation(this.imageView, this.staticEntityType.getSpritesNumber(), this.staticEntityType.getSpritesPosition(), spriteSize, Duration.millis(this.staticEntityType.getAnimationSpeed()));
		this.animation.setCycleCount(Animation.INDEFINITE);
		this.animation.play();
	}

	/**
	 * Determines if the entity is animated.
	 *
	 * @return true if animated, false otherwise
	 */
	public boolean isAnimated() {
		return this.animated;
	}

	@Override
	public boolean isVisibleOnFog() {
		return true;
	}
}
