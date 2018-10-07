package akhmedoff.usman.data.model

import akhmedoff.usman.data.db.SimpleTypeConverters
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize


@Entity(
        tableName = "catalogs",
        primaryKeys = ["id"]
)
@Parcelize
class Catalog : Parcelable {
    var id: String = ""

    @TypeConverters(SimpleTypeConverters::class)
    var items: List<CatalogItem> = listOf()

    var name: String? = null
    var view: String? = null
    var canHide: Boolean? = false
    var type: String? = null

    var nextKey: String = ""
}