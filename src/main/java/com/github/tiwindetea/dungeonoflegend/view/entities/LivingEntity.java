package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.Direction;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.geometry.Rectangle2D;

/**
 * Created by maxime on 5/6/16.
 */
public class LivingEntity extends Entity {
	private LivingEntityType livingEntityType;
	private Direction direction;

	public LivingEntity(LivingEntityType livingEntityType, Vector2i position, Direction direction, String description) {
		super(description);
		this.livingEntityType = livingEntityType;
		this.setPosition(position);
		this.imageView.setImage(livingEntityType.getImage());
		this.setDirection(direction);

		getChildren().add(this.imageView);
	}

	public void setPosition(Vector2i position, Direction direction) {
		this.setPosition(position);
		this.setDirection(direction);
	}

	@Override
	public void setPosition(Vector2i position) {
		this.position = new Vector2i(position);
		this.imageView.setTranslateX(position.x * spriteSize.x);
		this.imageView.setTranslateY(position.y * spriteSize.y);
	}

	public void setDirection(Direction direction) {
		if(direction != this.direction) {
			this.direction = direction;
			Vector2i spritePosition = this.livingEntityType.getSpritePosition(this.direction);
			this.imageView.setViewport(new Rectangle2D(spritePosition.x * spriteSize.x, spritePosition.y * spriteSize.y, spriteSize.x, spriteSize.y));
		}
	}

	public Direction getDirection() {
		return this.direction;
	}
}
