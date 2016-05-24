package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.ViewPackage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
	public StaticEntity(StaticEntityType staticEntityType, String description, long id) {
		this(staticEntityType, new Vector2i(), description);

		setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("Drag start");
				Dragboard db = StaticEntity.this.startDragAndDrop(TransferMode.COPY);
				ClipboardContent content = new ClipboardContent();
				content.putString(Long.toString(id));
				db.setContent(content);
				event.consume();
			}
		});
	}

	private void animate() {
		this.animated = true;

		final EventHandler<ActionEvent> eventExecutor = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				StaticEntity.this.actualSpriteNumber = (StaticEntity.this.actualSpriteNumber + 1) % StaticEntity.this.spritesNumber;
				StaticEntity.this.imageView.setViewport(new Rectangle2D((StaticEntity.this.staticEntityType.getSpritesPosition().x + StaticEntity.this.actualSpriteNumber) * ViewPackage.spritesSize.x, StaticEntity.this.staticEntityType.getSpritesPosition().y * ViewPackage.spritesSize.y, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y));
			}
		};
		final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(this.staticEntityType.getAnimationSpeed()), eventExecutor));
		this.spritesNumber = this.staticEntityType.getSpritesNumber();
		this.actualSpriteNumber = 0;
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
