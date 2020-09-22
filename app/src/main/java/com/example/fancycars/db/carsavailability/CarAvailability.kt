package com.example.fancycars.db.carsavailability

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "car_availability_data_table")
@TypeConverters(AvailabilityConverter::class)
data class CarAvailability(
    @PrimaryKey
    val id: Int,
    var availability: String
)

enum class Availability(val value: String) {
    @SerializedName("In Dealership")
    IN_DEALERSHIP("In Dealership"),

    @SerializedName("Out of stock")
    OUT_OF_STOCK("Out of stock"),

    @SerializedName("Unavailable")
    UNAVAILABLE("Unavailable");

    override fun toString(): String = value
}

class AvailabilityConverter {
    @TypeConverter
    fun toAvailability(value: String) = enumValueOf<Availability>(value)

    @TypeConverter
    fun fromAvailability(value: Availability) =  value.value
}