package com.peter.currency.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.currency.data.model.Historical
import com.peter.currency.databinding.ItemHistoricalBinding

class HistoricalAdapter(val historicalList: List<Historical>) :
    RecyclerView.Adapter<HistoricalAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemHistoricalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemHistoricalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.historical = historicalList[position]
    }

    override fun getItemCount(): Int = historicalList.size
}