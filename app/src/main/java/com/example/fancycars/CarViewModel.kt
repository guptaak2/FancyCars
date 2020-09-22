package com.example.fancycars

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.fancycars.db.cars.Car
import com.example.fancycars.db.cars.CarRepository
import com.example.fancycars.db.carsavailability.CarAvailability
import com.example.fancycars.db.carsavailability.CarsAvailabilityRepository
import kotlinx.coroutines.launch

/**
 * ViewModel that fetches data from [CarRepository] and [CarsAvailabilityRepository].
 */
class CarViewModel(
    private val carRepository: CarRepository,
    private val availabilityRepository: CarsAvailabilityRepository
) : ViewModel(), Observable {

    @Bindable
    val seeCarsButtonText = MutableLiveData<String>()

    init {
        seeCarsButtonText.value = "See Cars"
    }

    val carAvailabilities = availabilityRepository.carsAvailability

    val cars = Pager(
        PagingConfig(
            pageSize = 5,
            maxSize = 20
        )
    ) {
        carRepository.cars
    }.liveData.cachedIn(viewModelScope)

    val carsSortedByName = Pager(
        PagingConfig(
            pageSize = 5,
            maxSize = 20
        )
    ) {
        carRepository.carsSortedByName
    }.liveData.cachedIn(viewModelScope)

    fun insert(car: Car) = viewModelScope.launch {
        carRepository.insert(car)
    }

    fun insert(carAvailability: CarAvailability) = viewModelScope.launch {
        availabilityRepository.insert(carAvailability)
    }

    fun update(car: Car) = viewModelScope.launch {
        carRepository.update(car)
    }

    fun delete(car: Car) = viewModelScope.launch {
        carRepository.delete(car)
    }

    fun delete(carAvailability: CarAvailability) = viewModelScope.launch {
        availabilityRepository.delete(carAvailability)
    }

    fun deleteAll() = viewModelScope.launch {
        carRepository.deleteAll()
        availabilityRepository.deleteAll()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        // no-op
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        // no-op
    }
}