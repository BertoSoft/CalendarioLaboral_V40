package com.example.calendariolaboral_v40.modulos.vacaciones.domain.repository

import com.example.calendariolaboral_v40.modulos.vacaciones.domain.model.DatosVacaciones
import javax.inject.Inject

interface VacacionesRepository {
    suspend fun getAllVacaciones(strAno: String): List<DatosVacaciones>
    suspend fun existeVacaciones(dato: DatosVacaciones): Int

    suspend fun setVacaciones(dato: DatosVacaciones): Boolean
}