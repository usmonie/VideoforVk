package akhmedoff.usman.videoforvk.model

data class ResponseCatalog(
    val catalogs: MutableList<Catalog>,
    val next: String
)