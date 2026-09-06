package com.example.calendariolaboral_v40.modulos.detallevacas.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v40.core.ui.extensions.toDias
import com.example.calendariolaboral_v40.core.utils.Utils
import com.example.calendariolaboral_v40.databinding.ItemRvDetalleBinding
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.model.DatosVacaciones

class DetalleVacasAdapter(
    private val utils: Utils
    ): ListAdapter<DatosVacaciones, DetalleVacasAdapter.DetalleVacasViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetalleVacasViewHolder {
        val binding = ItemRvDetalleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DetalleVacasViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DetalleVacasViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.render(item, utils)
    }

    inner class DetalleVacasViewHolder(
        private val binding: ItemRvDetalleBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun render(vacaciones: DatosVacaciones, util: Utils){
            val strFechaInicio = util.fromLocalDateToFechaCorta(vacaciones.fechaInicio)
            val strFechaFinal = util.fromLocalDateToFechaCorta(vacaciones.fechaFinal)
            val strPeriodo = "Del $strFechaInicio al $strFechaFinal"
            val strDias = "Días laborables: ${vacaciones.totalDias.toDias()}"

            with(binding){
                tvDetalleFechas.text = strPeriodo
                tvDetalleTotalDias.text = strDias
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