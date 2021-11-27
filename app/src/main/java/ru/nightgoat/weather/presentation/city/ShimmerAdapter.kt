package ru.nightgoat.weather.presentation.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nightgoat.weather.databinding.PlaceholderForecastItemBinding

class ShimmerAdapter: RecyclerView.Adapter<ShimmerAdapter.ShimmerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val binding = PlaceholderForecastItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ShimmerViewHolder(binding)
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onBindViewHolder(holder: ShimmerAdapter.ShimmerViewHolder, position: Int) {
        List(5) {
            holder
        }
    }

    override fun getItemCount(): Int {
        return FAKE_ITEMS_COUNT
    }

    inner class ShimmerViewHolder(val binding: PlaceholderForecastItemBinding): RecyclerView.ViewHolder(binding.root)

    companion object {
        const val FAKE_ITEMS_COUNT = 5
    }
}