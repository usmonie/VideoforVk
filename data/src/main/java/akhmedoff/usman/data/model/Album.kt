package akhmedoff.usman.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index

@Entity(
        tableName = "albums",
        primaryKeys = ["id", "ownerId"],
        indices = [(Index(value = ["id", "ownerId"], unique = true))]
)
class Album : Item(), Parcelable {
    var count: Int = 0
}