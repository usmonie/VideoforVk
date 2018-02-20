package akhmedoff.usman.videoforvk.model

class Group(
    var id: Long,
    var name: String,
    var screenName: String,
    var isClosed: Boolean,
    var type: String,
    var isAdmin: Boolean,
    var isMember: Boolean,
    var photo50: String,
    var photo100: String,
    var photo200: String
)