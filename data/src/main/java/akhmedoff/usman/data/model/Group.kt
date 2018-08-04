package akhmedoff.usman.data.model

import androidx.room.Entity

@Entity(tableName = "groups")
class Group : Owner() {
    var isClosed: Boolean = false
    var type: String = ""
    var isAdmin: Boolean = false
    var isMember: Boolean = false
    var photo50: String = ""
    var photo200: String = ""
}