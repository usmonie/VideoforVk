package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.two_factor_dialog.*

class TwoFactorAutentificationDialog(
    context: Context,
    private val listener: AuthentificatorListener
) :
    Dialog(context) {

    interface AuthentificatorListener {
        fun enterCode(code: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.two_factor_dialog)
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        enter_code_button?.setOnClickListener { listener.enterCode(code_input.text.toString()) }
    }

    fun setNumber(number: String) {
        two_factor_number?.visibility = View.VISIBLE

        two_factor_number?.text = context.resources.getString(R.string.two_factor_number, number)
    }

    fun showLoading() {
        two_factor_number?.visibility = View.GONE
        enter_code_button?.visibility = View.GONE
        code_input_layout?.visibility = View.GONE
        dialog_progress?.visibility = View.VISIBLE
        dialog_progress?.animate()
    }

    fun hideLoading() {
        two_factor_number?.visibility = View.VISIBLE
        enter_code_button?.visibility = View.VISIBLE
        code_input_layout?.visibility = View.VISIBLE

        dialog_progress?.visibility = View.GONE
        dialog_progress?.animate()
    }

    fun showErrorCode(error: String) {
        code_input_layout?.isErrorEnabled = true
        code_input_layout?.error = error
    }
}