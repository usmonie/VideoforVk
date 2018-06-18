package akhmedoff.usman.videoforvk.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.DialogFragment
import android.view.View

abstract class BaseDialogFragment<V : BaseContract.View, P : BaseContract.Presenter<V>> :
    DialogFragment(),
    BaseContract.View {

    protected lateinit var presenter: P

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: BaseViewModel<V, P> = getViewModel()

        var isPresenterCreated = false

        if (viewModel.getPresenter() == null) {
            viewModel.setPresenter(initPresenter())
            isPresenterCreated = true
        }

        viewModel.getPresenter()?.let { presenter = it }

        presenter.attachLifecycle(lifecycle)
        presenter.attachView(this as V)

        if (isPresenterCreated) presenter.onPresenterCreated()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachLifecycle(lifecycle)
        presenter.detachView()
    }

    protected abstract fun initPresenter(): P

    private inline fun <reified T : ViewModel> getViewModel() = ViewModelProviders
        .of(this)
        .get(T::class.java)
}