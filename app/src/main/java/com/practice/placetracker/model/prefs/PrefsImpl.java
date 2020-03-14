package com.practice.placetracker.model.prefs;

import android.content.Context;

public class PrefsImpl extends BasicPrefsImpl implements Prefs {

    private static final String KEY_EMAIL = "email";

    public PrefsImpl(Context ctx) {
        super(ctx);
    }

    @Override
    public String getDefaultPrefsFileName() {
        return "tracker";
    }

    @Override
    public void putEmail(String email) {
        put(KEY_EMAIL, email);
    }

    @Override
    public String getEmail() {
        return get(KEY_EMAIL, "");
    }
}

