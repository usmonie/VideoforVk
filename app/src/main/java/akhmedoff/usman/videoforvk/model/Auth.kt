package akhmedoff.usman.videoforvk.model

import com.google.gson.annotations.SerializedName

class Auth {

    @SerializedName("access_token")
    var accessToken: String? = null
    @SerializedName("expires_in")
    var expiresIn: Int? = null
    @SerializedName("user_id")
    var userId: Int? = null

    @SerializedName("error")
    var error: String? = null
    @SerializedName("error_description")
    var errorDescription: String? = null
    @SerializedName("redirect_uri")
    var redirectUri: String? = null

    @SerializedName("captcha_sid")
    var captchaSid: String? = null
    @SerializedName("captcha_img")
    var captchaImg: String? = null

    var isSuccessfull: Boolean = false
        get() = accessToken != null
}