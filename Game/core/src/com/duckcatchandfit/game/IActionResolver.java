package com.duckcatchandfit.game;

public interface IActionResolver {

    void startListeningSensors();

    void stopListeningSensors();

    void showToast(CharSequence toastMessage, int toastDuration);

    void showSpeechPopup();
}
