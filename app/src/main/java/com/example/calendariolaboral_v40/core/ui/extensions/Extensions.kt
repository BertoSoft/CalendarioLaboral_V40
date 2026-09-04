package com.example.calendariolaboral_v40.core.ui.extensions

import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.TipoFestivo

fun CardView.animacionClick(onAnimacionEnd: () -> Unit){
    val animacion = AnimationUtils.loadAnimation(context, R.anim.anim_click)
    this.startAnimation(animacion)
    this.postDelayed({ onAnimacionEnd() }, 100)
}

@StringRes
fun TipoFestivo.toStringRes(): Int = when(this){
    TipoFestivo.NACIONAL -> R.string.NACIONAL
    TipoFestivo.AUTONOMICO -> R.string.AUTONOMICO
    TipoFestivo.LOCAL -> R.string.LOCAL
    TipoFestivo.EXCESO_JORNADA -> R.string.EXCESO_JORNADA
    TipoFestivo.CONVENIO -> R.string.CONVENIO
}

// Esto es un colector de funciones de extension

fun Int.toDias(): String{
    return when{
        this == 1 -> "$this Día."
        this == 0 -> "$this Días."
        this > 1 -> "$this Días."
        else -> "-- Días"
    }
}