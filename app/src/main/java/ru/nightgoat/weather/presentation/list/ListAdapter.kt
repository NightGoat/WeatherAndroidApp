package ru.nightgoat.weather.presentation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.list_city_card.view.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.data.entity.CityEntity

class ListAdapter(val fragment: ListFragmentCallbacks) :
    RecyclerView.Adapter<ListViewHolder>() {

    private var cityList = listOf<CityEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_city_card, parent, false))
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(cityList[position], fragment = fragment)
    }

    fun setList(list: MutableList<CityEntity>) {
        this.cityList = list
    }

}

