package com.example.fancycars

import android.content.res.AssetManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fancycars.databinding.FragmentFirstBinding
import com.example.fancycars.db.cars.Car
import com.example.fancycars.db.cars.CarDatabase
import com.example.fancycars.db.cars.CarRepository
import com.example.fancycars.db.carsavailability.CarAvailability
import com.example.fancycars.db.carsavailability.CarAvailabilityDatabase
import com.example.fancycars.db.carsavailability.CarsAvailabilityRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.coroutines.launch
import java.lang.reflect.Type

/**
 * Fragment that shows a list of cars
 */
class CarsFragment : Fragment() {

    private lateinit var carListAdapter: CarListAdapter
    private lateinit var binding: FragmentFirstBinding
    private var viewModel: CarViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val carDao = context?.let { CarDatabase.getInstance(it).carDao }
        val carRepository = carDao?.let { CarRepository(it) }

        val availabilityDao =
            context?.let { CarAvailabilityDatabase.getInstance(it).carAvailabilityDAO }
        val availabilityRepository = availabilityDao?.let { CarsAvailabilityRepository(it) }

        val factory = carRepository?.let { carRepo ->
            availabilityRepository?.let { availabilityRepo ->
                CarViewModelFactory(
                    carRepo,
                    availabilityRepo
                )
            }
        }

        viewModel = factory?.let { ViewModelProvider(this, it).get(CarViewModel::class.java) }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)
        binding.apply {
            carViewModel = viewModel
            lifecycleOwner = activity
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateCars()
        populateCarsAvailability()

        view.findViewById<Button>(R.id.see_cars_button).setOnClickListener {
            // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            initRecyclerView()
            sort_button.visibility = View.VISIBLE
        }

        view.findViewById<Button>(R.id.sort_button).setOnClickListener {
            // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            displayCarsListSortedByName()
        }
    }

    private fun initRecyclerView() {
        carListAdapter = CarListAdapter { selectedCar: Car -> onCarListItemClicked(selectedCar) }
        context?.let {
            binding.recyclerView.run {
                layoutManager = LinearLayoutManager(it)
                visibility = View.VISIBLE
                adapter = carListAdapter
            }
            displayCarsList()
        }
    }

    private fun displayCarsList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.run {
                this.cars.observe(viewLifecycleOwner, {
                    carListAdapter.submitData(lifecycle, it)
                })
                seeCarsButtonText.value = "Loaded All Cars"
            }
        }
    }

    private fun displayCarsListSortedByName() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.run {
                this.carsSortedByName.observe(viewLifecycleOwner, {
                    carListAdapter.submitData(lifecycle, it)
                })
            }
        }
    }

    private fun updateCar(car: Car) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.run {
                update(car)
            }
        }
    }

    private fun onCarListItemClicked(car: Car) {
        viewModel?.run {
            carAvailabilities.observe(viewLifecycleOwner, { availabilities ->
                availabilities.forEach {
                    if (car.id == it.id) {
//                        val updatedCar = Car(
//                            car.id,
//                            car.img,
//                            car.name,
//                            car.make,
//                            car.model,
//                            car.year,
//                            it.availability
//                        )
                        Toast.makeText(context, "Selected car is: ${car.name} and is ${it.availability}", Toast.LENGTH_SHORT).show()
//                        updateCar(updatedCar)
                    }
                }
            })
        }

    }

    private fun populateCars() {
        val data = context?.assets?.readAssetsFile("cars.json")
        val reviewType: Type = object : TypeToken<MutableList<Car>>() {}.type
        data?.run {
            val cars: MutableList<Car> = Gson().fromJson(this, reviewType)
            cars.forEach {
                viewModel?.run {
                    this.insert(it)
                }
            }
        }
    }

    private fun populateCarsAvailability() {
        val data = context?.assets?.readAssetsFile("carsavailability.json")
        val reviewType: Type = object : TypeToken<MutableList<CarAvailability>>() {}.type
        data?.run {
            val availabilities: MutableList<CarAvailability> = Gson().fromJson(this, reviewType)
            availabilities.forEach {
                viewModel?.run {
                    this.insert(it)
                }
            }
        }
    }

    private fun AssetManager.readAssetsFile(fileName: String): String =
        open(fileName).bufferedReader().use { it.readText() }
}