package com.duckcatchandfit.game.commands;

import com.duckcatchandfit.game.ICommand;
import com.duckcatchandfit.game.players.IPlayerControls;

public class FireLaserCommand implements ICommand {

    @Override
    public void apply(IPlayerControls playerControls) {
        playerControls.fireLaser();
    }
}
