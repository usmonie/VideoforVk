package akhmedoff.usman.videoforvk.model

class Group(
    val id: Long,
    val name: String,
    val screenName: String,
    val isClosed: Boolean,
    val type: String,
    val isAdmin: Boolean,
    val isMember: Boolean,
    val photo50: String,
    val photo100: String,
    val photo200: String
)