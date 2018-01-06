package akhmedoff.usman.videoforvk.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "videos")
data class Item(@PrimaryKey var id: Long = 0,
                @SerializedName("owner_id") var ownerId: Long = 0,
                var title: String = "",
                var duration: Int = 0,
                var description: String = "",
                var date: Long = 0,
                var comments: Int = 0,
                var views: Int = 0,
                var width: Int = 0,
                var height: Int = 0,
                @SerializedName("photo_130") var photo130: String = "",
                @SerializedName("photo_320") var photo320: String = "",
                @SerializedName("photo_800") var photo800: String = "",
                @SerializedName("adding_date") var addingDate: Int = 0,
                @SerializedName("first_frame_320") var firstFrame320: String = "",
                @SerializedName("first_frame_160") var firstFrame160: String = "",
                @SerializedName("first_frame_130") var firstFrame130: String = "",
                @SerializedName("first_frame_800") var firstFrame800: String = "",
                @Ignore var files: Files = Files(),
                var player: String = "",
                @SerializedName("can_add") var canAdd: Boolean = false,
                @SerializedName("can_comment") var canComment: Boolean = false,
                @SerializedName("can_repost") var canRepost: Boolean = false,
                @Ignore var likes: Likes = Likes(),
                @Ignore var reposts: Reposts = Reposts(),
                var repeat: Boolean = false
)