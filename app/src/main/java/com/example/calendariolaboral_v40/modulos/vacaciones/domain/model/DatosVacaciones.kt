package com.example.calendariolaboral_v40.modulos.vacaciones.domain.model

import java.time.LocalDate

data class DatosVacaciones(
    val id: Int,
    val fechaInicio: LocalDate,
    val fechaFinal: LocalDate,
    val totalDias: Int
)
