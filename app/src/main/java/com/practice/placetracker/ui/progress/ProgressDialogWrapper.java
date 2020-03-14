package com.practice.placetracker.ui.progress;

import com.practice.placetracker.ui.arch.progress.ProgressDecor;

import androidx.fragment.app.FragmentActivity;

public class ProgressDialogWrapper implements ProgressDecor {
    private final ProgressFragment progressView;
    private final FragmentActivity context;

    public ProgressDialogWrapper(FragmentActivity context, String title, String message) {
        progressView = ProgressFragment.newInstance(title, message);
        this.context = context;
    }

    public ProgressDialogWrapper(FragmentActivity context, int title, int message) {
        progressView = ProgressFragment.newInstance(title, message);
        this.context = context;
    }

    public ProgressDialogWrapper(FragmentActivity context) {
        progressView = ProgressFragment.newInstance();
        this.context = context;
    }

    @Override
    public void show() {
        progressView.show(context.getSupportFragmentManager(), "progress");
    }

    @Override
    public boolean isShowing() {
        return progressView.isShowing();
    }

    @Override
    public void dismiss() {
        progressView.dismiss();
    }

}
