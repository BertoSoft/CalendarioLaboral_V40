package com.example.calendariolaboral_v40.modulos.excesos.domain.usecase

import com.example.calendariolaboral_v40.modulos.excesos.domain.model.DatosExceso
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.TipoFestivo
import com.example.calendariolaboral_v40.modulos.festivos.domain.repository.FestivosRepository
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class ExccesosUseCase@Inject constructor(
    private val festivosRepository: FestivosRepository
) {

    suspend fun getDatosUseCase(ano: Int): DatosExceso {
        val lista = festivosRepository.getAllFestivos(ano.toString())
        return DatosExceso(
            sabados = getSabados(ano),
            domingos = getDomingos(ano),
            lista.count{ it.tipoFestivo == TipoFestivo.NACIONAL },
            lista.count{ it.tipoFestivo == TipoFestivo.AUTONOMICO },
            lista.count{ it.tipoFestivo == TipoFestivo.LOCAL },
            lista.count{ it.tipoFestivo == TipoFestivo.CONVENIO }
        )
    }

    private fun getSabados(ano: Int): Int{
        var sabados = 0
        var fecha = LocalDate.of(ano, 1, 1)
        while (fecha.dayOfWeek != DayOfWeek.SATURDAY){
            fecha = fecha.plusDays(1)
        }
        while (fecha.year == ano){
            sabados ++
            fecha = fecha.plusWeeks(1)
        }
        return sabados
    }

    private fun getDomingos(ano: Int): Int{
        var domingos = 0
        var fecha = LocalDate.of(ano, 1, 1)
        while (fecha.dayOfWeek != DayOfWeek.SUNDAY){
            fecha = fecha.plusDays(1)
        }
        while (fecha.year == ano){
            domingos ++
            fecha = fecha.plusWeeks(1)
        }
        return domingos
    }
}