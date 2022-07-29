package com.jacques.monopoly.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jacques.visualelements.MonopolyMaker;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
		config.title = "Monopoly Maker";
		config.useGL30 = false;
		config.width = MonopolyMaker.FRAME_WIDTH;
		config.height = MonopolyMaker.FRAME_HEIGHT;
		config.samples = 10;
		//config.addIcon("core/assets/gameboardicon.png", Files.FileType.Internal);
		new LwjglApplication(new MonopolyMaker(), config);
	}
}