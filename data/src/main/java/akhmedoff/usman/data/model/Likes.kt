package akhmedoff.usman.data.model

import android.os.Parcel
import android.os.Parcelable

class Likes() : Parcelable {
    var userLikes: Boolean = false
    var likes: Int = 0

    constructor(parcel: Parcel) : this() {
        userLikes = parcel.readByte() != 0.toByte()
        likes = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (userLikes) 1 else 0)
        parcel.writeInt(likes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Likes> {
        override fun createFromParcel(parcel: Parcel): Likes {
            return Likes(parcel)
        }

        override fun newArray(size: Int): Array<Likes?> {
            return arrayOfNulls(size)
        }
    }
}