package akhmedoff.usman.videoforvk.model

class User(
    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var nickname: String = "",
    var screenName: String = "",
    var photo100: String = "",
    var photoMax: String = "",
    var photoMaxOrig: String = "",
    var photoId: String = "",
    var hasPhoto: Boolean = false,
    var isFriend: Boolean = false,
    var friendStatus: Boolean = false,
    var online: Boolean = false,
    var status: String = "",
    var isFavorite: Boolean = false
)