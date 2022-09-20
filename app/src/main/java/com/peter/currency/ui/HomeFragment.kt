package com.peter.currency.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.peter.currency.R
import com.peter.currency.databinding.FragmentHomeBinding
import com.peter.currency.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        mainViewModel.symbolsData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    it.data?.let { _ ->
                        val symbolsList = it.data.symbols.keys.toTypedArray()
                        setupSpinner(symbolsList)
                    }
                }
            }
        }

        binding.detailsBtn.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment()
            )
        }
        binding.swapBtn.setOnClickListener {
            val temp = binding.fromSpinner.selectedItemPosition
            binding.fromSpinner.apply {
                setSelection(binding.toSpinner.selectedItemPosition)
            }
            binding.toSpinner.apply {
                setSelection(temp)
            }
            callConvert()
        }

    }

    private fun callConvert() {
        mainViewModel.convert(
            binding.toSpinner.selectedItem.toString(),
            binding.fromSpinner.selectedItem.toString()
        )
    }

    @OptIn(FlowPreview::class)
    private fun setupSpinner(symbolsList: Array<String>) {
        val aa = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            symbolsList
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(binding.fromSpinner)
        {
            adapter = aa
            setSelection(0, false)
        }

        with(binding.toSpinner)
        {
            adapter = aa
            setSelection(0, false)
        }

        mainViewModel.fromAmount.debounce(100).onEach {
            callConvert()
        }.launchIn(lifecycleScope)


        binding.fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                callConvert()
            }

        }
        binding.toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                callConvert()
            }

        }
    }

    //region variable
    private val mainViewModel: MainViewModel by activityViewModels()
    lateinit var binding: FragmentHomeBinding
    // endregion
}