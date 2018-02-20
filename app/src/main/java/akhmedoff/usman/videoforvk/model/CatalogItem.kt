package akhmedoff.usman.videoforvk.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(
    tableName = "albums",
    primaryKeys = ["id", "ownerId"],
    indices = [(Index(value = ["id", "ownerId"], unique = true))]
)
class CatalogItem : Item() {
    var accessKey: String? = null
    var platform: String? = null
    var type: CatalogItemType? = null
}