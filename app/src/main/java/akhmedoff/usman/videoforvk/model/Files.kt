package akhmedoff.usman.videoforvk.model

import com.google.gson.annotations.SerializedName

data class Files(
        @SerializedName("mp4_240") var mp4240: String? = null,
        @SerializedName("mp4_360") var mp4360: String? = null,
        @SerializedName("mp4_480") var mp4480: String? = null,
        @SerializedName("mp4_720") var mp4720: String? = null,
        @SerializedName("mp4_1080") var mp41080: String? = null,
        var external: String? = null
)