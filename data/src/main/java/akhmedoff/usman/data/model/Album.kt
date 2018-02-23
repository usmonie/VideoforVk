package akhmedoff.usman.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(
    tableName = "albums",
    primaryKeys = ["id", "ownerId"],
    indices = [Index(value = ["id", "ownerId"], unique = true)]
)
data class Album(

    val id: String,

    val ownerId: String,
    val title: String,
    val count: Int,
    val photo320: String,
    val photo160: String
)