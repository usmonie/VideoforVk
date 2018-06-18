package akhmedoff.usman.videoforvk.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity<V : BaseContract.View, P : BaseContract.Presenter<V>> :
        AppCompatActivity(), BaseContract.View {

    protected lateinit var presenter: P

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    override fun onDestroy() {
        super.onDestroy()
        presenter.detachLifecycle(lifecycle)
        presenter.detachView()
    }

    protected abstract fun initPresenter(): P

    private inline fun <reified T : ViewModel> getViewModel() =
            ViewModelProviders
                    .of(this)
                    .get(T::class.java)
}