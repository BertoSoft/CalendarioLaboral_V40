package com.example.calendariolaboral_v40.modulos.festivos.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.TipoFestivo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject

data class FestivosUiEstado(
    val fecha: LocalDate? = null,
    val tipoFestivo: TipoFestivo? = null,
    val lista: List<DatosFestivos>? = null,
    val msgError: String? = null,
    val isCargando: Boolean = false
)

@HiltViewModel
class FestivosViewModel @Inject constructor(): ViewModel() {

    private val _estado = MutableStateFlow<FestivosUiEstado>(FestivosUiEstado())
    val estado: StateFlow<FestivosUiEstado> get() = _estado
}