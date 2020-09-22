package com.example.fancycars

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fancycars.databinding.CarListItemBinding
import com.example.fancycars.db.cars.Car
import com.squareup.picasso.Picasso

/**
 * A [PagingDataAdapter] that receives PagingData<Car> as the user scrolls the page. We're using
 * this adapter as it's Google's recommended way to perform paging/infinite scroll.
 */
class CarListAdapter(private val clickListener: (Car) -> Unit) :
    PagingDataAdapter<Car, CarListAdapter.CarViewHolder>(DIFF_CALLBACK) {

    class CarViewHolder private constructor(private val binding: CarListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Car, clickListener: (Car) -> Unit) {
            binding.run {
                Picasso.with(this.root.context).load(Uri.parse(car.img)).fit().into(image)
                name.text = car.name
                if (availability.equals("In Dealership")) {
                    availability.text = "In Dealership"
                    button.text = "BUY"
                } else {
                    availability.visibility = View.GONE
                    button.text = "CHECK AVAILABILITY"
                }
                make.text = car.make
                model.text = car.model
                year.text = car.year

                button.setOnClickListener { clickListener.invoke(car) }
            }
        }

        companion object {
            fun from(parent: ViewGroup): CarViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: CarListItemBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.car_list_item, parent, false)
                return CarViewHolder(binding)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Car>() {

            override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, clickListener) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        return CarViewHolder.from(parent)
    }
}