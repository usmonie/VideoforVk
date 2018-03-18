package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import android.arch.persistence.room.*

@Dao
interface CatalogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: CatalogItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(item: List<CatalogItem>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: CatalogItem)

    @Query("SELECT * FROM catalog_items WHERE id IS :id LIMIT 1")
    fun load(id: Long): Catalog

    @Query("SELECT * FROM catalog_items LIMIT :count OFFSET :from")
    fun loadAll(count: Int, from: Int): List<Catalog>
}