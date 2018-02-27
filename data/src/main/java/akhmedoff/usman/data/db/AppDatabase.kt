package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.Group
import akhmedoff.usman.data.model.User
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [User::class, Group::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun groupDao(): GroupDao
}