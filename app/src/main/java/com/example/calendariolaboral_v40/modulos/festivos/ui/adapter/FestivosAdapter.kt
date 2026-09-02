package com.example.calendariolaboral_v40.modulos.festivos.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v40.databinding.ActivityFestivosBinding
import com.example.calendariolaboral_v40.databinding.ItemRvFestivosBinding
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.DatosFestivos

class FestivosAdapter: ListAdapter<DatosFestivos, FestivosAdapter.FestivosViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FestivosAdapter.FestivosViewHolder {
        // 🔄 Inflamos el layout específico de la celda usando su propio Binding
        val binding = ItemRvFestivosBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FestivosViewHolder(binding)    }

    override fun onBindViewHolder(
        holder: FestivosAdapter.FestivosViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.render(item)    }

    inner class FestivosViewHolder(
        private val binding: ItemRvFestivosBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun render(festivo: DatosFestivos){

        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<DatosFestivos>(){
        override fun areItemsTheSame(
            oldItem: DatosFestivos,
            newItem: DatosFestivos
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DatosFestivos,
            newItem: DatosFestivos
        ): Boolean {
            return oldItem == newItem
        }

    }
}

