package akhmedoff.usman.data.model

import android.arch.persistence.room.Entity

@Entity(tableName = "users")
class User(
    var firstName: String = "",
    var lastName: String = ""
) : Owner("$firstName $lastName") {
    var nickname: String = ""
    var photoMax: String = ""
    var photoMaxOrig: String = ""
    var photoId: String = ""
    var isFriend: Boolean = false
    var friendStatus: Int = 1
}