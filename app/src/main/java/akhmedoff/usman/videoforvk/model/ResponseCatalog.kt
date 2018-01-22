package akhmedoff.usman.videoforvk.model

data class ResponseCatalog(
    val catalogs: List<Catalog>,
    val next: String
)