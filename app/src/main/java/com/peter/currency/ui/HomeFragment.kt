package com.peter.currency.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.peter.currency.databinding.FragmentHomeBinding
import com.peter.currency.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getSymbols()
    }
    
    override fun onStart() {
        super.onStart()

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
    }

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
    }

    //region variable
    private val mainViewModel: MainViewModel by viewModels()
    // endregion
}