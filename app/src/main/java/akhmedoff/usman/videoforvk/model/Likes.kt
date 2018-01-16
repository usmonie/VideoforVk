package akhmedoff.usman.videoforvk.model

import com.google.gson.annotations.SerializedName

class Likes(
        @SerializedName("user_likes") val userLikes: Boolean = false,
        val count: Int = 0
)