package akhmedoff.usman.videoforvk.model

import com.google.gson.annotations.SerializedName

data class ResponseVideo(
        @SerializedName("count") val count: Int,
        @SerializedName("items") val items: List<Item>?,
        @SerializedName("profiles") val profiles: List<User>?,
        @SerializedName("groups") val groups: List<Group>?
)