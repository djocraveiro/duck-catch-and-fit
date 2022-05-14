package com.duckcatchandfit.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	//#region Public Methods

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(360, 640);
		config.setForegroundFPS(60);
		config.setTitle("Duck Catch and Fit");

		new Lwjgl3Application(new DuckCatchAndFitGame(), config);
	}

	//#endregion
}
