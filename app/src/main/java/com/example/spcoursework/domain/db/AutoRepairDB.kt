package com.example.spcoursework.domain.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.spcoursework.entities.Client
import com.example.spcoursework.entities.Employee
import com.example.spcoursework.entities.Request

@Database(
    entities = [
        Employee::class,
        Client::class,
        Request::class
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 1, to = 3)
    ]
)
@TypeConverters(
    EmployeeRoleConverter::class,
    UUIDConverter::class
)
abstract class AutoRepairDB : RoomDatabase() {
    abstract fun getDao(): AutoRepairDao


    companion object {
        @Volatile
        private var INSTANCE: AutoRepairDB? = null

        fun getInstance(context: Context): AutoRepairDB {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AutoRepairDB::class.java,
                    "AutoRepair_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}