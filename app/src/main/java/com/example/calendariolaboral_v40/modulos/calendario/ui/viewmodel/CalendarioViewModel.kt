package com.example.calendariolaboral_v40.modulos.calendario.ui.viewmodel

import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v40.core.utils.Utils
import com.example.calendariolaboral_v40.modulos.calendario.domain.model.DatosCalendario
import com.example.calendariolaboral_v40.modulos.calendario.domain.model.Meses
import com.example.calendariolaboral_v40.modulos.calendario.domain.usecase.CalendarioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

data class CalendarioUiEstado(
    val isCargando: Boolean = false,
    val msgError: String? = null,
    val listaMes: List<DatosCalendario>? = null,
    val strMes: String? = null,
    val strAno: String? = null
)

@HiltViewModel
class CalendarioViewModel@Inject constructor(
    private val utils: Utils,
    private val usecase: CalendarioUseCase
): ViewModel() {

    private val _estado = MutableStateFlow<CalendarioUiEstado>(CalendarioUiEstado())
    val estado: StateFlow<CalendarioUiEstado> get() = _estado

    fun spAnoClick(strAno: String){
        val strMes = _estado.value.strMes ?: Meses.entries[LocalDate.now().monthValue - 1].name
        _estado.update { estadoActual ->
            estadoActual.copy(
                strMes = strMes,
                strAno = strAno,
                msgError = null
            )
        }
        getListaDatosMes(strAno, strMes)
    }

    fun spMesClick(strMes: String){
        val strAno = _estado.value.strAno ?: LocalDate.now().year.toString()
        _estado.update { estadoActual ->
            estadoActual.copy(
                strMes = strMes,
                strAno = strAno,
                msgError = null
            )
        }
        getListaDatosMes(strAno, strMes)
    }

    private fun getListaDatosMes(strAno: String, strMes: String) {
        _estado.update { estadoActual ->
            estadoActual.copy(
                isCargando = true
            )
        }
        viewModelScope.launch {
            try {
                val listaDatos = usecase.getListaDatosUseCase(strAno, strMes)
                val listaDias = getListaRv(listaDatos)
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        isCargando = false,
                        listaMes = listaDias,
                        msgError = null
                    )
                }
            }
            catch (e: Exception){
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        isCargando   = false,
                        listaMes = emptyList(),
                        msgError = "Se produjo un error: ${e.message}"
                    )
                }
            }
        }
    }

    private fun getListaRv(datos: List<DatosCalendario>): List<DatosCalendario>{
        val listaDiasMes = mutableListOf<DatosCalendario>()
        if(datos.isEmpty()) return emptyList()
        val primerDiaSemana = datos.first().fecha?.dayOfWeek ?: return emptyList()
        var diasVaciosInicio = when(primerDiaSemana){
            DayOfWeek.MONDAY -> 0
            DayOfWeek.TUESDAY -> 1
            DayOfWeek.WEDNESDAY -> 2
            DayOfWeek.THURSDAY -> 3
            DayOfWeek.FRIDAY -> 4
            DayOfWeek.SATURDAY -> 5
            DayOfWeek.SUNDAY -> 6
        }

        for(i in 0 until diasVaciosInicio){
            listaDiasMes.add(DatosCalendario(
                null,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false
            ))
        }

        for(dato in datos){
            listaDiasMes.add(DatosCalendario(
                dato.fecha,
                dato.isHoy,
                dato.isNacional,
                dato.isAutonomico,
                dato.isLocal,
                dato.isConvenio,
                dato.isVacaciones,
                dato.isSabado,
                dato.isDomingo,
                true
            ))
        }
        val diasVaciosFinal = 42 - listaDiasMes.size

        for(i in 0 until  diasVaciosFinal){
            listaDiasMes.add(DatosCalendario(
                null,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false
            ))
        }
        return listaDiasMes
    }

}