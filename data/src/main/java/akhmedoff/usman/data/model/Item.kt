package akhmedoff.usman.data.model

open class Item {
    var id: Int = 0
    var ownerId: Int = 0
    lateinit var title: String
    var duration: Int? = 0
    var width: Int? = 0
    var height: Int? = 0
    var description: String? = null
    var date: Long = 0
    var comments: Int = 0
    var views: Int? = 0
    var photo130: String? = null
    var photo320: String? = null
    var photo640: String? = null
    var photo800: String? = null
    var canAdd: Boolean = false
}