package akhmedoff.usman.videoforvk.data.repository

import akhmedoff.usman.videoforvk.AppExecutors
import akhmedoff.usman.videoforvk.model.Response
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import java.util.*

/**
 * A generic class that can provide a resource backed by both the SQLite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
 */
abstract class NetworkBoundResources<ResultType, RequestType> @MainThread
internal constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<ResultType>()

    init {
        result.value = null
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource, this::setValue)
            }
        }
    }

    @MainThread
    private fun setValue(newValue: ResultType?) {
        if (!Objects.equals(result.value, newValue)) result.value = newValue
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource, this::setValue)
        result.addSource<Response<RequestType>>(apiResponse) { response ->
            result.removeSource<Response<RequestType>>(apiResponse)
            result.removeSource(dbSource)

            if (response?.isSuccessfull!!) {
                appExecutors.diskIO().execute({
                    saveCallResult(processResponse(response))
                    appExecutors.mainThread().execute {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        result.addSource(loadFromDb()) { newData -> setValue(newData) }
                    }
                })
            } else {
                onFetchFailed()
                result.addSource(dbSource) { newData -> setValue(newData) }
            }
        }
    }

    protected fun onFetchFailed() {}

    fun asLiveData() = result

    @WorkerThread
    protected fun processResponse(response: Response<RequestType>) =
        response.response!!

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<Response<RequestType>>
}