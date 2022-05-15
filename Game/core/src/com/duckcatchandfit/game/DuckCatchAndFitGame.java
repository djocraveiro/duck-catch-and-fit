package com.duckcatchandfit.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.duckcatchandfit.game.screens.GameOverScreen;
import com.duckcatchandfit.game.screens.GameScreen;
import com.duckcatchandfit.game.screens.TitleScreen;

public class DuckCatchAndFitGame extends Game implements IGameNavigation {

	//#region Fields

	private Screen screen;

	//#endregion

	//#region Public Methods

	@Override
	public void create() {
		screen = new TitleScreen(this);
		setScreen(screen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		screen.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
		screen.dispose();
	}

	//#region IGameNavigation

	@Override
	public void ShowGameScreen() {
		screen = new GameScreen(this);
		setScreen(screen);
	}

	@Override
	public void ShowGameOverScreen(int score) {
		screen = new GameOverScreen(this, score);
		setScreen(screen);
	}

	//#enregion

	//#endregion
}
