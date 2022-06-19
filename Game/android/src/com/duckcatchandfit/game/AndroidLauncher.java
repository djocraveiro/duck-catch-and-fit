package com.duckcatchandfit.game;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.WindowManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.ArrayList;
import java.util.List;

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

		requestRequiredPermissions();

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game, config);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == PERMISSIONS_REQUEST) {
			handleRequestPermissionsResult(permissions, grantResults);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		game.dispose();
		gameController.dispose();
	}

	//#enregion

	//#region Private Methods

	private final int PERMISSIONS_REQUEST = 1;

	private void requestRequiredPermissions() {
		List<String> permissionsToRequest = new ArrayList<>();

		if (hasPermission(Manifest.permission.RECORD_AUDIO)) {
			gameController.setVoiceEnabled(true);
		}
		else {
			permissionsToRequest.add(Manifest.permission.RECORD_AUDIO);
		}

		if (!permissionsToRequest.isEmpty()){
			String[] permissions = permissionsToRequest.toArray(new String[0]);
			requestPermissions(permissions, PERMISSIONS_REQUEST);
		}
	}

	public boolean hasPermission(String permission) {
		return  checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
	}

	private void handleRequestPermissionsResult(String[] permissions, int[] grantResults) {
		for (int i = 0; i < permissions.length; i++) {
			int grantResult = grantResults[i];

			if (permissions[i].equals(Manifest.permission.RECORD_AUDIO)
				&& grantResult == PackageManager.PERMISSION_GRANTED) {

				gameController.setVoiceEnabled(true);
			}
		}
	}

	//#endregion
}
