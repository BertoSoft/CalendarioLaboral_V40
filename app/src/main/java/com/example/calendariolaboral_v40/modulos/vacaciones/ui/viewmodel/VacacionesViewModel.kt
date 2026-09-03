package com.example.calendariolaboral_v40.modulos.vacaciones.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v40.core.utils.Utils
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.usecase.VacacionesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class VacacionesUiEstado(
    val fechaInicio: LocalDate? = null,
    val fechaFinal: LocalDate? = null,
    val lista: List<DatosVacaciones>? = null,
    val msgError: String? = null,
    val isCargando: Boolean = false,
    val isFechaFinActiva: Boolean = false,
    val isGuardarActivo: Boolean = false,
    val isMostrarCalendario: Boolean = false
)

@HiltViewModel
class VacacionesViewModel @Inject constructor(
    private val usecase: VacacionesUseCase
): ViewModel() {

    private val _estado = MutableStateFlow<VacacionesUiEstado>(VacacionesUiEstado())
    val estado: StateFlow<VacacionesUiEstado> get() = _estado

    fun tvFechaInicialClick(ano: Int, mes: Int, dia: Int){
        val fechaInicio: LocalDate? = LocalDate.of(ano, mes, dia)
        val fechaFinal = _estado.value.fechaFinal

        if(fechaInicio == null) {
            _estado.update { estadoActual ->
                estadoActual.copy(
                    fechaInicio = null,
                    isMostrarCalendario = false,
                    isFechaFinActiva = false,
                    isGuardarActivo = false,
                    msgError = "Debes seleccionar una fecha de inicio...",
                )
            }
        }
        // Mostramos calendario
        else if(fechaFinal == null){
            _estado.update { estadoActual ->
                estadoActual.copy(
                    fechaInicio = fechaInicio,
                    isMostrarCalendario = true,
                    isFechaFinActiva = true,
                    msgError = null,
                )
            }
        }
        // Comprobamos las dfos fechas
        else{
            _estado.update { estadoActual ->
                estadoActual.copy(
                    isCargando = true,
                )
            }

            viewModelScope.launch {
                val todoOk = usecase.isFechasValidas(DatosVacaciones(
                    -1,
                    fechaInicio,
                    fechaFinal,
                    -1
                    )
                )
                if(todoOk){
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            fechaInicio = fechaInicio,
                            isFechaFinActiva = true,
                            isMostrarCalendario = false,
                            isCargando = false,
                            msgError = null
                        )
                    }
                }
                else{
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            fechaInicio = null,
                            isFechaFinActiva = false,
                            isMostrarCalendario = false,
                            isCargando = false,
                            msgError = "La fecha final no puede ser anterior a la inicial..."
                        )
                    }
                }
            }
        }
    }

    fun clearMsgError(){
        _estado.update { estadoActual ->
            estadoActual.copy(
                msgError = null,
            )
        }
    }

    fun clearMostrarCalendario(){
        _estado.update { estadoActual ->
            estadoActual.copy(
                isMostrarCalendario = false,
            )
        }
    }

}