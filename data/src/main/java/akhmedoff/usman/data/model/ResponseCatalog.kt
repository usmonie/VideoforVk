package akhmedoff.usman.data.model

class ResponseCatalog(
        val catalogs: MutableList<Catalog>,
        val next: String? = null,
        val profiles: List<User>? = null,
        val groups: List<Group>? = null
)