package com.github.tiwindetea.dungeonoflegend.view;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Created by maxime on 5/5/16.
 */
public class RectangleSizeTransition extends Transition {

	private final Rectangle rectangle;
	private final double initialWidth;
	private final double finalWidth;

	public RectangleSizeTransition(Rectangle rectangle, double finalWidth, Duration duration) {
		this.rectangle = rectangle;
		this.initialWidth = this.rectangle.getWidth();
		this.finalWidth = finalWidth;
		setCycleDuration(duration);
		setInterpolator(Interpolator.LINEAR);
		setCycleCount(1);
	}

	@Override
	protected void interpolate(double frac) {
		this.rectangle.setWidth(this.initialWidth + (this.finalWidth - this.initialWidth) * frac);
	}
}
