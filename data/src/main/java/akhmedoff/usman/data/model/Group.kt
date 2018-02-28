package akhmedoff.usman.data.model

import android.arch.persistence.room.Entity

@Entity(tableName = "groups")
class Group : Owner() {
    var name: String = ""
    var isClosed: Boolean = false
    var type: String = ""
    var isAdmin: Boolean = false
    var isMember: Boolean = false
    var photo50: String = ""
    var photo200: String = ""
}