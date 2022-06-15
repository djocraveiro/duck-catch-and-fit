package com.duckcatchandfit.game;

import com.duckcatchandfit.game.players.IPlayerControls;

public interface ICommand {

    void apply(IPlayerControls playerControls);
}
