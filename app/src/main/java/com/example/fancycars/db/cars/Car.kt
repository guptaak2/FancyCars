package com.example.fancycars.db.cars

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_data_table")
data class Car(
    @PrimaryKey
    val id: Int,
    val img: String,
    val name: String,
    val make: String,
    val model: String,
    val year: String,
    val availability: String
)