package com.practice.placetracker.ui.arch.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.practice.placetracker.R;
import com.practice.placetracker.ui.arch.Contract;

import java.lang.reflect.ParameterizedType;

public abstract class MvpDialogFragment<Presenter extends Contract.Presenter, Host extends Contract.Host>
        extends AppCompatDialogFragment implements Contract.View {
    /**
     * the fragment callBack
     */
    private Host callBack;

    /**
     * sometimes dialogs are dismissed after onPause, so transaction can't be saved an fragment
     * will be shown once more. This variable prohibits such cases
     */
    private boolean isDismissed = false;

    public final boolean hasCallBack() {
        return callBack != null;
    }

    /**
     * get the current fragment call back
     *
     * @return the current fragment call back
     */
    protected final Host getCallBack() {
        return callBack;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // keep the call back
        try {
            this.callBack = (Host) context;
        } catch (Throwable e) {
            final String hostClassName = ((Class) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[1]).getCanonicalName();
            throw new RuntimeException("Activity must implement " + hostClassName
                    + " to attach " + getClass().getSimpleName(), e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // release the call back
        this.callBack = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribePresenter();
    }

    protected void subscribePresenter() {
        final Presenter presenter = getController();
        if (presenter != null) {
            presenter.subscribe(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDismissed && getDialog() != null) {
            getDialog().dismiss();
        }
    }

    @Override
    public void onStop() {
        final Presenter presenter = getController();
        if (presenter != null) {
            presenter.unsubscribe();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        destroy();
        super.onDestroy();
    }

    @CallSuper
    public void destroy() {
        final Presenter presenter = getController();
        if (presenter != null) {
            presenter.destroy();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        isDismissed = true;
        super.onDismiss(dialog);
    }

    @Override
    public void dismissAllowingStateLoss() {
        isDismissed = true;
        super.dismissAllowingStateLoss();
    }

    @Nullable
    protected abstract Presenter getController();

    public boolean isShowing() {
        final Dialog dialog = getDialog();
        return dialog != null && dialog.isShowing();
    }


    @Override
    public void showToast(int strResId) {
        Toast.makeText(getActivity(), strResId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        if (hasCallBack()) {
            getCallBack().showProgress(R.string.empty, R.string.empty);
        }
    }

    @Override
    public void showProgress(String title, String message) {
        if (hasCallBack()) {
            getCallBack().showProgress(title, message);
        }
    }

    @Override
    public void hideProgress() {
        if (hasCallBack()) {
            getCallBack().hideProgress();
        }
    }

}
