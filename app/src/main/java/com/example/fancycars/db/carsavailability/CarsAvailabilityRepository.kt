package com.example.fancycars.db.carsavailability

/**
 * Repository class for [CarAvailabilityDAO]
 */
class CarsAvailabilityRepository(private val carAvailabilityDAO: CarAvailabilityDAO) {

    val carsAvailability = carAvailabilityDAO.getAllCars()

    suspend fun insert(carAvailability: CarAvailability) {
        carAvailabilityDAO.insertCarAvailability(carAvailability)
    }

    suspend fun update(carAvailability: CarAvailability) {
        carAvailabilityDAO.updateCarAvailability(carAvailability)
    }

    suspend fun delete(carAvailability: CarAvailability) {
        carAvailabilityDAO.deleteCarAvailability(carAvailability)
    }

    suspend fun deleteAll() {
        carAvailabilityDAO.deleteAllCars()
    }
}