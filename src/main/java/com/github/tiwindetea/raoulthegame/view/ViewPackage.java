//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.view;

import com.github.tiwindetea.raoulthegame.model.MainPackage;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by maxime on 5/5/16.
 */
public class ViewPackage {
	public static final String NAME = "com.github.tiwindetea.raoulthegame";
	public static final String PATH = NAME.replace('.', '/');
	private final static String BUNDLE_NAME = NAME + ".ViewPackage";
	public final static ResourceBundle VIEW_PACKAGE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	public final static Image OBJECTS_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("objects.file"));
	public final static Image PLAYERS_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("players.file"));
	public final static Image MOBS_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("mobs.file"));
	public static final Image ICON_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("icon.file"));
	public static final Image HUD_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("playerhud.file"));
	public static final Image INVENTORY_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("playerinventory.file"));
	public static final Image DIALOG_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("dialogimage.file"));
	public final static Image SPELLS_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("spells.file"));

	public static final Image SOLO_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("menu.solo.file"));
	public static final Image MULTIPLAYER_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("menu.multiplayer.file"));
	public static final Image LOAD_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("menu.load.file"));
	public static final Image RESUME_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("menu.resume.file"));
	public static final Image GREEN_LOGO_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("menu.logo.green.file"));
	public static final Image ORANGE_LOGO_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("menu.logo.orange.file"));
	public static final Image TUTORIAL_1_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("tuto.instructions.file"));
	public static final Image TUTORIAL_2_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("tuto.illustration.file"));
	public static final Image CREDITS_IMAGE = new Image(PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("credits.file"));

	public static final Vector2i SPRITES_SIZE = new Vector2i(Integer.parseInt(VIEW_PACKAGE_BUNDLE.getString("sprites.size.x")), Integer.parseInt(VIEW_PACKAGE_BUNDLE.getString("sprites.size.y")));

	public static final String BUTTONS_STYLE_FILE_PATH = PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("menu.buttons.style.file");

	private final static String TILES_BUNDLE_NAME = MainPackage.name + ".Tile";
	private final static ResourceBundle TILES_RESOURCE_BUNDLE = ResourceBundle.getBundle(TILES_BUNDLE_NAME);
	public static final Vector2i FOG_SPRITE_POSITION = new Vector2i(Integer.parseInt(TILES_RESOURCE_BUNDLE.getString("fog.sprite.position.x")),
	  Integer.parseInt(TILES_RESOURCE_BUNDLE.getString("fog.sprite.position.y")));

	public static final String MAIN_FONT_PATH = "/" + PATH + "/" + VIEW_PACKAGE_BUNDLE.getString("font.mainfont.file");


	private final static String LET_bundleName = MainPackage.name + ".LivingEntity";
	public final static ResourceBundle LET_resourceBundle = ResourceBundle.getBundle(LET_bundleName, Locale.getDefault());

	private final static String ST_bundleName = MainPackage.name + ".Spell";
	public final static ResourceBundle ST_resourceBundle = ResourceBundle.getBundle(ST_bundleName, Locale.getDefault());

	/**
	 * Gets main font.
	 *
	 * @param fontSize the font size
	 * @return the main font
	 */
	public static Font getMainFont(int fontSize) {
		return Font.loadFont(GUI.class.getResource(MAIN_FONT_PATH).toExternalForm(), fontSize);
	}
}
