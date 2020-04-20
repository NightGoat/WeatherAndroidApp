package ru.nightgoat.weather.presentation.list

import android.graphics.Typeface
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_city_card.view.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.data.entity.CityEntity

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun bind(item: CityEntity, fragment: ListFragmentCallbacks){
        itemView.listCard_name.text = item.name
        itemView.listCard_country.text = item.country
        itemView.listCard_icon.typeface =
            Typeface.createFromAsset(itemView.context.assets,
                "fonts/weathericons.ttf")
        itemView.listCard_icon.text =
            fragment.getWeatherIcon(
                item.iconId,
                item.date,
                item.sunrise,
                item.sunset)
        itemView.listCard_temp.text =
            item.temp.toString().plus(itemView.context.getString(R.string.degree))
        itemView.setOnClickListener {
            fragment.setCurrentCityAndCallCityFragment(item.name, item.cityId)
            fragment.swapPositionWithFirst(item)
        }
        itemView.listCard.setCardBackgroundColor(
            ContextCompat.getColor(itemView.context, fragment.getColor(item)))
    }
}