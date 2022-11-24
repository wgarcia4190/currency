package com.example.currencyapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyapp.R
import com.example.currencyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun navigateToHistorical() {
        val fragment = HistoricalFragment()

        supportFragmentManager.beginTransaction()
            .addToBackStack(HistoricalFragment.TAG)
            .replace(R.id.main_content, fragment, HistoricalFragment.TAG)
            .commit()
    }

    fun navigateToDetails(currency: String) {
        val fragment = DetailsFragment()
        val args = Bundle().apply {
            putString("currency", currency)
        }
        fragment.arguments = args
        supportFragmentManager.beginTransaction()
            .addToBackStack(DetailsFragment.TAG)
            .replace(R.id.main_content, fragment, DetailsFragment.TAG)
            .commit()
    }

}
