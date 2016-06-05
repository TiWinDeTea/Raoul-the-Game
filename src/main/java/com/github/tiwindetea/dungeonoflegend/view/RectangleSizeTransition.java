//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.view;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * The type RectangleSizeTransition.
 *
 * @author Maxime PINARD
 */
public class RectangleSizeTransition extends Transition {

	private final Rectangle rectangle;
	private final double initialWidth;
	private final double finalWidth;

	/**
	 * Instantiates a new RectangleSizeTransition.
	 *
	 * @param rectangle  the rectangle
	 * @param finalWidth the final width
	 * @param duration   the duration
	 */
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
