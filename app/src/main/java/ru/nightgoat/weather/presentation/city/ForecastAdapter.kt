package ru.nightgoat.weather.presentation.city

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.city_forecast_card.view.*
import kotlinx.android.synthetic.main.fragment_city.view.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.network.model.TimeGap
import ru.nightgoat.weather.utils.getDayOfWeekAndDate
import kotlin.math.roundToInt

class ForecastAdapter(private val fragment: CityFragmentCallbacks) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private var timeGaps = mutableListOf<TimeGap>()

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

    fun setList(list: MutableList<TimeGap>) {
        this.timeGaps = list
        notifyDataSetChanged()
    }

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @ExperimentalStdlibApi
        fun bind(item: TimeGap, fragment: CityFragmentCallbacks) {
            itemView.cityCard_date.text = getDayOfWeekAndDate(item.dt*1000)
            itemView.cityCard_temp.text = item.main.temp.roundToInt().toString()
                .plus(itemView.context.getString(R.string.degree))
            itemView.cityCard_icon.typeface =
                Typeface.createFromAsset(itemView.context.assets, "fonts/weathericons.ttf")
            itemView.cityCard_icon.text = fragment.getWeatherIcon(
                item.weather[0].id,
                item.dt,
                item.sunrise,
                item.sunset
            )
        }
    }
}