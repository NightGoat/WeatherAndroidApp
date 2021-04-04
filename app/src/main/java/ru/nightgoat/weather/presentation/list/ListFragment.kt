package ru.nightgoat.weather.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.delegates.viewBinding
import ru.nightgoat.weather.core.utils.CITY_ID_KEY
import ru.nightgoat.weather.core.utils.CITY_NAME_KEY
import ru.nightgoat.weather.core.utils.NAME_KEY
import ru.nightgoat.weather.core.utils.NOT_FOUND_KEY
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.databinding.FragmentListBinding
import ru.nightgoat.weather.presentation.base.BaseFragment
import javax.inject.Inject

class ListFragment : BaseFragment(), ListFragmentCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val binding: FragmentListBinding by viewBinding()

    private val viewModel: ListViewModel by viewModels(factoryProducer = { viewModelFactory })

    private val adapter = ListAdapter(this)

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
        arguments?.getString(NAME_KEY)?.let {
            viewModel.getCityFromApiAndPutInDB(it, units, apiKey)
        }
        viewModel.updateAllFromApi(units, apiKey)
    }

    private fun setSwipeListener() {
        binding.listSwipeLayout.setOnRefreshListener {
            viewModel.updateAllFromApi(
                units,
                apiKey
            )
        }
    }

    private fun initList() {
        binding.listRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.listRecyclerView.adapter = adapter
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
        }).attachToRecyclerView(binding.listRecyclerView)
    }

    private fun listFabListener() = binding.listFab.setOnClickListener {
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
                showSnackBar(text = message, view = binding.listFab)
            })

            refreshLiveData.observe(viewLifecycleOwner, { isRefreshNeeded ->
                binding.listSwipeLayout.isRefreshing = isRefreshNeeded
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