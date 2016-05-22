package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.Direction;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.RectangleSizeTransition;
import com.github.tiwindetea.dungeonoflegend.view.ViewPackage;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * The type LivingEntity.
 *
 * @author Maxime PINARD.
 */
public class LivingEntity extends Entity {
	private static final double HEALTH_RECTANGLES_HEIGHT = 1;
	private static final double HEALTH_RECTANGLES_WIDTH_RATIO = 0.8;
	private static final double HEALTH_RECTANGLES_WIDTH = HEALTH_RECTANGLES_WIDTH_RATIO * ViewPackage.spritesSize.x;
	private static final Color MAX_HEALTH_RECTANGLE_COLOR = Color.DARKRED;
	private static final Color ACTUAL_HEALTH_RECTANGLE_COLOR = Color.RED;
	private static final Duration ANIMATION_DURATION = Duration.millis(500);

	private final Rectangle maxHealthRectangle = new Rectangle(HEALTH_RECTANGLES_WIDTH, HEALTH_RECTANGLES_HEIGHT,MAX_HEALTH_RECTANGLE_COLOR);
	private final Rectangle actualHealthRectangle = new Rectangle(HEALTH_RECTANGLES_WIDTH, HEALTH_RECTANGLES_HEIGHT,ACTUAL_HEALTH_RECTANGLE_COLOR);

	private double healthProportion = 1;
	private LivingEntityType livingEntityType;
	private Direction direction;

	/**
	 * Instantiates a new LivingEntity.
	 *
	 * @param livingEntityType the living entity type
	 * @param position         the position
	 * @param direction        the direction
	 * @param description      the description
	 */
	public LivingEntity(LivingEntityType livingEntityType, Vector2i position, Direction direction, String description) {
		super(position, description);
		this.livingEntityType = livingEntityType;
		this.imageView.setImage(livingEntityType.getImage());
		this.imageView.setTranslateX(0);
		this.imageView.setTranslateY(0);
		this.setDirection(direction);

		getChildren().add(this.imageView);

		maxHealthRectangle.setTranslateX((ViewPackage.spritesSize.x - HEALTH_RECTANGLES_WIDTH) / 2);
		maxHealthRectangle.setTranslateY(ViewPackage.spritesSize.y);
		actualHealthRectangle.setTranslateX((ViewPackage.spritesSize.x - HEALTH_RECTANGLES_WIDTH) / 2);
		actualHealthRectangle.setTranslateY(ViewPackage.spritesSize.y);

		getChildren().add(maxHealthRectangle);
		getChildren().add(actualHealthRectangle);
	}

	/**
	 * Sets position and direction.
	 * Use super class function to only set position
	 *
	 * @param position  the position
	 * @param direction the direction
	 */
	public void setPosition(Vector2i position, Direction direction) {
		this.setPosition(position);
		this.setDirection(direction);
	}

	/**
	 * Sets direction.
	 *
	 * @param direction the direction
	 */
	public void setDirection(Direction direction) {
		if(direction != this.direction) {
			this.direction = direction;
			Vector2i spritePosition = this.livingEntityType.getSpritePosition(this.direction);
			this.imageView.setViewport(new Rectangle2D(spritePosition.x * spriteSize.x, spritePosition.y * spriteSize.y, spriteSize.x, spriteSize.y));
		}
	}

	private void updateHealth() {
		RectangleSizeTransition transition = new RectangleSizeTransition(this.actualHealthRectangle, healthProportion * this.maxHealthRectangle.getWidth(), ANIMATION_DURATION);
		transition.play();
	}

	public double getHealthProportion() {
		return healthProportion;
	}

	public void setHealthProportion(double healthProportion) {
		this.healthProportion = healthProportion;
		updateHealth();
	}

	public void setHealthBarVisible(boolean visibility){
		actualHealthRectangle.setVisible(visibility);
		maxHealthRectangle.setVisible(visibility);
	}

	/**
	 * Gets direction.
	 *
	 * @return the direction
	 */
	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public boolean isVisibleOnFog() {
		return false;
	}
}
