package com.example.neolight.data.local.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [FlashOption::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun optionsDao(): FlashOptionDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flash_database"
                ).addCallback(OptionsCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class OptionsCallback(val scope: CoroutineScope): Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("AppDatabase", "Callback called!")
            INSTANCE?.let { FlashOptionsDB ->
                scope.launch {
                    Log.d("AppDatabase", "Inserting Items!")
                    FlashOptionsDB.optionsDao().insert(FlashOption(name="Default", delay = 0))
                    FlashOptionsDB.optionsDao().insert(FlashOption(name="Flash 1", delay = 500))
                }
            }
        }
    }
}







