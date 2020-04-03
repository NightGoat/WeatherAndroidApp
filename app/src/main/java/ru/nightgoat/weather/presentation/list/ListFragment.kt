package ru.nightgoat.weather.presentation.list

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.di.components.DaggerListFragmentComponent
import ru.nightgoat.weather.di.components.ListFragmentComponent
import javax.inject.Inject

class ListFragment : Fragment(), ListFragmentCallbacks {

    @Inject
    lateinit var viewModel: ListViewModel

    private lateinit var sharedPreferences: SharedPreferences
    private val adapter: ListAdapter = ListAdapter(this)

    private val injector: ListFragmentComponent = DaggerListFragmentComponent
        .builder()
        .setFragment(this)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        injector.inject(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!
        initList()
        subscribeViewModel()
        listFabListener()
    }

    private fun initList() {
        val layoutManager = LinearLayoutManager(context)
        list_recyclerView.layoutManager = layoutManager
        list_recyclerView.adapter = adapter
    }

    private fun listFabListener() = list_fab.setOnClickListener {
        context?.let { context ->
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
            with(alertDialog) {
                setTitle(getString(R.string.add_city))
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL
                val editText = EditText(context)
                layout.addView(editText)
                setView(layout)
                setPositiveButton(getString(R.string.add)) { dialog: DialogInterface, _: Int ->
                    viewModel.addCity(editText.text.toString())
                    dialog.dismiss()
                }
                setNegativeButton(getString(R.string.cancel)) { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }
                create()
                show()
            }
        }
    }

    private fun subscribeViewModel() {
        viewModel.cityListLiveData.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })
        viewModel.snackBarLiveData.observe(viewLifecycleOwner, Observer {
            Snackbar.make(list_fab, it, Snackbar.LENGTH_SHORT).show()
        })
    }

    override fun setCurrentCityAndCallCityFragment(cityName: String) {
        sharedPreferences.edit().putString("cityName", cityName).apply()
        findNavController().navigate(R.id.action_navigation_list_to_navigation_city)
    }
}