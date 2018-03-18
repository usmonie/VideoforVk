package akhmedoff.usman.data.model

import android.arch.persistence.room.Relation

data class Catalog(
    var id: String = "",

    @Relation(parentColumn = "id", entityColumn = "catalogId")
    var items: List<CatalogItem> = listOf(),
    var name: String? = null,
    var view: String? = null,
    var canHide: Boolean? = false,
    var type: String? = null
)