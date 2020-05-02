package com.practice.placetracker.ui.initial;

import com.practice.placetracker.App;
import com.practice.placetracker.model.logger.Logger;

class InitialFragmentInjector {

    static InitialContract.Presenter injectPresenter() {
        return new InitialPresenter(App.getInstance().getAppComponent().getPrefs(), Logger.withTag("MyLog"));
    }
}

