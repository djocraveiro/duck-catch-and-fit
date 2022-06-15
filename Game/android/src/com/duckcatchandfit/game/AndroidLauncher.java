package com.duckcatchandfit.game;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.speech.RecognizerIntent;
import android.widget.Toast;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.duckcatchandfit.game.commands.CommandIntentHelper;
import com.duckcatchandfit.game.utils.ApplicationExecutors;

import java.util.List;

public class AndroidLauncher extends AndroidApplication {

	//#region Fields

	private DuckCatchAndFitGame game;
	private ActionResolverAndroid actionResolver;
	private final ApplicationExecutors exec = new ApplicationExecutors();

	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ICommand command = CommandIntentHelper.readCommand(intent);
			if (command != null) {
				exec.getMainThread().execute(() -> {
					game.sendCommand(command);
				});
			}
		}
	};

	//#endregion

	//#region Public Methods

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionResolver = new ActionResolverAndroid(getContext());
		game = new DuckCatchAndFitGame(actionResolver);

		actionResolver.setGameControls(game);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game, config);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter(CommandIntentHelper.Action));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		game.dispose();
		actionResolver.dispose();
	}

	//#enregion

	//#region Private Methods

	public static final int REQUEST_SPEECH = 1;
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_SPEECH && resultCode == RESULT_OK) {
			// Get the spoken sentence..
			List<String> thingsYouSaid =
					data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			Toast.makeText(getContext(), thingsYouSaid.get(0), Toast.LENGTH_SHORT).show();
		}
	}

	//#endregion
}
