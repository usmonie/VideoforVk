package akhmedoff.usman.data.model

import android.util.Log
import retrofit2.Response as RetrofitResponse

class Response<T> {
    val response: T?
    val errorMessage: String?
    val code: Int

    constructor(response: RetrofitResponse<T>) {
        this.response = response.body()
        code = response.code()
        errorMessage = null
    }

    constructor(throwable: Throwable) {
        Log.d("responseError", throwable.message)
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