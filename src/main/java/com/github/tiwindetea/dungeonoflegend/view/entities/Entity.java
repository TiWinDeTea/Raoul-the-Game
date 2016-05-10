package com.github.tiwindetea.dungeonoflegend.view.entities;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

/**
 * Created by maxime on 5/6/16.
 */
public abstract class Entity extends Parent {
	protected static final Vector2i spriteSize = new Vector2i(Integer.parseInt(MainPackage.spriteSheetBundle.getString("xresolution")), Integer.parseInt(MainPackage.spriteSheetBundle.getString("yresolution")));
	protected ImageView imageView = new ImageView();
	protected Vector2i position;
	protected boolean[][] LOS;

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

	public abstract void setPosition(Vector2i position);
}
