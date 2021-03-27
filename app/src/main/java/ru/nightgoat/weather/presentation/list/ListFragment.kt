package ru.nightgoat.weather.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.presentation.base.BaseFragment
import ru.nightgoat.weather.utils.*
import javax.inject.Inject

class ListFragment : BaseFragment(), ListFragmentCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private val adapter = ListAdapter(this)

    private val apiKey by lazy {
        getApiKey(sharedPreferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        subscribeViewModel()
        listFabListener()
        setSwipeListener()
        val units = chooseUnits()
        arguments?.getString(NAME_KEY)?.let {
            viewModel.getCityFromApiAndPutInDB(it, units, apiKey)
        }
        viewModel.updateAllFromApi(units, apiKey)
    }

    private fun setSwipeListener() {
        list_swipeLayout.setOnRefreshListener { viewModel.updateAllFromApi(chooseUnits(), apiKey) }
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
        navigateTo(R.id.action_navigation_list_to_navigation_addCity)
    }

    private fun subscribeViewModel() {
        with(viewModel) {
            cityListLiveData.observe(viewLifecycleOwner, { cities ->
                adapter.setList(cities)
            })

            snackBarLiveData.observe(viewLifecycleOwner, { snackBarText ->
                val message = getString(R.string.city_not_found)
                    .takeIf { snackBarText == NOT_FOUND_KEY } ?: snackBarText
                showSnackBar(message)
            })

            refreshLiveData.observe(viewLifecycleOwner, { isRefreshNeeded ->
                list_swipeLayout.isRefreshing = isRefreshNeeded
            })

            cityIdLiveData.observe(viewLifecycleOwner, { cityId ->
                sharedPreferences?.edit()?.putInt(CITY_ID_KEY, cityId)?.apply()
            })
        }

    }

    override fun setCurrentCityAndCallCityFragment(cityName: String, cityId: Int) {
        sharedPreferences?.edit()?.putString(CITY_NAME_KEY, cityName)?.putInt(CITY_ID_KEY, cityId)
            ?.apply()
        navigateTo(R.id.action_navigation_list_to_navigation_city)
    }

    override fun getWeatherIcon(id: Int, dt: Long, sunrise: Long, sunset: Long): String {
        return chooseIcon(id, dt, sunrise, sunset)
    }

    override fun getColor(cityEntity: CityEntity): Int {
        val city = sharedPreferences?.getInt(CITY_ID_KEY, 0)
        return if (city == cityEntity.cityId) {
            R.color.colorPrimary
        } else {
            R.color.colorBackgroundDark
        }
    }

    override fun swapPositionWithFirst(city: CityEntity) {
        viewModel.swapPositionWithFirst(city)
    }

    override fun updateCity(city: CityEntity) {
        viewModel.updateCityInDB(city)
    }
}