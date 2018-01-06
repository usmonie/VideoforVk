package akhmedoff.usman.videoforvk.model

import com.google.gson.annotations.SerializedName

data class Reposts(
        @SerializedName("count") val count: Int = -1,
        @SerializedName("user_reposted") val userReposted: Boolean = false
)