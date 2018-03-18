package akhmedoff.usman.data.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(
    tableName = "videos",
    primaryKeys = ["id", "ownerId"],
    indices = [(Index(value = ["id", "ownerId"], unique = true))]
)
class Video : Item() {
    var addingDate: Long = 0
    var firstFrame320: String? = null
    var firstFrame160: String? = null
    var firstFrame130: String? = null
    var firstFrame800: String? = null

    @Embedded
    lateinit var files: Files
    lateinit var player: String
    var canComment: Boolean = false
    var canRepost: Boolean = false

    @Embedded(prefix = "likes")
    var likes: Likes? = null

    @Embedded(prefix = "reposts")
    var reposts: Reposts? = null
    var repeat: Boolean = false

}
