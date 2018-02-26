package akhmedoff.usman.data.model

import android.util.Log
import retrofit2.Response as RetrofitResponse

class Response<T> {
    val response: T?
    val errorMessage: String?

    constructor(response: RetrofitResponse<T>) {
        this.response = response.body()
        errorMessage = null
    }

    constructor(throwable: Throwable) {
        Log.d("responseError", throwable.message)
        response = null
        errorMessage = throwable.message
    }

    constructor(auth: T) {
        response = auth
        errorMessage = null
    }
}