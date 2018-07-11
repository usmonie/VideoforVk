package akhmedoff.usman.thevt.ui.login

import akhmedoff.usman.thevt.R
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.two_factor_dialog.*

class TwoFactorAuthenticationDialog(
        context: Context,
        private val listener: (String) -> Unit,
        cancelListener: () -> Unit
) :
        Dialog(context, false, DialogInterface.OnCancelListener { cancelListener() }) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.two_factor_dialog)
        window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        enter_code_button?.setOnClickListener { listener(code_input.text.toString()) }
        dismiss_code?.setOnClickListener { cancel() }
    }

    fun setNumber(number: String) {
        two_factor_number?.visibility = View.VISIBLE

        two_factor_number?.text = context.resources.getString(R.string.two_factor_number, number)
    }

    fun showLoading() {
        two_factor_number?.visibility = View.GONE
        enter_code_button?.visibility = View.GONE
        dismiss_code?.visibility = View.GONE
        code_input_layout?.visibility = View.GONE
        dialog_progress?.visibility = View.VISIBLE
        dialog_progress?.animate()
    }

    fun hideLoading() {
        two_factor_number?.visibility = View.VISIBLE
        enter_code_button?.visibility = View.VISIBLE
        code_input_layout?.visibility = View.VISIBLE
        dismiss_code?.visibility = View.VISIBLE

        dialog_progress?.visibility = View.GONE
        dialog_progress?.animate()
    }

    fun showErrorCode(error: String) {
        code_input_layout?.isErrorEnabled = true
        code_input_layout?.error = error
    }
}