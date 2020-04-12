package com.practice.placetracker.ui.arch.fragments;

import android.content.Context;
import android.widget.Toast;

import com.practice.placetracker.R;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.ui.arch.Contract;

import java.lang.reflect.ParameterizedType;

import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;

public abstract class MvpFragment <ControllerType extends Contract.Presenter, Host extends Contract.Host>
        extends Fragment implements Contract.View {
    protected final CompositeDisposable onStopDisposables = new CompositeDisposable();
    protected final CompositeDisposable onDestroyDisposables = new CompositeDisposable();
    /**
     * the fragment callBack
     */
    private Host callBack;

    //@Override
    public final boolean hasCallBack() {
        return callBack != null;
    }

    public final boolean noHost() {
        return callBack == null;
    }

    /**
     * get the current fragment call back
     *
     * @return the current fragment call back
     */
    public final Host getCallBack() {
        return callBack;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // keep the call back
        try {
            this.callBack = (Host) context;
        } catch (Throwable e) {
            final String hostClassName = ((Class) ((ParameterizedType) getClass().
                    getGenericSuperclass())
                    .getActualTypeArguments()[1]).getCanonicalName();
            throw new RuntimeException("Activity must implement " + hostClassName
                    + " to attach " + this.getClass().getSimpleName(), e);
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
        final ControllerType controllerType = getPresenter();
        if (controllerType != null) {
            controllerType.subscribe(this);
        }
    }

    @Override
    public void onStop() {
        final ControllerType controllerType = getPresenter();
        if (controllerType != null) {
            controllerType.unsubscribe();
        }
        onStopDisposables.clear();
        hideProgress();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        final ControllerType controllerType = getPresenter();
        if (controllerType != null) {
            controllerType.destroy();
        }
        onDestroyDisposables.clear();
        super.onDestroy();
    }

    protected abstract ControllerType getPresenter();

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
        if (hasCallBack() && getCallBack().hasProgress()) {
            getCallBack().showProgress(R.string.empty, R.string.empty);
        }
    }

    @Override
    public void showProgress(String title, String message) {
        if (hasCallBack() && getCallBack().hasProgress()) {
            getCallBack().showProgress(title, message);
        }
    }

    @Override
    public void hideProgress() {
        if (hasCallBack() && getCallBack().hasProgress()) {
            getCallBack().hideProgress();
        }
    }

}
