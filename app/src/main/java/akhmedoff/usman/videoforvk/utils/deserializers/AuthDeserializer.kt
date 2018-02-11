package akhmedoff.usman.videoforvk.utils.deserializers

import akhmedoff.usman.videoforvk.Error
import akhmedoff.usman.videoforvk.model.Auth
import akhmedoff.usman.videoforvk.model.ValidationType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class AuthDeserializer : JsonDeserializer<Auth> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): Auth {
        val jsonObject = json.asJsonObject

        val auth = Auth()

        jsonObject["access_token"]?.let { auth.accessToken = it.asString }
        jsonObject["expires_in"]?.let { auth.expiresIn = it.asInt }
        jsonObject["user_id"]?.let { auth.userId = it.asInt }
        jsonObject["error_description"]?.let { auth.errorDescription = it.asString }

        jsonObject["error"]?.let {
            when (it.asJsonPrimitive.asString) {
                "need_validation" -> auth.error = Error.NEED_VALIDATION
                "need_captcha" -> auth.error = Error.NEED_CAPTCHA
                "invalid_client" -> if (auth.errorDescription == "Username or password is incorrect")
                    auth.error = Error.ERROR_LOGIN
            }
        }
        jsonObject["redirect_uri"]?.let { auth.redirectUri = it.asString }

        jsonObject["validation_type"]?.let {
            when (it.asJsonPrimitive.asString) {
                "2fa_sms" -> auth.validationType = ValidationType.SMS
                "2fa_app" -> auth.validationType = ValidationType.APP
            }
        }
        jsonObject["phone_mask"]?.let { auth.phoneMask = it.asString }

        jsonObject["captcha_sid"]?.let { auth.captchaSid = it.asString }
        jsonObject["captcha_img"]?.let { auth.captchaImg = it.asString }

        return auth
    }
}
