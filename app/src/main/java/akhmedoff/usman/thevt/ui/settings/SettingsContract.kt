package akhmedoff.usman.thevt.ui.settings

interface SettingsContract {

    interface View {
        var presenter: Presenter

        var videoPath: String

        var videoQuality: Int

        fun clearCache()

        fun openChangePathWindow()

        fun signOut()
    }

    interface Presenter {
        var view: View?

        fun onCreate()

        fun saveSettings()

        fun clearCache()

        fun signOut()

        fun changePathWindow()
    }
}