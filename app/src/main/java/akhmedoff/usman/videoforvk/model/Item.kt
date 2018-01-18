package akhmedoff.usman.videoforvk.model

abstract class Item {
    var id: Int = 0
    var ownerId: Int = 0
    lateinit var title: String
    var duration: Int = 0
    var width: Int? = 0
    var height: Int? = 0
    lateinit var description: String
    var date: Int = 0
    var comments: Int = 0
    var views: Int = 0
    lateinit var photo130: String
    lateinit var photo320: String
    var photo640: String? = null
    var photo800: String? = null
    var canAdd: Boolean = false
}