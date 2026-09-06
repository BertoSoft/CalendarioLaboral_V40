package com.example.calendariolaboral_v40.modulos.detallevacas.domain.model

import com.example.calendariolaboral_v40.modulos.vacaciones.domain.model.DatosVacaciones

data class DatosDetalleVacas(
    val lista: List<DatosVacaciones>,
    val diasAtrasados: Int,
    val diasDisfrutados: Int,
    val diasPendientes: Int
)
