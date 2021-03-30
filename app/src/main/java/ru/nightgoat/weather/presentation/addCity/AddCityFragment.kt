package ru.nightgoat.weather.presentation.addCity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
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
        val inputManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (binding.addCityEdit.requestFocus()) {
            inputManager.toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
        binding.addCityBtnAdd.setOnClickListener {
            val bundle = Bundle()
            binding.addCityEdit.text.toString().let {
                if (it.isNotEmpty()) {
                    viewModel.addSearchEntity(it)
                    viewModel.purgeList()
                    binding.addCityEdit.clearFocus()
                    bundle.putString(NAME_KEY, it)
                    navigateTo(
                        R.id.action_navigation_addCity_to_navigation_list,
                        bundle
                    )
                }
            }
        }

        binding.addCityBtnCancel.setOnClickListener {
            viewModel.purgeList()
            binding.addCityEdit.clearFocus()
            navigateTo(R.id.action_navigation_addCity_to_navigation_list)
        }

        initList()
        observeViewModel()
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
