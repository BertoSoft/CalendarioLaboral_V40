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
                // 1. CONTROL DE VISIBILIDAD DE DÍAS VACÍOS
                // Comprobamos si la fecha no es nula (el último parámetro true/false del constructor)
                if (dia.fecha != null) {
                    tvNumeroDia.text = dia.fecha.dayOfMonth.toString()

                    // 2. CONTROL DE COLORES (Siempre debe llevar un else obligatorio)
                    if (dia.isSabado || dia.isDomingo) {
                        tvNumeroDia.setTextColor(Color.RED)
                        cardContenedorDia.setCardBackgroundColor(itemView.context.getColor(R.color.sp_activo))
                    } else {
                        if(dia.isHoy){
                            tvNumeroDia.setTextColor(itemView.context.getColor(R.color.azul)) // Restablece color base para días de semana
                        }
                        else{
                            tvNumeroDia.setTextColor(itemView.context.getColor(R.color.texto_excesos)) // Restablece color base para días de semana
                        }
                        val colorFondo = getColorFondo(dia)
                        cardContenedorDia.setCardBackgroundColor(colorFondo)
                    }

                    // Aquí puedes añadir tus otras validaciones visuales en el futuro:
                    // if (dia.isVacaciones) { ... } else { ... }

                } else {
                    // Si el día es vacío (huecos de inicio o fin), ocultamos la celda por completo
                    cardContenedorDia.setCardBackgroundColor(itemView.context.getColor(R.color.sp_activo))
                }
            }
        }

        fun getColorFondo(dia: DatosCalendario): Int{
            var color: Int
            if(dia.isVacaciones){
                color = itemView.context.getColor(R.color.vacaciones)
            }
            else if(dia.isNacional){
                color = itemView.context.getColor(R.color.nacional)
            }
            else if(dia.isAutonomico){
                color = itemView.context.getColor(R.color.autonomico)
            }
            else if(dia.isLocal){
                color = itemView.context.getColor(R.color.local)
            }
            else if(dia.isConvenio){
                color = itemView.context.getColor(R.color.convenio)
            }
            else{
                color = itemView.context.getColor(R.color.sp_activo)
            }
            return color
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