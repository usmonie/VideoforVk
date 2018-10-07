package akhmedoff.usman.thevt.ui.settings

import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.ui.splash.SplashActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import java.io.File


private const val REQUEST_CODE_OPEN_DIRECTORY = 123

class SettingsFragment : BottomSheetDialogFragment(), SettingsContract.View {

    companion object {
        const val FRAGMENT_TAG = "settings_fragment"
    }

    override lateinit var presenter: SettingsContract.Presenter

    override var videoPath: String
        get() = video_save_path.text.toString()
        set(value) {
            video_save_path.text = value
        }

    override var videoQuality: Int
        get() = qualities_spinner.selectedItemPosition
        set(value) {
            qualities_spinner.setSelection(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SettingsPresenter(this, getUserRepository(context!!))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qualities_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.saveSettings()
            }
        }

        change_path.setOnClickListener { presenter.changePathWindow() }
        sign_out_button.setOnClickListener { presenter.signOut() }
        clear_cache.setOnClickListener { presenter.clearCache() }
    }

    override fun onStart() {
        super.onStart()
        presenter.onCreate()
    }

    override fun clearCache() {
        try {
            deleteDir(context?.cacheDir)
        } catch (e: Exception) {
        }
    }

    override fun signOut() {
        startActivity(Intent(activity, SplashActivity::class.java))

        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == Activity.RESULT_OK) {
            val uri = data?.data?.path
            video_save_path.text = uri ?: Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath
            presenter.saveSettings()
        }
    }

    override fun openChangePathWindow() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY)
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) return false

            }
            return dir.delete()
        } else return if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }

}