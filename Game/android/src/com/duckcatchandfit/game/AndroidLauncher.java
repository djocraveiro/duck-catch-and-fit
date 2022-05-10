package com.duckcatchandfit.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.duckcatchandfit.game.GameEngine;

public class AndroidLauncher extends AndroidApplication {

	//#region Public Methods

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GameEngine(), config);
	}

	//#enregion
}
