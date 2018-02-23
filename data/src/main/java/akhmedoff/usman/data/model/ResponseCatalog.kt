package akhmedoff.usman.data.model

class ResponseCatalog(
    val catalogs: MutableList<Catalog>,
    val next: String? = null
)