package akhmedoff.usman.videoforvk.player

import android.media.AudioManager
import com.google.android.exoplayer2.ExoPlayer

class AudioFocusListener(var player: ExoPlayer?) : AudioManager.OnAudioFocusChangeListener {
    override fun onAudioFocusChange(focusChange: Int) {
        player?.playWhenReady = when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> false
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> false
            AudioManager.AUDIOFOCUS_GAIN -> true
            else -> false
        }
    }
}