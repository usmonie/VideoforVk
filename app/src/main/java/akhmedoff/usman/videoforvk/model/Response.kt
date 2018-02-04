package akhmedoff.usman.videoforvk.model

import android.util.Log
import com.google.gson.annotations.SerializedName
import retrofit2.Response as RetrofitResponse

class Response<T> {
    @SerializedName("responseCatalog")
    val response: T?
    val errorMessage: String?
    val code: Int

    constructor(response: RetrofitResponse<T>) {
        this.response = response.body()
        code = response.code()
        errorMessage = null
    }

    constructor(throwable: Throwable) {
        Log.d("responseError", throwable.toString())
        response = null
        errorMessage = throwable.message
        code = 500
    }

    constructor(auth: T) {
        response = auth
        errorMessage = null
        code = 200
    }

    var isSuccessfull = false
        get() = code in 200..300
}