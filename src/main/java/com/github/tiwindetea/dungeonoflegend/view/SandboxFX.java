package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SandboxFX extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {

		/*EntityType ent = EntityType.PLAYER1;

		final ImageView imageView = new ImageView(ent.getImage());
		imageView.setViewport(new Rectangle2D(ent.getSpritesPosition().x, ent.getSpritesPosition().y, Integer.parseInt(MainPackage.spriteSheetBundle.getString("xresolution")), Integer.parseInt(MainPackage.spriteSheetBundle.getString("yresolution"))));

		final Animation animation = new SpriteAnimation(imageView, ent.getSpritesNumber(), ent.getSpritesPosition(), new Vector2i(Integer.parseInt(MainPackage.spriteSheetBundle.getString("xresolution")), Integer.parseInt(MainPackage.spriteSheetBundle.getString("yresolution"))), Duration.millis(500));
		animation.setCycleCount(Animation.INDEFINITE);
		animation.play();

		primaryStage.setScene(new Scene(new Group(imageView)));
		primaryStage.show();*/

		Entity ent = new Entity(EntityType.SUPER_POT, new Vector2i());
		primaryStage.setScene(new Scene(new Group(ent.getImageView())));
		primaryStage.show();
	}
}