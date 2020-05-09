package ru.nightgoat.weather.presentation.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.presentation.base.BaseFragment
import ru.nightgoat.weather.presentation.city.CityFragment
import ru.nightgoat.weather.utils.getApiKey
import ru.nightgoat.weather.widget.GoogleLikeWidgetProvider
import timber.log.Timber
import javax.inject.Inject

class ListFragment : BaseFragment(), ListFragmentCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private val adapter = ListAdapter(this)
    private lateinit var API_KEY : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!
        API_KEY = getApiKey(sharedPreferences)
        initList()
        subscribeViewModel()
        listFabListener()
        setSwipeListener()
        arguments?.getString("name").let {
            if (it != null) {
                viewModel.getCityFromApiAndPutInDB(it, chooseUnits(), API_KEY)
            }
        }
        viewModel.updateAllFromApi(chooseUnits(), API_KEY)
    }

    private fun setSwipeListener() {
        list_swipeLayout.setOnRefreshListener { viewModel.updateAllFromApi(chooseUnits(), API_KEY) }
    }

    private fun initList() {
        list_recyclerView.layoutManager = LinearLayoutManager(context)
        list_recyclerView.adapter = adapter
        initDragAndSwipe()
    }

    private fun initDragAndSwipe() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN or ItemTouchHelper.UP,
            ItemTouchHelper.LEFT
        ) {
            var adapterList = mutableListOf<CityEntity>()

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapterList = adapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteCity(adapter.getEntity(viewHolder.adapterPosition))
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewModel.updateAllInRepository(adapterList)
            }
        }).attachToRecyclerView(list_recyclerView)
    }

    private fun listFabListener() = list_fab.setOnClickListener {
        findNavController().navigate(R.id.action_navigation_list_to_navigation_addCity)
    }

    private fun subscribeViewModel() {
        with (viewModel) {
            cityListLiveData.observe(viewLifecycleOwner, Observer {
                adapter.setList(it)
            })

            snackBarLiveData.observe(viewLifecycleOwner, Observer {
                if (it == "nf")
                    Snackbar.make(list_fab, R.string.city_not_found, Snackbar.LENGTH_SHORT).show()
                else Snackbar.make(list_fab, it, Snackbar.LENGTH_SHORT).show()
            })

            refreshLiveData.observe(viewLifecycleOwner, Observer {
                list_swipeLayout.isRefreshing = it
            })

            cityIdLiveData.observe(viewLifecycleOwner, Observer {
                sharedPreferences.edit().putInt("cityId", it).apply()
            })
        }

    }

    override fun setCurrentCityAndCallCityFragment(cityName: String, cityId: Int) {
        sharedPreferences.edit().putString("cityName", cityName).putInt("cityId", cityId).apply()
        findNavController().navigate(R.id.action_navigation_list_to_navigation_city)
    }

    override fun getWeatherIcon(id: Int, dt: Long, sunrise: Long, sunset: Long): String {
        return chooseIcon(id, dt, sunrise, sunset)
    }

    override fun getColor(cityEntity: CityEntity): Int {
        return when (sharedPreferences.getInt("cityId", 0)) {
            cityEntity.cityId -> R.color.colorPrimary
            else -> R.color.colorBackgroundDark
        }
    }

    override fun swapPositionWithFirst(city: CityEntity) {
        viewModel.swapPositionWithFirst(city)
    }

    override fun updateCity(city: CityEntity) {
        viewModel.updateCityInDB(city)
    }
}