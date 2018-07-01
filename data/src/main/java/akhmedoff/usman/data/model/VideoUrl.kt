package akhmedoff.usman.data.model

import android.os.Parcel
import android.os.Parcelable

class VideoUrl(val url: String, val quality: Quality) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(Quality::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeParcelable(quality, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<VideoUrl> {
        override fun createFromParcel(parcel: Parcel): VideoUrl = VideoUrl(parcel)

        override fun newArray(size: Int): Array<VideoUrl?> = arrayOfNulls(size)
    }
}

