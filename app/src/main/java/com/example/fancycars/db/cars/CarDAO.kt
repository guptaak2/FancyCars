package com.example.fancycars.db.cars

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface CarDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car): Long

    @Update
    suspend fun updateCar(car: Car) : Int

    @Delete
    suspend fun deleteCar(car: Car) : Int

    @Query("DELETE FROM car_data_table")
    suspend fun deleteAllCars()

    @Query("SELECT * FROM car_data_table ORDER BY id ASC")
    fun getAllCars(): PagingSource<Int, Car>

    @Query("SELECT * FROM car_data_table ORDER BY name DESC")
    fun getAllCarsSortedByName(): PagingSource<Int, Car>

    @Query("SELECT * FROM car_data_table WHERE id = :carId")
    suspend fun getCar(carId: Int): Car
}