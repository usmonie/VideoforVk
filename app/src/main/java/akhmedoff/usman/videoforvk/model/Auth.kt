package akhmedoff.usman.videoforvk.model

class Auth {
    var accessToken: String? = null
    var expiresIn: Int? = null
    var userId: Int? = null

    var error: String? = null
    var errorDescription: String? = null
    var redirectUri: String? = null

    var captchaSid: String? = null
    var captchaImg: String? = null

    var isSuccessfull: Boolean = false
        get() = accessToken != null
}