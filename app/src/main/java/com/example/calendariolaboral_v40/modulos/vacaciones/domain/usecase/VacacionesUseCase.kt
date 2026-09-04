package com.example.calendariolaboral_v40.modulos.vacaciones.domain.usecase

import com.example.calendariolaboral_v40.core.data.repositoryImpl.VacacionesRepositoryImpl
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.TipoFestivo
import com.example.calendariolaboral_v40.modulos.festivos.domain.repository.FestivosRepository
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.repository.VacacionesRepository
import com.example.calendariolaboral_v40.modulos.vacaciones.ui.Vacaciones
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class VacacionesUseCase @Inject constructor(
    private val vacacionesRepository: VacacionesRepository,
    private val festivosRepository: FestivosRepository
) {

    fun isFechasValidas(dato: DatosVacaciones): Boolean {
        if(dato.fechaInicio.isAfter(dato.fechaFinal)) return false
        if(dato.fechaInicio == dato.fechaFinal) return true
        return true
    }

    suspend fun getAllVacacionesUseCase(strAno: String): List<DatosVacaciones>{
        var diasTotales = 0
        val listaSinDiasTotales = vacacionesRepository.getAllVacaciones(strAno)
        val listaFestivos = festivosRepository.getAllFestivos(strAno)
        var isSabado = false
        var isDomingo = false
        var fecha: LocalDate
        val listaDiasTotales = listaSinDiasTotales.map { periodoVacaciones ->
            fecha = periodoVacaciones.fechaInicio
            diasTotales = 0
            while (fecha.isBefore(periodoVacaciones.fechaFinal) || fecha == periodoVacaciones.fechaFinal){
                isSabado = false
                isDomingo = false
                if(fecha.dayOfWeek == DayOfWeek.SATURDAY) isSabado = true
                if(fecha.dayOfWeek == DayOfWeek.SUNDAY) isDomingo = true
                if(!isSabado && !isDomingo){
                    val id = festivosRepository.existeFestivo(DatosFestivos(-1, fecha, TipoFestivo.NACIONAL))
                    if(id < 0) diasTotales += 1
                }
                fecha = fecha.plusDays(1)
            }
            periodoVacaciones.copy(totalDias = diasTotales)
        }
        return listaDiasTotales.sortedBy { it.fechaInicio }
    }

    suspend fun existeVacacionesUseCase(dato: DatosVacaciones): Int{
        return vacacionesRepository.existeVacaciones(dato)
    }

    suspend fun setVacacionesUseCase(dato: DatosVacaciones): Boolean{
        return vacacionesRepository.setVacaciones(dato)
    }

    suspend fun deleteVacacionesUseCase(vacaciones: DatosVacaciones): Boolean{
        return vacacionesRepository.deleteVacaciones(vacaciones)
    }
}