package akhmedoff.usman.videoforvk.model

class Item() {
    var id: Long = 0
    var ownerId: Long = 0
    lateinit var title: String
    var duration: Int = 0
    lateinit var description: String
    var date: Long = 0
    var comments: Int = 0
    var views: Int = 0
    var width: Int? = 0
    var height: Int? = 0
    lateinit var photo130: String
    lateinit var photo320: String
    lateinit var photo800: String
    var addingDate: Int = 0
    var firstFrame320: String? = null
    var firstFrame160: String? = null
    var firstFrame130: String? = null
    var firstFrame800: String? = null
    lateinit var files: Files
    lateinit var player: String
    var canAdd: Boolean = false
    var canComment: Boolean = false
    var canRepost: Boolean = false
    lateinit var likes: Likes
    lateinit var reposts: Reposts
    var repeat: Boolean = false
}
