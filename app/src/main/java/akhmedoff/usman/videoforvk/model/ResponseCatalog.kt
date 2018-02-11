package akhmedoff.usman.videoforvk.model

import akhmedoff.usman.videoforvk.utils.deserializers.CatalogDeserializer
import com.google.gson.annotations.JsonAdapter

@JsonAdapter(CatalogDeserializer::class)
class ResponseCatalog(
    val catalogs: MutableList<Catalog>,
    val next: String? = null
)