package com.example.currencyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.databinding.FragmentHistoricalBinding
import com.example.currencyapp.room.CurrencyEntity
import com.example.currencyapp.viewmodel.MainViewModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoricalFragment : Fragment() {
    private lateinit var binding: FragmentHistoricalBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var myItemsAdapter: CustomAdapter
    private var currencyList: List<CurrencyEntity>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        mainViewModel.ratesLiveData!!.observe(requireActivity()) {
            it?.let {
                myItemsAdapter.submitList(it)
                setUpAAChartView(it)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoricalBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG: String = "HistoricalFragment"
    }

    private fun setupRecyclerView() = binding.recyclerView.apply {
        mainViewModel.getDataset()
        myItemsAdapter = CustomAdapter()

        adapter = myItemsAdapter
        if (layoutManager == null)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    }

    private fun setUpAAChartView(currencyList: List<CurrencyEntity>) {
        var series = arrayOf(AASeriesElement())

        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .title("Historical")
            .backgroundColor("#4b2b7f")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("Tokyo")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
                AASeriesElement()
                    .name("NewYork")
                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)),
                AASeriesElement()
                    .name("London")
                    .data(arrayOf(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)),
                AASeriesElement()
                    .name("Berlin")
                    .data(arrayOf(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
            )
            )

        //The chart view object calls the instance object of AAChartModel and draws the final graphic
        binding.AAChartView!!.aa_drawChartWithChartModel(aaChartModel)

    }


}