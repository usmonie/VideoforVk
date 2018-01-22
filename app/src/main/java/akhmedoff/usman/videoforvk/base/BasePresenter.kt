package akhmedoff.usman.videoforvk.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle

abstract class BasePresenter<V : BaseContract.View> : LifecycleObserver, BaseContract.Presenter<V> {

    var state: Bundle? = null

    override var view: V? = null

    override val isViewAttached: Boolean
        get() = view != null

    override fun attachLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    override fun detachLifecycle(lifecycle: Lifecycle) {
        lifecycle.removeObserver(this)
    }

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun onPresenterDestroy() {
        if (state != null && !state!!.isEmpty) {
            state!!.clear()
        }
    }

    override fun getStateBundle(): Bundle {
        if (state == null) {
            state = Bundle()

        }
        return state!!
    }

    override fun onPresenterCreated() {
    }
}