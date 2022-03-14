package com.example.appnasapi.bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [POD::class], version = 1, exportSchema = true)
abstract class PODDatabase() : RoomDatabase() {
    abstract fun podDao(): PODDao

    companion object {
        @Volatile
        private var INSTANCE: PODDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): PODDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PODDatabase::class.java,
                    "pod_database"
                ).addCallback(PODCallback(scope))
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }

    private class PODCallback(val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let {

            }
        }
    }
}