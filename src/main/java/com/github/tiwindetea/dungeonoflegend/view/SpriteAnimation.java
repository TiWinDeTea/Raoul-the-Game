package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * The type SpriteAnimation.
 *
 * @author Maxime PINARD
 */
public class SpriteAnimation extends Transition {
	private final ImageView imageView;
	private final int numberOfSprites;
	private final Vector2i spritesSize;
	private final Vector2i spritesPosition;

	private int actualSprite;

	/**
	 * Instantiates a new SpriteAnimation.
	 *
	 * @param imageView       the image view
	 * @param numberOfSprites the number of sprites
	 * @param spritesPosition the sprites position
	 * @param spritesSize     the sprites size
	 * @param duration        the duration
	 */
	public SpriteAnimation(ImageView imageView, int numberOfSprites, Vector2i spritesPosition, Vector2i spritesSize, Duration duration) {
		this.imageView = imageView;
		this.numberOfSprites = numberOfSprites;
		this.spritesSize = spritesSize;
		this.spritesPosition = spritesPosition;
		setCycleDuration(duration);
		setInterpolator(Interpolator.LINEAR);
	}

	protected void interpolate(double k) {
		final int newSprite = (int) Math.floor(k * this.numberOfSprites) % this.numberOfSprites;
		if(newSprite != this.actualSprite) {
			this.actualSprite = newSprite;
			this.imageView.setViewport(new Rectangle2D((this.spritesPosition.x + this.actualSprite) * this.spritesSize.x, this.spritesPosition.y * this.spritesSize.y, this.spritesSize.x, this.spritesSize.y));
		}
	}
}

