package com.practice.placetracker.ui.arch.progress;

public interface ProgressContainer {
    boolean hasProgress();

    void showProgress(String title, String message);

    void showProgress(int titleRes, int messageRes);

    void showProgress();

    void hideProgress();

}
