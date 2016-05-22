package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.ViewPackage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private int actualSpriteNumber;
	private int spritesNumber;

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

		final EventHandler<ActionEvent> eventExecutor = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				actualSpriteNumber = (actualSpriteNumber + 1) % spritesNumber;
				imageView.setViewport(new Rectangle2D((staticEntityType.getSpritesPosition().x + actualSpriteNumber) * ViewPackage.spritesSize.x, staticEntityType.getSpritesPosition().y * ViewPackage.spritesSize.y, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y));
			}
		};
		final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(this.staticEntityType.getAnimationSpeed()), eventExecutor));
		spritesNumber = staticEntityType.getSpritesNumber();
		actualSpriteNumber = 0;
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
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
