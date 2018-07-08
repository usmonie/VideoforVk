package akhmedoff.usman.videoforvk.player

import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player

class SimpleControlDispatcher(
        private val externalLinkListener: (String) -> Unit
) : DefaultControlDispatcher() {

    var isExternal: Boolean = false
    var url: String = ""

    override fun dispatchSetPlayWhenReady(
            player: Player?,
            playWhenReady: Boolean
    ) = super.dispatchSetPlayWhenReady(
            player,
            if (isExternal) {
                externalLinkListener(url)
                false
            } else playWhenReady
    )
}