package ru.nightgoat.weather.presentation.list

import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_city_card.view.*
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.utils.weatherIcon


class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun bind(item: CityEntity, fragment: ListFragmentCallbacks){
        itemView.listCard_name.text = item.name
        itemView.listCard_icon.typeface = Typeface.createFromAsset(itemView.context.assets, "fonts/weathericons.ttf")
        itemView.listCard_icon.text = weatherIcon(item.iconId, item.sunrise, item.sunset)
        itemView.listCard_temp.text = item.temp.toString()
        itemView.setOnClickListener {
            fragment.setCurrentCityAndCallCityFragment(item.name)
        }
    }
}