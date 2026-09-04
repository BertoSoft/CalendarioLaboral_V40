package com.example.calendariolaboral_v40.modulos.festivos.domain.usecase

import com.example.calendariolaboral_v40.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v40.modulos.festivos.domain.repository.FestivosRepository
import javax.inject.Inject

class FestivosUseCase @Inject constructor(
    private val festivosRepository: FestivosRepository
) {

    suspend fun getAllFestivosUseCase(strAno: String): List<DatosFestivos> {
        val lista: List<DatosFestivos> = festivosRepository.getAllFestivos(strAno)
        return lista.sortedBy { it.fecha }
    }

    suspend fun existeFestivoUseCase(dato: DatosFestivos): Int{
        return festivosRepository.existeFestivo(dato)
    }

    suspend fun setFestivoUseCase(dato: DatosFestivos): Boolean{
        val id = existeFestivoUseCase(dato)
        val datoNuevo = dato.copy(id = id)
        return festivosRepository.setFestivo(datoNuevo)
    }

    suspend fun  delFestivoUseCase(dato: DatosFestivos): Boolean{
        return festivosRepository.delFestivos(dato)
    }
}