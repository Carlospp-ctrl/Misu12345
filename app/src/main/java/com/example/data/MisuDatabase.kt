package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MisuEntity::class, MoodLogEntity::class], version = 1, exportSchema = false)
abstract class MisuDatabase : RoomDatabase() {
    abstract fun misuDao(): MisuDao

    companion object {
        @Volatile
        private var INSTANCE: MisuDatabase? = null

        fun getDatabase(context: Context): MisuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MisuDatabase::class.java,
                    "misu_database"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
