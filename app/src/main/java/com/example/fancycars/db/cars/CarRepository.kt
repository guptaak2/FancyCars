package com.example.fancycars.db.cars

/**
 * Repository class for [CarDAO]
 */
class CarRepository(private val carDAO: CarDAO) {

    val cars = carDAO.getAllCars()

    val carsSortedByName = carDAO.getAllCarsSortedByName()

    suspend fun insert(car: Car) {
        carDAO.insertCar(car)
    }

    suspend fun update(car: Car): Int {
        return carDAO.updateCar(car)
    }

    suspend fun delete(car: Car) {
        carDAO.deleteCar(car)
    }

    suspend fun deleteAll() {
        carDAO.deleteAllCars()
    }
}