package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.Owner
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface OwnerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Owner)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: Owner)

    @Query("SELECT * FROM owners WHERE id IS :id LIMIT 1")
    fun load(id: Long): LiveData<Owner>

    @Query("SELECT * FROM owners LIMIT :count OFFSET :from")
    fun loadAll(count: Int, from: Int): List<Owner>
}