package akhmedoff.usman.data.model

class Video : Item() {
    var addingDate: Long = 0
    var firstFrame320: String? = null
    var firstFrame160: String? = null
    var firstFrame130: String? = null
    var firstFrame800: String? = null
    lateinit var files: Files
    lateinit var player: String
    var canComment: Boolean = false
    var canRepost: Boolean = false
    var likes: Likes? = null
    var reposts: Reposts? = null
    var repeat: Boolean = false
}
