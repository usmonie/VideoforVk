package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.Group
import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.*

@Dao
interface GroupDao : BaseDao<Group> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(item: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertAll(items: List<Group>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override fun update(item: Group)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override fun updateAll(items: List<Group>)

    @Query("SELECT * FROM groups WHERE id LIKE :id LIMIT 1")
    override fun load(id: String): LiveData<Group>

    @Query("SELECT * FROM groups")
    override fun loadAll(): DataSource.Factory<Int, Group>
}