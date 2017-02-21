package com.github.tiwindetea.raoulthegame.view;

import com.github.tiwindetea.raoulthegame.view.entities.SpellType;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The type SpellDisplayer.
 *
 * @author Maxime PINARD
 */
public class SpellDisplayer extends Parent {

	private static final Color SPELLS_COOLDOWN_MASK_COLOR = Color.rgb(194, 194, 194, 0.5);

	private ImageView spellImageView = new ImageView();
	private Rectangle cooldownRectangle = new Rectangle();

	private String description = "";
	private int baseCooldown = 0;
	private int cooldown = 0;

	/**
	 * Instantiates a new SpellDisplayer.
	 */
	public SpellDisplayer() {

		setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.setText(SpellDisplayer.this.description);
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.clear();
			}
		});

		getChildren().add(this.spellImageView);

		getChildren().add(this.cooldownRectangle);
		this.cooldownRectangle.xProperty().bind(this.spellImageView.xProperty());
		this.cooldownRectangle.yProperty().bind(this.spellImageView.yProperty()
		  .add(ViewPackage.SPRITES_SIZE.y)
		  .subtract(this.cooldownRectangle.heightProperty()));
		this.cooldownRectangle.widthProperty().setValue(ViewPackage.SPRITES_SIZE.x);
		this.cooldownRectangle.setFill(SPELLS_COOLDOWN_MASK_COLOR);
	}

	private void updateColldown() {
		if(this.baseCooldown > 0 && this.cooldown > 0 && this.cooldown < this.baseCooldown) {
			this.cooldownRectangle.heightProperty()
			  .setValue(this.cooldown / (double) this.baseCooldown * ViewPackage.SPRITES_SIZE.y);
		}
	}

	/**
	 * Sets spell type.
	 *
	 * @param spellType the spell type
	 */
	void setSpellType(SpellType spellType) {
		this.spellImageView.setImage(spellType.getImage());
		this.spellImageView.setViewport(new Rectangle2D(spellType.getSpritePosition().x * ViewPackage.SPRITES_SIZE.x,
		  spellType.getSpritePosition().y * ViewPackage.SPRITES_SIZE.y,
		  ViewPackage.SPRITES_SIZE.x,
		  ViewPackage.SPRITES_SIZE.y));
	}

	/**
	 * Sets description.
	 *
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets base cooldown.
	 *
	 * @param baseCooldown the base cooldown
	 */
	public void setBaseCooldown(int baseCooldown) {
		this.baseCooldown = baseCooldown;
		updateColldown();
	}

	/**
	 * Sets cooldown.
	 *
	 * @param cooldown the cooldown
	 */
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
		updateColldown();
	}

}
