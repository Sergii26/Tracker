package com.practice.placetracker.ui.progress;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.practice.placetracker.R;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.ui.arch.fragments.MessageDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProgressFragment extends MessageDialog<ProgressContract.Presenter, ProgressContract.Host> implements ProgressContract.View {
    private final ILog logger = Logger.withTag("MyLog");

    public static ProgressFragment newInstance(int titleResId, int msgResId) {
        final Bundle b = new Bundle();
        b.putInt(KEY_TITLE, titleResId);
        b.putInt(KEY_MSG, msgResId);
        b.putInt(KEY_TYPE, TYPE_INT);
        final ProgressFragment f = newInstance();
        f.setArguments(b);
        return f;
    }

    public static ProgressFragment newInstance(String title, String msg) {
        final Bundle b = new Bundle();
        b.putString(KEY_TITLE_STR, title);
        b.putString(KEY_MSG_STR, msg);
        b.putInt(KEY_TYPE, TYPE_STR);
        final ProgressFragment f = newInstance();
        f.setArguments(b);
        return f;
    }

    public static ProgressFragment newInstance() {
        return new ProgressFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        logger.log("ProgressFragment in onCreate()");
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    protected ProgressContract.Presenter getController() {
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        logger.log("ProgressFragment in onCreateDialog()");
        final Bundle args = getArguments();
        final ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        if (args != null) {
            final int type = args.getInt(KEY_TYPE);
            if (TYPE_INT == type) {
                dialog.setTitle(getString(args.getInt(KEY_TITLE, R.string.empty)));
                dialog.setMessage(getString(args.getInt(KEY_MSG, R.string.empty)));
            } else if (TYPE_STR == type) {
                dialog.setTitle(args.getString(KEY_TITLE_STR, ""));
                dialog.setMessage(args.getString(KEY_MSG_STR, ""));
            }
        }
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }

}
