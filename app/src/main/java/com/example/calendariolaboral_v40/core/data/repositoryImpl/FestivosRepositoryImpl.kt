package com.example.calendariolaboral_v40.core.data.repositoryImpl

import com.example.calendariolaboral_v40.core.data.MiSqliteHelper
import com.example.calendariolaboral_v40.core.di.IoDispatcher
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v40.modulos.festivos.domain.repository.FestivosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FestivosRepositoryImpl @Inject constructor(
    private val dbHelper: MiSqliteHelper,
    @IoDispatcher private val coroutine: CoroutineDispatcher
): FestivosRepository {
    override suspend fun getAllFestivos(strAno: String): List<DatosFestivos> {
        return withContext(coroutine){
            dbHelper.getALlFestivos(strAno)
        }
    }

    override suspend fun existeFestivo(dato: DatosFestivos): Int {
        return withContext(coroutine){
            dbHelper.existeFestivo(dato)
        }    }

    override suspend fun setFestivo(dato: DatosFestivos): Boolean {
        return withContext(coroutine){
            dbHelper.setFestivo(dato)
        }    }

    override suspend fun delFestivos(dato: DatosFestivos): Boolean {
        return withContext(coroutine){
            dbHelper.delFestivo(dato)
        }    }


}