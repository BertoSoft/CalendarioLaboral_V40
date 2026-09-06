package com.example.calendariolaboral_v40.modulos.detallevacas.domain.usecase

import com.example.calendariolaboral_v40.modulos.detallevacas.domain.model.DatosDetalleVacas
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.usecase.VacacionesUseCase
import javax.inject.Inject

class DetalleVacasUseCase@Inject constructor(
    private val vacacionesUseCase: VacacionesUseCase
) {

    suspend fun getDatosUseCase(strAno: String):DatosDetalleVacas{
        var diasAtrasados = 9 // Días atrasados al comienzo año 2022
        val anoActual = strAno.toInt()

        for (ano in 2022 until anoActual){
            val strAno = ano.toString()
            val diasDisfrutados = getDiasDisfrutados(strAno)
            diasAtrasados += (22 - diasDisfrutados)
        }
        val lista = vacacionesUseCase.getAllVacacionesUseCase(strAno)
        val diasDisfrutados = lista.sumOf { it.totalDias }
        val diasPendientes = (diasAtrasados + 22) - diasDisfrutados

        return DatosDetalleVacas(
            lista = lista,
            diasAtrasados = diasAtrasados,
            diasDisfrutados = diasDisfrutados,
            diasPendientes = diasPendientes
        )
    }

    suspend fun getDiasDisfrutados(strAno: String): Int{
        val lista = vacacionesUseCase.getAllVacacionesUseCase(strAno)
        return lista.sumOf { it.totalDias }
    }
}