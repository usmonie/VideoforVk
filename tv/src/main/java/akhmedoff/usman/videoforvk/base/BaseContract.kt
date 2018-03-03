package akhmedoff.usman.videoforvk.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle

interface BaseContract {

    interface View : LifecycleOwner

    interface Presenter<V : BaseContract.View> {

        val state: Bundle

        var view: V?

        val isViewAttached: Boolean

        fun attachLifecycle(lifecycle: Lifecycle)
        fun detachLifecycle(lifecycle: Lifecycle)

        fun attachView(view: V)
        fun detachView()

        fun onPresenterCreated()
        fun onPresenterDestroy()

    }
}