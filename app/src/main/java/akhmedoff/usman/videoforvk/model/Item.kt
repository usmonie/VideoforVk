package akhmedoff.usman.videoforvk.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "videos")
data class Item(@PrimaryKey var id: Long?,
                @SerializedName("owner_id") var ownerId: Long?,
                var title: String?,
                var duration: Int?,
                var description: String?,
                var date: Long?,
                var comments: Int?,
                var views: Int?,
                var width: Int?,
                var height: Int?,
                @SerializedName("photo_130") var photo130: String?,
                @SerializedName("photo_320") var photo320: String?,
                @SerializedName("photo_800") var photo800: String?,
                @SerializedName("adding_date") var addingDate: Int?,
                @SerializedName("first_frame_320") var firstFrame320: String?,
                @SerializedName("first_frame_160") var firstFrame160: String?,
                @SerializedName("first_frame_130") var firstFrame130: String?,
                @SerializedName("first_frame_800") var firstFrame800: String?,
                @Ignore var files: Files? = Files(),
                var player: String?,
                @SerializedName("can_add") var canAdd: Boolean?,
                @SerializedName("can_comment") var canComment: Boolean?,
                @SerializedName("can_repost") var canRepost: Boolean?,
                @Ignore var likes: Likes? = Likes(),
                @Ignore var reposts: Reposts? = Reposts(),
                var repeat: Boolean?
)