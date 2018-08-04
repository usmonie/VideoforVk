package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.Catalog
import androidx.room.*

@Dao

interface CatalogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Catalog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: List<Catalog>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: Catalog)

    @Query("SELECT * FROM catalogs WHERE id IS :id LIMIT 1")
    fun load(id: Long): Catalog

    @Query("SELECT * FROM catalogs WHERE nextKey IN (:nextKey) LIMIT :count")
    fun loadAll(count: Int, nextKey: Array<String>): List<Catalog>
}