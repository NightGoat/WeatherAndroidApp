package ru.nightgoat.weather.presentation.addCity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.delegates.viewBinding
import ru.nightgoat.weather.core.utils.NAME_KEY
import ru.nightgoat.weather.databinding.FragmentAddCityBinding
import ru.nightgoat.weather.presentation.base.BaseFragment
import javax.inject.Inject

class AddCityFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val binding: FragmentAddCityBinding by viewBinding()

    private val viewModel: AddCityViewModel by viewModels(factoryProducer = { viewModelFactory })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard()
        addCityClickListener()
        cancelBtnClickListener()
        initList()
        observeViewModel()
    }

    private fun showKeyboard() {
        val inputManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (binding.addCityEdit.requestFocus()) {
            inputManager.toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

    private fun cancelBtnClickListener() {
        binding.addCityBtnCancel.setOnClickListener {
            viewModel.purgeList()
            binding.addCityEdit.clearFocus()
            navigateTo(R.id.action_navigation_addCity_to_navigation_list)
        }
    }

    private fun addCityClickListener() {
        binding.addCityBtnAdd.setOnClickListener {
            val bundle = Bundle()
            val cityName = binding.addCityEdit.text.toString()
            if (cityName.isNotEmpty()) {
                viewModel.addSearchEntity(cityName)
                viewModel.purgeList()
                binding.addCityEdit.clearFocus()
                bundle.putString(NAME_KEY, cityName)
                navigateTo(
                    R.id.action_navigation_addCity_to_navigation_list,
                    bundle
                )
            }
        }
    }

    private fun initList() {
        binding.addCityList.setOnItemClickListener { _, view, _, _ ->
            binding.addCityEdit.setText((view as TextView).text)
        }
    }

    private fun observeViewModel() {
        viewModel.searchListLiveData.observe(viewLifecycleOwner, { listOfSearches ->
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listOfSearches)
            binding.addCityList.adapter = adapter
        })
    }

}
