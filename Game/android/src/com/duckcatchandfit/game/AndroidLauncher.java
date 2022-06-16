package com.duckcatchandfit.game;

import android.os.Bundle;

import android.view.WindowManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	//#region Fields

	private DuckCatchAndFitGame game;
	private GameControllerAndroid gameController;

	//#endregion

	//#region Public Methods

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		gameController = new GameControllerAndroid(getContext());
		game = new DuckCatchAndFitGame(gameController);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game, config);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		game.dispose();
		gameController.dispose();
	}

	//#enregion
}
