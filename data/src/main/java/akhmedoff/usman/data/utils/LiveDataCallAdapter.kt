package akhmedoff.usman.data.utils

import akhmedoff.usman.data.model.ApiResponse
import android.arch.lifecycle.LiveData
import android.util.Log
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 * @param <R>
 */
class LiveDataCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, LiveData<ApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>) = object : LiveData<ApiResponse<R>>() {
        internal var started = AtomicBoolean(false)
        override fun onActive() {
            super.onActive()
            if (started.compareAndSet(false, true)) call.enqueue(object : Callback<R> {
                override fun onResponse(call: Call<R>, response: Response<R>) {
                    postValue(ApiResponse(response))
                }

                override fun onFailure(call: Call<R>, throwable: Throwable) {
                    postValue(ApiResponse(throwable))
                    Log.e("failure", throwable.message)
                }
            })
        }
    }
}