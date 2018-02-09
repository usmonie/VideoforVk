package akhmedoff.usman.videoforvk.model

data class Album(
    val id: String,
    val ownerId: String,
    val title: String,
    val count: Int,
    val photo320: String,
    val photo160: String
)