package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.MainPackage;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.scene.image.Image;

import java.util.ResourceBundle;

/**
 * Created by maxime on 5/5/16.
 */
public class ViewPackage {
	public static final String name = "com.github.tiwindetea.dungeonoflegend";
	public static final String path = name.replace('.', '/');
	private final static String bundleName = name + ".SpriteSheet";
	public final static ResourceBundle spriteSheetBundle = ResourceBundle.getBundle(bundleName);
	public final static Image objectsImage = new Image(path + "/" + spriteSheetBundle.getString("objects.file"));
	public final static Image playersImage = new Image(path + "/" + spriteSheetBundle.getString("players.file"));
	//public final static Image enemiesImage = new Image(path + "/" + spriteSheetBundle.getString("enemies.file"));

	public static final Vector2i spritesSize = new Vector2i(Integer.parseInt(MainPackage.spriteSheetBundle.getString("xresolution")), Integer.parseInt(MainPackage.spriteSheetBundle.getString("yresolution")));
}
