package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.Album
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(item: List<Album>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: Album)

    @Query("SELECT * FROM albums WHERE id IS :id LIMIT 1")
    fun load(id: Long): LiveData<Album>

    @Query("SELECT * FROM albums LIMIT :count OFFSET :from")
    fun loadAll(count: Int, from: Int): LiveData<List<Album>>
}