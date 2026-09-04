package com.example.calendariolaboral_v40.modulos.vacaciones.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v40.core.utils.Utils
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.usecase.VacacionesUseCase
import com.example.calendariolaboral_v40.modulos.vacaciones.ui.Vacaciones
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

    fun itemClick(vacaciones: DatosVacaciones){
        _estado.update { estadoActual ->
            estadoActual.copy(
                fechaInicio = vacaciones.fechaInicio,
                fechaFinal = vacaciones.fechaFinal,
                isMostrarCalendario = false,
                isFechaFinActiva = true,
                isGuardarActivo = true
            )
        }
    }

    fun itemDeleteClick(vacaciones: DatosVacaciones){
        _estado.update { estadoActual ->
            estadoActual.copy(
                isCargando = true
            )
        }

        viewModelScope.launch {
            try {
                val todoOk = usecase.deleteVacacionesUseCase(vacaciones)
                if(todoOk){
                    val strAno = vacaciones.fechaInicio.year.toString()
                    val lista = usecase.getAllVacacionesUseCase(strAno)
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            fechaInicio = null,
                            fechaFinal = null,
                            lista = lista,
                            isMostrarCalendario = false,
                            isFechaFinActiva = true,
                            isGuardarActivo = true,
                            msgError = null,
                            isCargando = false
                        )
                    }
                }
                else{
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            fechaInicio = null,
                            fechaFinal = null,
                            isMostrarCalendario = false,
                            isFechaFinActiva = false,
                            isGuardarActivo = false,
                            isCargando = false,
                            msgError = "No se puede eliminar el registro"
                        )
                    }
                }
            }
            catch (e: Exception){
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        fechaInicio = null,
                        fechaFinal = null,
                        isMostrarCalendario = false,
                        isFechaFinActiva = false,
                        isGuardarActivo = false,
                        isCargando = false,
                        msgError = "No se puede eliminar el registro: ${e.message}"
                    )
                }
            }
        }
    }

    fun btnGuardarClick(){
        _estado.update { estadoActual ->
            estadoActual.copy(
                isCargando = true
            )
        }
        viewModelScope.launch {
            try {
                val fechaInicio = _estado.value.fechaInicio ?: return@launch
                val fechaFinal = _estado.value.fechaFinal ?: return@launch
                val id = usecase.existeVacacionesUseCase(DatosVacaciones(
                    -1,
                    fechaInicio,
                    fechaFinal,
                    -1
                ))
                val dato = DatosVacaciones(
                    id,
                    fechaInicio,
                    fechaFinal,
                    -1
                )
                val todoOk = usecase.setVacacionesUseCase(dato)
                if(todoOk){
                    val strAno = dato.fechaInicio.year.toString()
                    val lista = usecase.getAllVacacionesUseCase(strAno)

                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            fechaInicio = null,
                            fechaFinal = null,
                            lista = lista,
                            isFechaFinActiva = false,
                            isMostrarCalendario = false,
                            isGuardarActivo = false,
                            isCargando = false,
                            msgError = null
                        )
                    }
                }
                else{
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            msgError = "Se produjo un error al guardar el registro...",
                            isFechaFinActiva = false,
                            isMostrarCalendario = false,
                            isGuardarActivo = false,
                            isCargando = false
                        )
                    }
                }
            }
            catch (e: Exception){
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        msgError = "Se produjo un error al guardar el registro: ${e.message}",
                        isMostrarCalendario = false,
                        isFechaFinActiva = false,
                        isGuardarActivo = false,
                        isCargando = false
                    )
                }
            }
        }
    }

    fun spAnosClick(strAno: String){
        if(strAno == "") return
        _estado.update { estadoActual ->
            estadoActual.copy(
                isCargando = true
            )
        }
        viewModelScope.launch {
            try {
                val lista = usecase.getAllVacacionesUseCase(strAno)
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        lista = lista,
                        msgError = null,
                        isCargando = false
                    )
                }
            }
            catch (e: Exception){
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        lista = null,
                        msgError = "Se produjo un errror : ${e.message}",
                        isCargando = false
                    )
                }
            }
        }
    }

    fun tvFechaInicialClick(ano: Int, mes: Int, dia: Int){
        if(ano < 0)return
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

    fun tvFechaFinalClick(ano: Int, mes: Int, dia: Int){
        if(ano < 1){
            _estado.update { estadoActual ->
                estadoActual.copy(
                    isMostrarCalendario = false,
                )
            }
            return
        }
        val fechaFinal = LocalDate.of(ano, mes, dia)
        val fechaInicio = _estado.value.fechaInicio!!

        if(fechaFinal == null){
            _estado.update { estadoActual ->
                estadoActual.copy(
                    isMostrarCalendario = false,
                    isFechaFinActiva = true,
                    isGuardarActivo = false,
                    msgError = "Debes especificar una fecha final"
                )
            }
        }
        else{
            _estado.update { estadoActual ->
                estadoActual.copy(
                    isCargando = true,
                    isMostrarCalendario = false
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
                            fechaFinal = fechaFinal,
                            isMostrarCalendario = false,
                            isGuardarActivo = true,
                            msgError = null,
                            isCargando = false
                        )
                    }
                }
                else{
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            fechaFinal = null,
                            isMostrarCalendario = false,
                            isGuardarActivo = false,
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