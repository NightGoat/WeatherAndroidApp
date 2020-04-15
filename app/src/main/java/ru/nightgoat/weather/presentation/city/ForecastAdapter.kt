package ru.nightgoat.weather.presentation.city

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.city_forecast_card.view.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.utils.getDayOfWeekAndDate

class ForecastAdapter(private val fragment: CityFragmentCallbacks) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private var timeGaps = mutableListOf<ForecastEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.city_forecast_card, parent, false)
        )
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

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @ExperimentalStdlibApi
        fun bind(item: ForecastEntity, fragment: CityFragmentCallbacks) {
            itemView.cityCard_date.text = getDayOfWeekAndDate(item.date*1000)
            itemView.cityCard_temp.text = item.temp.toString()
                .plus(itemView.context.getString(R.string.degree))
            itemView.cityCard_icon.typeface =
                Typeface.createFromAsset(itemView.context.assets, "fonts/weathericons.ttf")
            itemView.cityCard_icon.text = fragment.getWeatherIcon(
                item.iconId,
                item.date,
                item.sunrise,
                item.sunset
            )
        }
    }
}