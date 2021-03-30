package ru.nightgoat.weather.presentation.city

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.utils.getDayOfWeekAndDate
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.databinding.CityForecastCardBinding

class ForecastAdapter(private val fragment: CityFragmentCallbacks) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private var timeGaps = mutableListOf<ForecastEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = CityForecastCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return timeGaps.size
    }

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(item = timeGaps[position], fragment = fragment)
    }

    fun setList(list: MutableList<ForecastEntity>) {
        this.timeGaps = list
        notifyDataSetChanged()
    }

    inner class ForecastViewHolder(private val binding: CityForecastCardBinding) : RecyclerView.ViewHolder(binding.root) {

        @ExperimentalStdlibApi
        fun bind(item: ForecastEntity, fragment: CityFragmentCallbacks) {
            binding.cityCardDate.text = getDayOfWeekAndDate(item.date)
            binding.cityCardTemp.text = item.temp.toString()
                .plus(itemView.context.getString(R.string.degree))
            binding.cityCardIcon.typeface =
                Typeface.createFromAsset(itemView.context.assets, FONTS_PATH)
            binding.cityCardIcon.text = fragment.getWeatherIcon(
                item.iconId,
                item.date,
                DEFAULT_TIME, DEFAULT_TIME
            )
        }
    }

    companion object {
        private const val FONTS_PATH = "fonts/weathericons.ttf"
        private const val DEFAULT_TIME = 0L
    }
}