package com.duckcatchandfit.game.commands;

import android.content.Intent;
import com.duckcatchandfit.game.ICommand;
import com.duckcatchandfit.game.models.ActivityReading;

public class CommandIntentHelper {

    //#region Fields

    public static final String Action = "com.duckcatchandfit.game.COMMAND";
    private static final String commandValueKey = "command-value";
    private static final String FireLaseValue = "fire_laser";

    private static final ICommand MoveLeftCommand = new MoveLeftCommand();
    private static final ICommand MoveRightCommand = new MoveRightCommand();
    private static final ICommand FireLaserCommand = new FireLaserCommand();

    //#endregion

    //#region Public Methods

    public static Intent createIntent(String value) {
        Intent intent = new Intent(Action);
        intent.putExtra(commandValueKey, value);

        return intent;
    }

    public static ICommand readCommand(Intent intent) {
        String value = intent.getStringExtra(commandValueKey);

        return readCommand(value);
    }

    public static ICommand readCommand(String value) {
        switch (value) {
            case ActivityReading.JUMP_LEFT:
                return MoveLeftCommand;

            case ActivityReading.JUMP_RIGHT:
                return MoveRightCommand;

            case FireLaseValue:
                return FireLaserCommand;

            default:
                return null;
        }
    }

    //#endregion
}
