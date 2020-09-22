package com.example.fancycars.db.carsavailability

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CarAvailability::class], version = 1, exportSchema = false)
abstract class CarAvailabilityDatabase : RoomDatabase() {

    abstract val carAvailabilityDAO: CarAvailabilityDAO

    companion object {
        /**
         * Creates a singleton instance of Car Availability Database
         */
        @Volatile
        private var INSTANCE: CarAvailabilityDatabase? = null
        fun getInstance(context: Context): CarAvailabilityDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CarAvailabilityDatabase::class.java,
                        "car_availability_database"
                    ).build()
                }
                return instance
            }
        }
    }
}