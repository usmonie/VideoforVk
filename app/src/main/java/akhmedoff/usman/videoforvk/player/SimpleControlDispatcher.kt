package akhmedoff.usman.videoforvk.player

import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player

class SimpleControlDispatcher(
    private val audioFocusListener: AudioFocusListener,
    private val audioManager: AudioManager,
    private val externalLinkListener: (String) -> Unit
) : DefaultControlDispatcher() {

    private val audioFocusRequest by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                .build()
            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(audioFocusListener)
                .build()
        } else {
            null
        }
    }

    var isExternal: Boolean = false
    var url: String = ""

    override fun dispatchSetPlayWhenReady(
        player: Player?,
        playWhenReady: Boolean
    ) = super.dispatchSetPlayWhenReady(
        player,
        when (isExternal) {
            true -> {
                externalLinkListener(url)
                false
            }

            else ->
                when (getAudioFocusResponse()) {
                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> playWhenReady
                    else -> false
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