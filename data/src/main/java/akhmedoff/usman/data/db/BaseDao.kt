package akhmedoff.usman.data.db

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource


interface BaseDao<T> {

    fun insert(item: T)

    fun insertAll(items: List<T>)

    fun update(item: T)

    fun updateAll(items: List<T>)

    fun load(id: String): LiveData<T>


    fun loadAll(): DataSource.Factory<Int, T>
}