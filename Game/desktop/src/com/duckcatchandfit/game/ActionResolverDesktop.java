package com.duckcatchandfit.game;

import com.badlogic.gdx.Gdx;

public class ActionResolverDesktop implements IActionResolver {

    //#region Public Methods

    @Override
    public void startListeningSensors() {
        Gdx.app.log(ActionResolverDesktop.class.getName(), "startListeningSensors not implemented");
    }

    @Override
    public void stopListeningSensors() {
        Gdx.app.log(ActionResolverDesktop.class.getName(), "stopListeningSensors not implemented");
    }

    @Override
    public void showToast(final CharSequence toastMessage, final int toastDuration) {
        Gdx.app.log(ActionResolverDesktop.class.getName(), "showToast not implemented");
    }

    @Override
    public void showSpeechPopup() {
        Gdx.app.log(ActionResolverDesktop.class.getName(), "showSpeechPopup not implemented");
    }

    //#endregion
}
