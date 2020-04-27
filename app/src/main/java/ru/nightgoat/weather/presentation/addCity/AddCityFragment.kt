package ru.nightgoat.weather.presentation.addCity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_city.*

import ru.nightgoat.weather.R
import ru.nightgoat.weather.presentation.base.BaseFragment
import javax.inject.Inject

class AddCityFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AddCityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddCityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imgr = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (addCity_edit.requestFocus()) {
            imgr.toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
        addCity_btn_add.setOnClickListener {
            val bundle = Bundle()
            addCity_edit.text.toString().let {
                if (it.isNotEmpty()) {
                    viewModel.addSearchEntity(it)
                    viewModel.purgeList()
                    addCity_edit.clearFocus()
                    bundle.putString("name", it)
                    findNavController().navigate(
                        R.id.action_navigation_addCity_to_navigation_list,
                        bundle
                    )
                }
            }
        }

        addCity_btn_cancel.setOnClickListener {
            viewModel.purgeList()
            addCity_edit.clearFocus()
            findNavController().navigate(R.id.action_navigation_addCity_to_navigation_list)
        }

        initList()
        observeViewModel()
    }

    private fun initList() {
        addCity_list.setOnItemClickListener { _, view, _, _ ->
            addCity_edit.setText((view as TextView).text)
        }
    }

    private fun observeViewModel() {
        viewModel.searchListLiveData.observe(viewLifecycleOwner, Observer { listOfSearches ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listOfSearches)
            addCity_list.adapter = adapter
        })
    }
}
