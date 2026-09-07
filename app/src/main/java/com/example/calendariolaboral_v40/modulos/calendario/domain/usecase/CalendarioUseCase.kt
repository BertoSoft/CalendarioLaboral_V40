package com.example.calendariolaboral_v40.modulos.calendario.domain.usecase

import com.example.calendariolaboral_v40.modulos.calendario.domain.model.DatosCalendario
import com.example.calendariolaboral_v40.modulos.calendario.domain.model.Meses
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.TipoFestivo
import com.example.calendariolaboral_v40.modulos.festivos.domain.usecase.FestivosUseCase
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.usecase.VacacionesUseCase
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class CalendarioUseCase@Inject constructor(
    private val festivosUseCase: FestivosUseCase,
    private val vacacionesUseCase: VacacionesUseCase
) {
    suspend fun getListaDatosUseCase(strAno: String, strMes: String): List<DatosCalendario> {
        val listaDias = mutableListOf<DatosCalendario>()
        val ano = strAno.toInt()
        val mes = Meses.entries.indexOfFirst { it.name == strMes} + 1
        val mesBuscado = YearMonth.of(ano, mes)
        val diasMes = mesBuscado.lengthOfMonth()
        val listaFestivos = festivosUseCase.getAllFestivosUseCase(strAno)
        val listaVacaciones = vacacionesUseCase.getAllVacacionesUseCase(strAno)

        val listaFestivosMes = listaFestivos.filter { it.fecha.monthValue == mes }
        val listaVacacionesMes = listaVacaciones.filter { it.fechaInicio.monthValue == mes || it.fechaFinal.monthValue == mes }

        for(dia in 1 .. diasMes){
            val fecha = LocalDate.of(ano, mes, dia)
            val isHoy = fecha == LocalDate.now()
            val diaSemana = fecha.dayOfWeek
            val isSabado = diaSemana == DayOfWeek.SATURDAY
            val isDomingo = diaSemana == DayOfWeek.SUNDAY

            val festivoDia = listaFestivos.find { it.fecha== fecha }
            val isNacional = (festivoDia?.tipoFestivo == TipoFestivo.NACIONAL)
            val isAutonomico = (festivoDia?.tipoFestivo == TipoFestivo.AUTONOMICO)
            val isLocal = (festivoDia?.tipoFestivo == TipoFestivo.LOCAL)
            val isConvenio = (festivoDia?.tipoFestivo == TipoFestivo.CONVENIO)

            val isVacaciones = listaVacacionesMes.any {
                !fecha.isBefore(it.fechaInicio) &&
                        !fecha.isAfter(it.fechaFinal)
            }

            listaDias.add(
                DatosCalendario(
                    fecha,
                    isHoy,
                    isNacional,
                    isAutonomico,
                    isLocal,
                    isConvenio,
                    isVacaciones,
                    isSabado,
                    isDomingo,
                )
            )
        }
        return listaDias
    }
}