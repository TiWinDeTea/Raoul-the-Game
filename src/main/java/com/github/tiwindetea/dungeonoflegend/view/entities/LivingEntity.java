package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.Direction;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.geometry.Rectangle2D;

/**
 * The type LivingEntity.
 *
 * @author Maxime PINARD.
 */
public class LivingEntity extends Entity {
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
		this.imageView.translateXProperty().bind(this.XPositionProperty);
		this.imageView.translateYProperty().bind(this.YPositionProperty);
		this.setDirection(direction);

		getChildren().add(this.imageView);
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
