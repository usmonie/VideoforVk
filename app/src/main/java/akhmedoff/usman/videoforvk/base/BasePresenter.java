package akhmedoff.usman.videoforvk.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;

public abstract class BasePresenter<V extends BaseContract.View> implements LifecycleObserver, BaseContract.Presenter<V> {

    private Bundle stateBundle;
    private V view;

    @NonNull
    @Override
    final public V getView() {
        return view;
    }

    @Override
    final public void attachLifecycle(@NonNull Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @Override
    final public void detachLifecycle(@NonNull Lifecycle lifecycle) {
        lifecycle.removeObserver(this);
    }

    @Override
    final public void attachView(@NonNull V view) {
        this.view = view;
    }

    @Override
    final public void detachView() {
        view = null;
    }

    @Override
    final public boolean isViewAttached() {
        return view != null;
    }

    @NonNull
    @Override
    final public Bundle getStateBundle() {
        return stateBundle == null ? stateBundle = new Bundle() : stateBundle;
    }

    @Override
    public void onPresenterDestroy() {
        if (stateBundle != null && !stateBundle.isEmpty()) {
            stateBundle.clear();
        }
    }

    @Override
    public void onPresenterCreated() {
        //NO-OP
    }
}