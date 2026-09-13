package com.example.calendariolaboral_v40.modulos.calendario.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.databinding.ItemRvCalendarioBinding
import com.example.calendariolaboral_v40.modulos.calendario.domain.model.DatosCalendario
import javax.inject.Inject


class CalendarioAdapter@Inject constructor(): ListAdapter<DatosCalendario, CalendarioAdapter.CalendarioViewHolder> (calendarioDiffCallBack){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarioAdapter.CalendarioViewHolder {
        val binding = ItemRvCalendarioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarioViewHolder(binding)    }

    override fun onBindViewHolder(
        holder: CalendarioAdapter.CalendarioViewHolder,
        position: Int
    ) {
        holder.render(getItem(position))    }

    inner class CalendarioViewHolder(
        private val binding: ItemRvCalendarioBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun render(dia: DatosCalendario){
            with(binding) {
                val context = itemView.context

                // 1. CONTROL DE VISIBILIDAD DE DÍAS VACÍOS
                if (dia.fecha != null) {
                    tvNumeroDia.text = dia.fecha.dayOfMonth.toString()

                    // 2. GESTIÓN DE COLORES DE TEXTO (Jerarquía e Identidad)
                    if (dia.isSabado || dia.isDomingo) {
                        // Fin de semana: Texto rojo elegante corporativo
                        tvNumeroDia.setTextColor(context.getColor(R.color.cal_weekend_text))
                        cardContenedorDia.setCardBackgroundColor(context.getColor(R.color.cal_default_bg))
                    } else {
                        if (dia.isHoy) {
                            // Si es hoy, destaca con tu azul primario estrella
                            tvNumeroDia.setTextColor(context.getColor(R.color.primary))
                        } else {
                            // Día de semana normal: Texto oscuro legible
                            tvNumeroDia.setTextColor(context.getColor(R.color.text_primary))
                        }

                        // Asignamos el fondo correspondiente al tipo de jornada
                        val colorFondo = getColorFondo(dia)
                        cardContenedorDia.setCardBackgroundColor(colorFondo)
                    }

                } else {
                    // Si el día es vacío (huecos de inicio o fin del mes)
                    tvNumeroDia.text = ""
                    // Un fondo gris sutil para marcar que no pertenecen al mes actual
                    cardContenedorDia.setCardBackgroundColor(context.getColor(R.color.cal_empty_bg))
                }
            }
        }

        fun getColorFondo(dia: DatosCalendario): Int {
            val context = itemView.context

            return when {
                dia.isVacaciones -> context.getColor(R.color.cal_vacaciones)
                dia.isNacional -> context.getColor(R.color.cal_nacional)
                dia.isAutonomico -> context.getColor(R.color.cal_autonomico)
                dia.isLocal -> context.getColor(R.color.cal_local)
                dia.isConvenio -> context.getColor(R.color.cal_convenio)
                dia.isExceso -> context.getColor(R.color.cal_exceso)
                else -> context.getColor(R.color.cal_default_bg) // Fondo por defecto (crad_surface)
            }
        }
    }

    object calendarioDiffCallBack: DiffUtil.ItemCallback<DatosCalendario>(){
        override fun areItemsTheSame(
            oldItem: DatosCalendario,
            newItem: DatosCalendario
        ): Boolean {
            return oldItem.fecha == newItem.fecha
        }

        override fun areContentsTheSame(
            oldItem: DatosCalendario,
            newItem: DatosCalendario
        ): Boolean {
            return oldItem == newItem
        }
    }
}