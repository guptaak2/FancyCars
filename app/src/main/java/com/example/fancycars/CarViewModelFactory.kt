package com.example.fancycars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fancycars.db.cars.CarRepository
import com.example.fancycars.db.carsavailability.CarsAvailabilityRepository

/**
 * Creates [CarViewModel]
 */
class CarViewModelFactory(
    private val carRepository: CarRepository,
    private val availabilityRepository: CarsAvailabilityRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarViewModel::class.java)) {
            return CarViewModel(carRepository, availabilityRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}