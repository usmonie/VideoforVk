package akhmedoff.usman.videoforvk.player

import akhmedoff.usman.data.model.Video
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

    private val audioFocusRequest: AudioFocusRequest? by lazy {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(audioFocusListener)
                .build()
        } else {
            null
        }
    }
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