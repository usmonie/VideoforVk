package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.Video
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*


@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Video)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: List<Video>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: Video)

    @Query("SELECT * FROM videos WHERE id IS :id LIMIT 1")
    fun load(id: Long): LiveData<Video>

    @Query("SELECT * FROM videos WHERE ownerId = :ownerId LIMIT :count OFFSET :from ")
    fun loadAll(count: Int, from: Int, ownerId: Int?): List<Video>

    @Query("SELECT * FROM videos WHERE title LIKE :query LIMIT :count OFFSET :from")
    fun loadAll(count: Int, from: Int, query: String?): List<Video>

}