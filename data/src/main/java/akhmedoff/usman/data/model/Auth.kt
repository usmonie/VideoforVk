package akhmedoff.usman.data.model

import akhmedoff.usman.data.Error

class Auth {
    var accessToken: String? = null
    var expiresIn: Int? = null
    var userId: Int? = null

    var error: Error = Error.OK
    var errorDescription: String? = null
    var redirectUri: String? = null

    var captchaSid: String? = null
    var captchaImg: String? = null

    var validationType: ValidationType? = null
    var phoneMask: String? = null

    var isSuccessful: Boolean = false
        get() = accessToken != null

}