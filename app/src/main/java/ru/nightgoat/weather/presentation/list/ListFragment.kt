package ru.nightgoat.weather.presentation.list

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.di.components.DaggerListFragmentComponent
import ru.nightgoat.weather.di.components.ListFragmentComponent
import javax.inject.Inject

class ListFragment : Fragment(), ListFragmentCallbacks {

    @Inject
    lateinit var viewModel: ListViewModel

    private val sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)
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
        initList()
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
                val editText = EditText(context)
                layout.addView(editText)
                setView(layout)
                setPositiveButton(getString(R.string.add)) { _: DialogInterface, _: Int ->
                    viewModel.addCity(editText.text.toString())
                }
                setNegativeButton(getString(R.string.cancel)) { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }
                create()
                show()
            }
        }


    }

    override fun setCurrentCityAndCallCityFragment(cityName: String) {
        sharedPreferences?.edit()?.putString("cityName", cityName)?.apply()
        findNavController().navigate(R.id.action_navigation_list_to_navigation_city)
    }
}