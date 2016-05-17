package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.InformationsDisplayer;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class Entity extends Parent {
	protected static final Vector2i spriteSize = new Vector2i(Integer.parseInt(MainPackage.spriteSheetBundle.getString("xresolution")), Integer.parseInt(MainPackage.spriteSheetBundle.getString("yresolution")));
	protected ImageView imageView = new ImageView();
	protected Vector2i position;
	protected boolean[][] LOS;

	private String description;

	public Entity(String description) {
		this.description = description;

		this.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.setText(description);
			}
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				InformationsDisplayer.clear();
			}
		});
	}

	public ImageView getImageView() {
		return this.imageView;
	}

	public Vector2i getPosition() {
		return this.position;
	}

	public void setLOS(boolean[][] LOS) {
		if(LOS == null || LOS.length == 0)
			return;
		if((LOS.length & 1) == 0) {
			System.out.println("Warning: LOS width not odd: "+LOS.length);
		}
		if((LOS[0].length & 1) == 0) {
			System.out.println("Warning: LOS height not odd: "+LOS[0].length);
		}
		this.LOS = LOS;
	}

	public boolean[][] getLOS() {
		return this.LOS;
	}

	public void modifieLOS(List<Vector2i> modifiedTilesPositions) {
		if(this.LOS != null && this.LOS.length > 0) {
			for(Vector2i modifiedTilesPosition : modifiedTilesPositions) {
				if(((modifiedTilesPosition.x >= 0) && (modifiedTilesPosition.x < this.LOS.length)) && ((modifiedTilesPosition.y >= 0) && (modifiedTilesPosition.y >= this.LOS[0].length))) {
					this.LOS[modifiedTilesPosition.x][modifiedTilesPosition.y] = !this.LOS[modifiedTilesPosition.x][modifiedTilesPosition.y];
				}
			}
		}
	}

	public abstract void setPosition(Vector2i position);
}
