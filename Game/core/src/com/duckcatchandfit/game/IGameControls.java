package com.duckcatchandfit.game;

import com.duckcatchandfit.game.players.IPlayerControls;

public interface IGameControls {

    float getScreenWidth();

    IPlayerControls getPlayerControls();
}
