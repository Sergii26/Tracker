package com.practice.placetracker.ui.arch;

import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;

import androidx.annotation.CallSuper;
import io.reactivex.disposables.CompositeDisposable;

/**
 * minimal implementation of presenter
 *
 * @param <V> - interface of activity/fragment/service/task
 */

public abstract class MvpPresenter<V extends Contract.View> implements Contract.Presenter<V> {
    private final ILog logger = Logger.withTag("MyLog");
    protected final CompositeDisposable onStopDisposable = new CompositeDisposable();
    protected final CompositeDisposable onDestroyDisposable = new CompositeDisposable();

    /**
     * link to activity/fragment/service/task
     */
    protected V view;

    @Override
    @CallSuper
    public final void subscribe(V view) {
        logger.log("MvpPresenter subscribe(v View)");
        this.view = view;
    }

    @Override
    @CallSuper
    public void unsubscribe() {
        logger.log("MvpPresenter unsubscribe view == null");
        onStopDisposable.clear();
        view = null;
    }

    protected boolean hasView() {
        return view != null;
    }

    @Override
    @CallSuper
    public void destroy() {
        onDestroyDisposable.clear();
    }

}
