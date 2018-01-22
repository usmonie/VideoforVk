package akhmedoff.usman.videoforvk.model

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
    lateinit var likes: Likes
    lateinit var reposts: Reposts
    var repeat: Boolean = false
}
