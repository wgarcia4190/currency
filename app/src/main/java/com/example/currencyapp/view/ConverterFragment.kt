package com.example.currencyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.currencyapp.R
import com.example.currencyapp.databinding.FragmentCoverterBinding
import com.example.currencyapp.helper.EndPoints
import com.example.currencyapp.helper.Resource
import com.example.currencyapp.helper.Utility
import com.example.currencyapp.room.CurrencyEntity
import com.example.currencyapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ConverterFragment : Fragment() {

    private lateinit var binding: FragmentCoverterBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        setUpClickListener()
    }


    private fun initSpinner() {

        binding.spnFirstCountry.setItems(getAllCountries())
        binding.txtFirstCurrencyName.text = getCountrySymbol(binding.spnFirstCountry.text)
        binding.spnFirstCountry.setOnClickListener {
            Utility.hideKeyboard(requireContext(), it)
        }

        binding.spnFirstCountry.setOnItemSelectedListener { _, _, _, item ->
            val currencySymbol = getCountrySymbol(item)
            if (currencySymbol != null) {
                binding.txtFirstCurrencyName.text = currencySymbol
            }
        }

        binding.spnFirstCountry.setOnClickListener {
            Utility.hideKeyboard(requireContext(), it)
        }

        binding.spnSecondCountry.setItems(getAllCountries())
        binding.txtSecondCurrencyName.text = getCountrySymbol(binding.spnSecondCountry.text)

        binding.spnSecondCountry.setOnItemSelectedListener { _, _, _, item ->
            val currencySymbol = getCountrySymbol(item)
            if (currencySymbol != null) {
                binding.txtSecondCurrencyName.text = currencySymbol
            }
        }

    }

    private fun getCountrySymbol(item: Any): String? {
        val countryCode = getCountryCode(item.toString())
        return getSymbol(countryCode)
    }

    private fun getSymbol(countryCode: String?): String? {
        val availableLocales = Locale.getAvailableLocales()
        for (i in availableLocales.indices) {
            if (availableLocales[i].country == countryCode
            ) return Currency.getInstance(availableLocales[i]).currencyCode
        }
        return ""
    }

    private fun getCountryCode(countryName: String) =
        Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }

    private fun getAllCountries(): ArrayList<String> {

        val locales = Locale.getAvailableLocales()
        val countries = ArrayList<String>()
        for (locale in locales) {
            val country = locale.displayCountry
            if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
                countries.add(locale.displayCountry)
            }
        }
        countries.sort()

        return countries
    }

    private fun setUpClickListener() {

        binding.btnConvert.setOnClickListener {

            val numberToConvert = binding.etFirstCurrency.text.toString()

            if (numberToConvert.isEmpty() || numberToConvert == "0") {
                Snackbar.make(
                    binding.mainLayout,
                    "Input a value in the first text field, result will be shown in the second text field",
                    Snackbar.LENGTH_LONG
                )
                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.dark_red))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show()
            } else if (!Utility.isNetworkAvailable(context)) {
                Snackbar.make(
                    binding.mainLayout,
                    "You are not connected to the internet",
                    Snackbar.LENGTH_LONG
                )
                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.dark_red))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show()
            } else {
                doConversion()
            }
        }

        binding.swap.setOnClickListener {
            var temp = binding.spnFirstCountry.text
            binding.spnFirstCountry.text = binding.spnSecondCountry.text
            binding.spnSecondCountry.text = temp

            var tempText = binding.txtFirstCurrencyName.text
            binding.txtFirstCurrencyName.text = binding.txtSecondCurrencyName.text
            binding.txtSecondCurrencyName.text = tempText
        }

        binding.goToHistorical.setOnClickListener {
            (requireActivity() as MainActivity).navigateToHistorical()
        }
        binding.goToDetail.setOnClickListener {
            (requireActivity() as MainActivity).navigateToDetails(binding.txtFirstCurrencyName.text.toString())
        }

    }

    private fun doConversion() {

        Utility.hideKeyboard(requireContext(), requireView())

        binding.prgLoading.visibility = View.VISIBLE

        binding.btnConvert.visibility = View.GONE
        binding.lytResult.visibility = View.GONE

        val apiKey = EndPoints.API_KEY
        val from = binding.txtFirstCurrencyName.text.toString()
        val to = binding.txtSecondCurrencyName.text.toString()
        val amount = binding.etFirstCurrency.text.toString().toDouble()

        mainViewModel.getConvertedData(apiKey, from, to, amount)

        observeUi()

    }

    private fun observeUi() {
        mainViewModel.data.observe(viewLifecycleOwner) { result ->

            when (result.status) {
                Resource.Status.SUCCESS -> {
                    if (result.data?.success == true) {
                        mainViewModel.convertedRate.value = result.data.result
                        val formattedString =
                            String.format("%,.2f", mainViewModel.convertedRate.value)
                        binding.etSecondCurrency.setText(formattedString)
                        mainViewModel.insertData(
                            CurrencyEntity(
                                date = result.data.date,
                                rate = result.data.info.rate,
                                timestamp = result.data.info.timestamp,
                                amount = result.data.query.amount,
                                from = result.data.query.from,
                                to = result.data.query.to,
                                result = result.data.result
                            )
                        )

                        //stop progress bar
                        binding.prgLoading.visibility = View.GONE
                        //show button
                        binding.btnConvert.visibility = View.VISIBLE
                        binding.lytResult.visibility = View.VISIBLE
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
                        binding.btnConvert.visibility = View.VISIBLE
                        binding.lytResult.visibility = View.VISIBLE
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
                    binding.btnConvert.visibility = View.VISIBLE
                }

                Resource.Status.LOADING -> {
                    binding.prgLoading.visibility = View.VISIBLE
                    binding.btnConvert.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        const val TAG: String = "ConverterFragment"
    }
}