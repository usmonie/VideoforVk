package akhmedoff.usman.videoforvk.ui.base

import android.arch.lifecycle.ViewModel

class BaseViewModel<V : BaseContract.View, P : BaseContract.Presenter<V>> : ViewModel() {

    private var presenter: P? = null

    fun getPresenter() = presenter

    fun setPresenter(presenter: P) {
        if (this.presenter == null) this.presenter = presenter
    }

    override fun onCleared() {
        super.onCleared()
        presenter?.onPresenterDestroy()
        presenter = null
    }
}