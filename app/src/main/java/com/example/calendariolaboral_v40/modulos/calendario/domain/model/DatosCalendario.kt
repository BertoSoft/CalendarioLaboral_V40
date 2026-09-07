package com.example.calendariolaboral_v40.modulos.calendario.domain.model

import java.time.LocalDate


data class DatosCalendario(
    val fecha: LocalDate?,
    val isHoy: Boolean,
    val isNacional: Boolean,
    val isAutonomico: Boolean,
    val isLocal: Boolean,
    val isConvenio: Boolean,
    val isVacaciones: Boolean,
    val isSabado: Boolean,
    val isDomingo: Boolean,
    val isVisible: Boolean = false
)

enum class Meses{
    Enero,
    Febrero,
    Marzo,
    Abril,
    Mayo,
    Junio,
    Julio,
    Agosto,
    Septiembre,
    Octubre,
    Noviembre,
    Diciembre
}

fun Int.toMeses(): String{
    if(this !in 1..12){
        return ""
    }
    return Meses.entries[this -1].name
}