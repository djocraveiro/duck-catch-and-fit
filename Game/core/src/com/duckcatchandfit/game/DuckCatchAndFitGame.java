package com.duckcatchandfit.game;

import com.badlogic.gdx.Game;

public class DuckCatchAndFitGame extends Game {

	//#region Fields

	private GameScreen gameScreen;

	//#endregion

	//#region Public Methods

	@Override
	public void create() {
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
		gameScreen.dispose();
	}

	//#endregion
}
