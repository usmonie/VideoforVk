package akhmedoff.usman.data.model

import akhmedoff.usman.data.db.SimpleTypeConverters
import android.arch.persistence.room.Entity
import android.arch.persistence.room.TypeConverters


@Entity(
        tableName = "catalogs",
        primaryKeys = ["id"]
)
class Catalog {
    var id: String = ""

    @TypeConverters(SimpleTypeConverters::class)
    var items: List<CatalogItem> = listOf()

    var name: String? = null
    var view: String? = null
    var canHide: Boolean? = false
    var type: String? = null

    var nextKey: String = ""
}