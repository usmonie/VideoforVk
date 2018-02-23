package akhmedoff.usman.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(
    tableName = "catalogs",
    primaryKeys = ["id"],
    indices = [(Index(value = ["id"], unique = true))]
)
class Catalog(
    var items: MutableList<CatalogItem>?,
    var name: String?,
    var id: String?,
    var view: String? = null,
    var canHide: Boolean?,
    var type: String?
)