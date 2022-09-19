package com.peter.currency.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.peter.currency.R
import com.peter.currency.databinding.FragmentDetailsBinding
import com.peter.currency.databinding.FragmentHomeBinding
import com.peter.currency.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getHistorical()

        mainViewModel.historicalData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    it.data?.let { data ->

                        val adapter = HistoricalAdapter(data)
                        binding.historicalRec.adapter = adapter
                    }
                }
            }
        }
    }

    //region variable
    private val mainViewModel: MainViewModel by viewModels()
    // endregion
}