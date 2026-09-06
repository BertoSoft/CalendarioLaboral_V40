package com.example.calendariolaboral_v40.modulos.detallevacas.ui.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v40.modulos.detallevacas.domain.usecase.DetalleVacasUseCase
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v40.modulos.vacaciones.ui.Vacaciones
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetalleVacasUiEstado(
    val lista: List<DatosVacaciones>? = null,
    val vacasAtrasadas: Int = 0,
    val vacasDisfrutadas: Int = 0,
    val vacasPendientes: Int = 0,
    val isCargando: Boolean = false,
    val msgError: String? = null
)

@HiltViewModel
class DetalleVacasViewModel@Inject constructor(
    private val usecase: DetalleVacasUseCase
): ViewModel(){

    private val _estado = MutableStateFlow<DetalleVacasUiEstado>(DetalleVacasUiEstado())
    val estado: StateFlow<DetalleVacasUiEstado> get() = _estado

    fun spAnosClick(strAno: String){
            _estado.update { estadoActual ->
                estadoActual.copy(
                    isCargando = true,
                )
            }
            viewModelScope.launch {
                try {
                    val dato = usecase.getDatosUseCase(strAno)
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            isCargando = false,
                            lista = dato.lista,
                            vacasAtrasadas = dato.diasAtrasados,
                            vacasDisfrutadas = dato.diasDisfrutados,
                            vacasPendientes = dato.diasPendientes,
                            msgError = null
                        )
                    }
                }
                catch (e: Exception){
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            isCargando = false,
                            lista = null,
                            vacasAtrasadas = 0,
                            vacasDisfrutadas = 0,
                            vacasPendientes = 0,
                            msgError = "Se produjo un error: ${e.message}"
                        )
                    }
                }
            }
    }
}
