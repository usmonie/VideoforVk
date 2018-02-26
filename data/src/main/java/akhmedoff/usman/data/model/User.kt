package akhmedoff.usman.data.model

import android.arch.persistence.room.Entity

@Entity(tableName = "users")
class User {
    var id: Long = 0
    var firstName: String = ""
    var lastName: String = ""
    var nickname: String = ""
    var screenName: String = ""
    var photo100: String = ""
    var photoMax: String = ""
    var photoMaxOrig: String = ""
    var photoId: String = ""
    var isFriend: Boolean = false
    var friendStatus: Int = 1
}