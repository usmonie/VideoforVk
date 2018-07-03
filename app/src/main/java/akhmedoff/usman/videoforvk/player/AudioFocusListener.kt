package akhmedoff.usman.videoforvk.player

import android.media.AudioManager

class AudioFocusListener(var focusChangedListener: (Boolean) -> Unit) :
    AudioManager.OnAudioFocusChangeListener {
    override fun onAudioFocusChange(focusChange: Int) = focusChangedListener(focusChange == AudioManager.AUDIOFOCUS_GAIN)

}