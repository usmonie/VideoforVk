package akhmedoff.usman.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(
    tableName = "albums",
    primaryKeys = ["id", "ownerId"],
    indices = [Index(value = ["id", "ownerId"], unique = true)]
)
class Album : Item() {
    var count: Int = 0
}