package com.example.calendariolaboral_v40.core.data.repositoryImpl

import com.example.calendariolaboral_v40.core.data.MiSqliteHelper
import com.example.calendariolaboral_v40.core.di.IoDispatcher
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.repository.VacacionesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VacacionesRepositoryImpl @Inject constructor(
    private val dbHelper: MiSqliteHelper,
    @IoDispatcher private val coroutine: CoroutineDispatcher
): VacacionesRepository {
    override suspend fun getAllVacaciones(strAno: String): List<DatosVacaciones> {
        return withContext(coroutine){
            dbHelper.getAllVacaciones(strAno)
        }
    }

    override suspend fun existeVacaciones(dato: DatosVacaciones): Int {
        return withContext(coroutine){
            dbHelper.existeVacaciones(dato)
        }
    }

    override suspend fun setVacaciones(dato: DatosVacaciones): Boolean {
        return withContext(coroutine){
            dbHelper.setVacaciones(dato)
        }
    }
}