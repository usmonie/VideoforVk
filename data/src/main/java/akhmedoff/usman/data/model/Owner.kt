package akhmedoff.usman.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "owners")
open class Owner {

    @PrimaryKey
    var id: Long = 0
    var name: String = ""
    var screenName: String = ""
    var photo100: String = ""
}