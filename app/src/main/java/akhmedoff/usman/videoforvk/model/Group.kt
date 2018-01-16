package akhmedoff.usman.videoforvk.model

import com.google.gson.annotations.SerializedName

class Group(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
        @SerializedName("screen_name") val screenName: String,
        @SerializedName("is_closed") val isClosed: Boolean,
        @SerializedName("type") val type: String,
        @SerializedName("is_admin") val isAdmin: Boolean,
        @SerializedName("is_member") val isMember: Boolean,
        @SerializedName("photo_50") val photo50: String,
        @SerializedName("photo_100") val photo100: String,
        @SerializedName("photo_200") val photo200: String
)