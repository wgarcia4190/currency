package com.example.currencyapp.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R
import com.example.currencyapp.databinding.FragmentDetailsBinding
import com.example.currencyapp.helper.Resource
import com.example.currencyapp.room.CurrencyEntity
import com.example.currencyapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var myItemsAdapter: CustomAdapter
    val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        doConversion()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRecyclerView() = binding.recyclerView.apply {
        mainViewModel.getDataset()
        myItemsAdapter = CustomAdapter()

        adapter = myItemsAdapter
        if (layoutManager == null)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doConversion() {
        val currencies =
            arrayListOf("USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNH", "HKD", "NZD")
        val date: LocalDate = LocalDate.now().minusDays(3)
        binding.prgLoading.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        mainViewModel.getConvertedBaseData(date.toString(), args.currency, currencies.toString())
        observeUi()
    }

    private fun observeUi() {
        mainViewModel.baseRatesData.observe(requireActivity()) { result ->

            when (result.status) {
                Resource.Status.SUCCESS -> {
                    if (result.data?.success == true) {
                        var list = ArrayList<CurrencyEntity>()

                        for (item in result.data.rates) {
                            list.add(
                                CurrencyEntity(
                                    date = result.data.date,
                                    from = result.data.base,
                                    to = item.key,
                                    amount = 1,
                                    result = item.value
                                )
                            )
                        }
                        myItemsAdapter.submitList(list)
                        binding.prgLoading.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    } else if (result.data?.success == false) {
                        val layout = binding.mainLayout
                        Snackbar.make(
                            layout,
                            result.data.error.info,
                            Snackbar.LENGTH_LONG
                        )
                            .setActionTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.dark_red
                                )
                            )
                            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                            .show()

                        binding.prgLoading.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                }
                Resource.Status.ERROR -> {

                    val layout = binding.mainLayout
                    Snackbar.make(
                        layout,
                        "Oopps! Something went wrong, Try again",
                        Snackbar.LENGTH_LONG
                    )
                        .setActionTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.dark_red
                            )
                        )
                        .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        .show()

                    binding.prgLoading.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }

                Resource.Status.LOADING -> {
                    binding.prgLoading.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        const val TAG: String = "DetailsFragment"
    }
}