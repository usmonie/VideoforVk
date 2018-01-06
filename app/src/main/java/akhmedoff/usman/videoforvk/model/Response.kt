package akhmedoff.usman.videoforvk.model

import com.google.gson.annotations.SerializedName
import retrofit2.Response as RetrofitResponse

class Response<T> {
    @SerializedName("response")
    val response: T?
    val errorMessage: String?
    val code: Int

    constructor(response: RetrofitResponse<T>) {
        this.response = response.body()
        code = response.code()
        errorMessage = null
    }

    constructor(throwable: Throwable) {
        response = null
        errorMessage = throwable.message
        code = 500
    }

    var isSuccessfull = false
        get() = code in 200..300
}