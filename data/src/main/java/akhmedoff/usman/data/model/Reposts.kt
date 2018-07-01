package akhmedoff.usman.data.model

import android.os.Parcel
import android.os.Parcelable

class Reposts() : Parcelable {
    var count: Int = -1
    var userReposted: Boolean = false

    constructor(parcel: Parcel) : this() {
        count = parcel.readInt()
        userReposted = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
        parcel.writeByte(if (userReposted) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reposts> {
        override fun createFromParcel(parcel: Parcel): Reposts {
            return Reposts(parcel)
        }

        override fun newArray(size: Int): Array<Reposts?> {
            return arrayOfNulls(size)
        }
    }
}