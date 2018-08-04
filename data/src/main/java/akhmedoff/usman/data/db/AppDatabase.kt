package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.*
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [Owner::class, Video::class, Album::class, Catalog::class, CatalogItem::class],
        version = 10,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {

        fun getInstance(context: Context) = Room
                .databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "vt_database.db"
                )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }

    abstract fun albumDao(): AlbumDao

    abstract fun ownerDao(): OwnerDao

    abstract fun catalogDao(): CatalogDao

    abstract fun videoDao(): VideoDao
}