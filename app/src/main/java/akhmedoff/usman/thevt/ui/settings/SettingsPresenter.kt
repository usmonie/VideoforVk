package akhmedoff.usman.thevt.ui.settings

import akhmedoff.usman.data.repository.UserRepository

class SettingsPresenter(override var view: SettingsContract.View?, private val userRepository: UserRepository) : SettingsContract.Presenter {

    override fun onCreate() {
        view?.videoPath = userRepository.getVideoSavePath()
        view?.videoQuality = userRepository.getVideoQuality()
    }

    override fun saveSettings() {
        view?.let { view ->
            userRepository.saveVideoQuality(view.videoQuality)
            userRepository.saveVideoSavePath(view.videoPath)
        }
    }

    override fun clearCache() {
        view?.clearCache()
    }

    override fun signOut() {
        userRepository.clear()
        view?.signOut()
    }

    override fun changePathWindow() {
        view?.openChangePathWindow()
    }
}