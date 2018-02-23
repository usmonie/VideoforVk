package akhmedoff.usman.videoforvk.player

import akhmedoff.usman.data.model.Video
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player

class SimpleControlDispatcher(
    private val audioFocusListener: AudioFocusListener,
    private val audioManager: AudioManager,
    private val externalLinkListener: (String) -> Unit,
    private val audioFocusRequest: AudioFocusRequest
) : DefaultControlDispatcher() {

    lateinit var item: Video

    override fun dispatchSetPlayWhenReady(
        player: Player?,
        playWhenReady: Boolean
    ) = super.dispatchSetPlayWhenReady(
        player,
        when (item.files.external) {
            null -> {
                val res = getAudioFocusResponse()

                when (res) {
                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> playWhenReady
                    else -> false
                }
            }

            else -> {
                item.files.external?.let(externalLinkListener)
                false
            }
        }
    )

    private fun getAudioFocusResponse() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            audioManager.requestAudioFocus(audioFocusRequest)
        else audioManager.requestAudioFocus(
            audioFocusListener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
}