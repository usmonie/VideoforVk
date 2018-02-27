package akhmedoff.usman.data.model

import android.util.Log
import retrofit2.Response as RetrofitResponse

class ApiResponse<T> {
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

    constructor(item: T) {
        response = item
        errorMessage = null
    }
}