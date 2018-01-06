package akhmedoff.usman.videoforvk.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(@PrimaryKey var id: Long = 0,
                @SerializedName("first_name") var firstName: String = "",
                @SerializedName("last_name") var lastName: String = "",
                var nickname: String = "",
                @SerializedName("screen_name") var screenName: String = "",
                @SerializedName("photo_100") var photo100: String = "",
                @SerializedName("photo_max") var photoMax: String = "",
                @SerializedName("photo_max_orig") var photoMaxOrig: String = "",
                @SerializedName("photo_id") var photoId: String = "",
                @SerializedName("has_photo") var hasPhoto: Boolean = false,
                @SerializedName("is_friend") var isFriend: Boolean = false,
                @SerializedName("friend_status") var friendStatus: Boolean = false,
                var online: Boolean = false,
                var status: String = "",
                @SerializedName("is_favorite") var isFavorite: Boolean = false
)