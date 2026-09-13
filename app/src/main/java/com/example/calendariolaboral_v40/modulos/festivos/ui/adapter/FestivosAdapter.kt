package com.example.calendariolaboral_v40.modulos.festivos.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.core.ui.extensions.toStringRes
import com.example.calendariolaboral_v40.core.utils.Utils
import com.example.calendariolaboral_v40.databinding.ActivityFestivosBinding
import com.example.calendariolaboral_v40.databinding.ItemRvFestivosBinding
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.TipoFestivo

class FestivosAdapter(
    private val utils: Utils,
    var onItemPulsado: ((DatosFestivos) -> Unit)? = null,
    var onItemDeletePulsado: ((DatosFestivos) -> Unit)? = null
    ): ListAdapter<DatosFestivos, FestivosAdapter.FestivosViewHolder>(DiffCallback) {
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
        holder.render(item, utils)
    }

    inner class FestivosViewHolder(
        private val binding: ItemRvFestivosBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun render(festivo: DatosFestivos, utils: Utils){
            val context = itemView.context
            val strFechaLarga = utils.fromLocalDateToFechaLarga(festivo.fecha)
            val strTipo = context.getString(festivo.tipoFestivo.toStringRes())

            // 🎯 Mapeamos los nuevos colores vibrantes específicos de las listas
            val colorRes = when(festivo.tipoFestivo){
                TipoFestivo.NACIONAL -> R.color.tag_nacional
                TipoFestivo.AUTONOMICO -> R.color.tag_autonomico
                TipoFestivo.LOCAL -> R.color.tag_local
                TipoFestivo.EXCESO_JORNADA -> R.color.tag_exceso
                TipoFestivo.CONVENIO -> R.color.tag_convenio
            }

            with(binding){
                tvFecha.text = strFechaLarga
                tvTipoFestivo.text = strTipo

                // EXTRAEMOS EL COLOR EN FORMATO INT
                val colorInt = context.getColor(colorRes)
                val colorPrimary = context.getColor(R.color.text_on_primary)

                // 1. Aplicamos el color vivo al texto al 100% de fuerza
                tvTipoFestivo.setTextColor(colorPrimary)

                // Pintamos la pastilla protectora con esa transparencia elegante
                cardChipTipo.setCardBackgroundColor(colorInt)

                // Acciones de pulsación
                cardFestivos.setOnClickListener {
                    onItemPulsado?.invoke(festivo)
                }

                ivDelete.setOnClickListener {
                    onItemDeletePulsado?.invoke(festivo)
                }
            }
        }

    }

    companion object DiffCallback: DiffUtil.ItemCallback<DatosFestivos>(){
        override fun areItemsTheSame(
            oldItem: DatosFestivos,
            newItem: DatosFestivos
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DatosFestivos,
            newItem: DatosFestivos
        ): Boolean {
            return oldItem == newItem
        }

    }
}

