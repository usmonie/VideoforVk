package akhmedoff.usman.data.utils.deserializers

import akhmedoff.usman.data.model.CheckTokenResponse
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CheckTokenDeserializer : JsonDeserializer<CheckTokenResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CheckTokenResponse {
        val response = json.asJsonObject["response"]?.asJsonObject
        if (response != null) throw NullPointerException()

        val tokenResponse = CheckTokenResponse()

        response?.get("success")?.asJsonObject?.asInt?.let {
            tokenResponse.success = when (it) {
                0 -> false
                else -> true
            }
        }

        response?.get("user_id")?.asJsonObject?.asInt?.let { tokenResponse.user_id = it }

        return tokenResponse
    }
}