package com.peter.currency.ui

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.peter.currency.databinding.FragmentHomeBinding
import com.peter.currency.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getSymbols()
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
        mainViewModel.convertResult.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    it.data?.let { _ ->
                        binding.toAmount.setText(it.data.result.toString())
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
            callConvert(binding.fromAmount.text.toString())
        }

        binding.fromAmount.textInputAsFlow()
            .debounce(100) // delay to prevent searching immediately on every character input
            .onEach { callConvert(it.toString()) }
            .launchIn(lifecycleScope)
    }

    private fun callConvert(amount: String) {
        mainViewModel.convert(
            binding.toSpinner.selectedItem.toString(),
            binding.fromSpinner.selectedItem.toString(),
            amount
        )
    }

    private fun AppCompatEditText.textInputAsFlow() = callbackFlow {
        val watcher: TextWatcher = doOnTextChanged { textInput: CharSequence?, _, _, _ ->
            this.trySend(textInput).isSuccess
        }
        awaitClose { this@textInputAsFlow.removeTextChangedListener(watcher) }
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