package com.example.fancycars.db.cars

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Car::class], version = 1, exportSchema = false)
abstract class CarDatabase : RoomDatabase() {

    abstract val carDao: CarDAO

    companion object {
        /**
         * Creates a singleton instance of Car Database
         */
        @Volatile
        private var INSTANCE: CarDatabase? = null
        fun getInstance(context: Context): CarDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CarDatabase::class.java,
                        "car_database"
                    ).build()
                }
                return instance
            }
        }
    }
}