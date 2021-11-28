package ru.nightgoat.weather.presentation.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.extentions.createTypeFace
import ru.nightgoat.weather.core.utils.getDayOfWeekAndDate
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.databinding.ItemCityForecastCardBinding

class ForecastAdapter(private val fragment: CityFragmentCallbacks) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private var timeGaps = mutableMapOf<Int, ForecastEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemCityForecastCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ForecastViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return timeGaps.size
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val localGaps = timeGaps.values.toMutableList()
        localGaps.getOrNull(position)?.let {
            holder.bind(item = it, fragment = fragment)
        }
    }

    fun setList(list: MutableList<ForecastEntity>) {
        list.forEachIndexed { index, entity ->
            val savedTimeGap = timeGaps[index]
            if (savedTimeGap?.temp != entity.temp || savedTimeGap.iconId != entity.iconId) {
                timeGaps[index] = entity
                notifyItemChanged(index)
            }
        }
    }

    inner class ForecastViewHolder(val binding: ItemCityForecastCardBinding): RecyclerView.ViewHolder(binding.root) {
        @ExperimentalStdlibApi
        fun bind(item: ForecastEntity, fragment: CityFragmentCallbacks) {
            with(binding) {
                cityCardDate.text = getDayOfWeekAndDate(item.date)
                cityCardTemp.text = itemView.context.getString(R.string.temp_with_degree, item.temp)
                cityCardIcon.typeface = itemView.context.createTypeFace()
                cityCardIcon.text = fragment.getWeatherIcon(
                    item.iconId,
                    item.date,
                    DEFAULT_TIME, DEFAULT_TIME
                )
            }
        }
    }

    companion object {
        private const val FONTS_PATH = "fonts/weathericons.ttf"
        private const val DEFAULT_TIME = 0L
    }
}