package akhmedoff.usman.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "owners")
open class Owner(open var name: String) {
    @PrimaryKey
    var id: Long = 0
    var screenName: String = ""
    var photo100: String = ""
}