package akhmedoff.usman.data.model

import android.os.Parcel
import android.os.Parcelable

open class Item() : Parcelable {
    var id: Int = 0
    var ownerId: Int = 0
    lateinit var title: String
    var duration: Int? = 0
    var width: Int? = 0
    var height: Int? = 0
    var description: String? = null
    var date: Long = 0
    var comments: Int = 0
    var views: Int? = 0
    var photo130: String? = null
    var photo320: String? = null
    var photo640: String? = null
    var photo800: String? = null
    var canAdd: Boolean = false

    var roomId: Int = 0

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
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
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
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}