package com.example.fancycars.db.carsavailability

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CarAvailabilityDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarAvailability(carAvailability: CarAvailability): Long

    @Update
    suspend fun updateCarAvailability(carAvailability: CarAvailability)

    @Delete
    suspend fun deleteCarAvailability(carAvailability: CarAvailability)

    @Query("DELETE FROM car_availability_data_table")
    suspend fun deleteAllCars()

    @Query("SELECT * FROM car_availability_data_table")
    fun getAllCars(): LiveData<List<CarAvailability>>

    @Query("SELECT * FROM car_availability_data_table WHERE id = :carId")
    suspend fun getCarAvailability(carId: Int): CarAvailability
}