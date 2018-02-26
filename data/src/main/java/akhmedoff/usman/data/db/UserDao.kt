package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.User
import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.*

@Dao
interface UserDao : BaseDao<User> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertAll(users: List<User>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override fun update(item: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override fun updateAll(users: List<User>)

    @Query("SELECT * FROM users WHERE id LIKE :id LIMIT 1")
    override fun load(id: String): LiveData<User>

    @Query("SELECT * FROM users")
    override fun loadAll(): DataSource.Factory<Int, User>
}