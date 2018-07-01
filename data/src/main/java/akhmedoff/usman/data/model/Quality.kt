package akhmedoff.usman.data.model

import android.os.Parcel
import android.os.Parcelable

enum class Quality() : Parcelable {

    EXTERNAL,
    HLS,
    HD,
    FULLHD,
    qHD,
    P360,
    P240,

    LOW;

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ordinal)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Quality> {
        override fun createFromParcel(`in`: Parcel) = Quality.values()[`in`.readInt()]

        override fun newArray(size: Int): Array<Quality?> = arrayOfNulls(size)
    }
}