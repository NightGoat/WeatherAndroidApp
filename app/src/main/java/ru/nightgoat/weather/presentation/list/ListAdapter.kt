package ru.nightgoat.weather.presentation.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.list_city_card.view.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.data.entity.CityEntity
import java.util.*

class ListAdapter(private val fragment: ListFragmentCallbacks) :
    RecyclerView.Adapter<ListViewHolder>() {

    private var cityList = mutableListOf<CityEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_city_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(cityList[position], fragment = fragment)
    }

    fun setList(list: MutableList<CityEntity>) {
        this.cityList = list
        notifyDataSetChanged()
    }

    fun getEntity(adapterPosition: Int): CityEntity {
        return cityList[adapterPosition]
    }

    fun onRowMoved(fromPosition: Int, toPosition: Int): MutableList<CityEntity> {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(cityList, i, i + 1)

            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(cityList, i, i - 1)
            }
        }
        Log.d(TAG, "onRowMoved")
        cityList[fromPosition].position = fromPosition
        cityList[toPosition].position = toPosition
        notifyItemMoved(fromPosition, toPosition)
        return cityList
    }

    fun getList(): MutableList<CityEntity> {
        return cityList
    }

    companion object {
        @JvmStatic
        val TAG = ListAdapter::class.java.simpleName
    }
}

