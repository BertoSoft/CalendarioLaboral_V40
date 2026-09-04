package com.example.calendariolaboral_v40.modulos.vacaciones.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v40.core.ui.extensions.toDias
import com.example.calendariolaboral_v40.core.utils.Utils
import com.example.calendariolaboral_v40.databinding.ItemRvVacacionesBinding
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.model.DatosVacaciones

class VacacionesAdapter(
    private val utils: Utils,
    var onItemPulsado: ((DatosVacaciones) -> Unit)? = null,
    var onItemDelete: ((DatosVacaciones) -> Unit)? = null
    ): ListAdapter<DatosVacaciones, VacacionesAdapter.VacacionesViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacacionesAdapter.VacacionesViewHolder {
        // 🔄 Inflamos el layout específico de la celda usando su propio Binding
        val binding = ItemRvVacacionesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VacacionesViewHolder(binding)    }

    override fun onBindViewHolder(
        holder: VacacionesAdapter.VacacionesViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.render(item, utils)
    }


    inner class VacacionesViewHolder(
    private val binding: ItemRvVacacionesBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun render( vacaciones: DatosVacaciones, utils: Utils){

            val strFechaInicio = "Del, " +
                    utils.fromLocalDateToFechaLarga(vacaciones.fechaInicio)
            val strFechaFinal = "al, " +
                    utils.fromLocalDateToFechaLarga(vacaciones.fechaFinal)
            val strDias = "Días de vacaciones, " +
                    vacaciones.totalDias.toDias()

            with(binding){
                tvFechaInicio.text = strFechaInicio
                tvFechaFin.text = strFechaFinal
                tvDias.text = strDias
            }




            itemView.setOnClickListener {
                onItemPulsado?.invoke(vacaciones)
            }

            binding.ivDelete.setOnClickListener {
                onItemDelete?.invoke(vacaciones)
            }

        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<DatosVacaciones>(){
        override fun areItemsTheSame(
            oldItem: DatosVacaciones,
            newItem: DatosVacaciones
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DatosVacaciones,
            newItem: DatosVacaciones
        ): Boolean {
            return oldItem == newItem
        }

    }
}