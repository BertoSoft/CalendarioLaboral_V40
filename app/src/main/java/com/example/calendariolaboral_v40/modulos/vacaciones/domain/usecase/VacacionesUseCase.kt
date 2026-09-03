package com.example.calendariolaboral_v40.modulos.vacaciones.domain.usecase

import com.example.calendariolaboral_v40.modulos.vacaciones.domain.model.DatosVacaciones
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class VacacionesUseCase @Inject constructor() {

    suspend fun isFechasValidas(dato: DatosVacaciones): Boolean {

        return true
    }
}