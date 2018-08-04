package akhmedoff.usman.data.model

import akhmedoff.usman.data.db.SimpleTypeConverters
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.TypeConverters

@Entity(
        tableName = "videos",
        primaryKeys = ["id", "ownerId"],
        indices = [(Index(value = ["id", "ownerId"], unique = true))]
)
class Video() : Item() {
    var addingDate: Long = 0
    var firstFrame320: String? = null
    var firstFrame160: String? = null
    var firstFrame130: String? = null
    var firstFrame800: String? = null

    @TypeConverters(SimpleTypeConverters::class)
    lateinit var files: List<VideoUrl>

    lateinit var player: String
    var canComment: Boolean = false
    var canRepost: Boolean = false

    @Embedded(prefix = "likes")
    var likes: Likes? = null

    @Embedded(prefix = "reposts")
    var reposts: Reposts? = null
    var repeat: Boolean = false

    @TypeConverters(SimpleTypeConverters::class)
    var userIds: MutableList<String> = mutableListOf()

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        ownerId = parcel.readInt()
        title = parcel.readString()
        duration = parcel.readValue(Int::class.java.classLoader) as? Int
        width = parcel.readValue(Int::class.java.classLoader) as? Int
        height = parcel.readValue(Int::class.java.classLoader) as? Int
        description = parcel.readString()
        date = parcel.readLong()
        comments = parcel.readInt()
        views = parcel.readValue(Int::class.java.classLoader) as? Int
        photo130 = parcel.readString()
        photo320 = parcel.readString()
        photo640 = parcel.readString()
        photo800 = parcel.readString()
        canAdd = parcel.readByte() != 0.toByte()
        roomId = parcel.readInt()
        addingDate = parcel.readLong()
        firstFrame320 = parcel.readString()
        firstFrame160 = parcel.readString()
        firstFrame130 = parcel.readString()
        firstFrame800 = parcel.readString()
        player = parcel.readString()
        canComment = parcel.readByte() != 0.toByte()
        canRepost = parcel.readByte() != 0.toByte()
        repeat = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeInt(id)
        parcel.writeInt(ownerId)
        parcel.writeString(title)
        parcel.writeValue(duration)
        parcel.writeValue(width)
        parcel.writeValue(height)
        parcel.writeString(description)
        parcel.writeLong(date)
        parcel.writeInt(comments)
        parcel.writeValue(views)
        parcel.writeString(photo130)
        parcel.writeString(photo320)
        parcel.writeString(photo640)
        parcel.writeString(photo800)
        parcel.writeByte(if (canAdd) 1 else 0)
        parcel.writeInt(roomId)
        parcel.writeLong(addingDate)
        parcel.writeString(firstFrame320)
        parcel.writeString(firstFrame160)
        parcel.writeString(firstFrame130)
        parcel.writeString(firstFrame800)
        parcel.writeString(player)
        parcel.writeByte(if (canComment) 1 else 0)
        parcel.writeByte(if (canRepost) 1 else 0)
        parcel.writeByte(if (repeat) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Video> {
        override fun createFromParcel(parcel: Parcel): Video {
            return Video(parcel)
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }

}
