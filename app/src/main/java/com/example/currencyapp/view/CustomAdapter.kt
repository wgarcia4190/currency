package com.example.currencyapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.databinding.TextRowItemBinding
import com.example.currencyapp.room.CurrencyEntity


class CustomAdapter :
    RecyclerView.Adapter<CustomAdapter.CurrencyViewHolder>() {
    private val dataSet = ArrayList<CurrencyEntity>()

    override fun getItemCount() = dataSet.size

    fun submitList(list: List<CurrencyEntity>) {
        dataSet.clear()
        dataSet.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = TextRowItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        with(holder) {
            with(dataSet[position]) {
                binding.fromValue.text = from
                binding.toValue.text = to
                binding.resultValue.text = result.toString()
                binding.dateValue.text = date

            }
        }

    }

    inner class CurrencyViewHolder(val binding: TextRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
