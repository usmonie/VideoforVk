package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.Owner
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Owner::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {

        fun getInstance(context: Context) =
            Room
                .databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "vt_database"
                )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()


    }

    abstract fun ownerDao(): OwnerDao
}