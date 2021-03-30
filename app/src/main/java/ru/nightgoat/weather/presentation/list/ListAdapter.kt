package ru.nightgoat.weather.presentation.list


import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.nightgoat.weather.R
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.databinding.ListCityCardBinding
import java.util.*

class ListAdapter(private val fragment: ListFragmentCallbacks) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var cityList = mutableListOf<CityEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListCityCardBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
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
        cityList[fromPosition].position = fromPosition
        cityList[toPosition].position = toPosition
        notifyItemMoved(fromPosition, toPosition)
        return cityList
    }

    inner class ListViewHolder(val binding: ListCityCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CityEntity, fragment: ListFragmentCallbacks) {
            binding.listCardName.text = item.name
            binding.listCardCountry.text = item.country
            binding.listCardIcon.typeface =
                Typeface.createFromAsset(
                    itemView.context.assets,
                    FONTS_PATH
                )
            binding.listCardIcon.text =
                fragment.getWeatherIcon(
                    item.iconId,
                    item.date,
                    item.sunrise,
                    item.sunset
                )
            binding.listCardTemp.text =
                item.temp.toString().plus(itemView.context.getString(R.string.degree))
            itemView.setOnClickListener {
                fragment.setCurrentCityAndCallCityFragment(item.name, item.cityId)
                fragment.swapPositionWithFirst(item)
            }
            binding.listCard.setCardBackgroundColor(
                ContextCompat.getColor(itemView.context, fragment.getColor(item))
            )
        }


    }

    companion object {
        private const val FONTS_PATH = "fonts/weathericons.ttf"
    }
}

