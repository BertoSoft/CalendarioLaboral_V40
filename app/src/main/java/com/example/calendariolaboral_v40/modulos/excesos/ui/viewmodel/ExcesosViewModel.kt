package com.example.calendariolaboral_v40.modulos.excesos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v40.modulos.excesos.domain.usecase.ExccesosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExcesosUiEstado(
    val ano: Int = 0,
    val sabados: Int = 0,
    val domingos: Int = 0,
    val nacionales: Int = 0,
    val autonomicos: Int = 0,
    val locales: Int = 0,
    val convenio: Int = 0,
    val isCargando: Boolean = false,
    val msgError: String? = null
){
    val diasAno: Int get() = if(ano > 0) java.time.Year.of(ano).length() else 365
    val diasTotales: Int get() = diasAno-sabados-domingos-nacionales-autonomicos-locales-convenio
    val horasTotales: Int get() = diasTotales * 8
    val horasTrabajo: Int get() = horasTotales - 176
    val horasSobrantes: Int get() = horasTrabajo - 1752
    val diasSobrantes: Int get() = horasSobrantes / 8
}

@HiltViewModel
class ExcesosViewModel@Inject constructor(
    private val usecase: ExccesosUseCase
): ViewModel() {

    private val _estado = MutableStateFlow<ExcesosUiEstado>(ExcesosUiEstado())
    val estado: StateFlow<ExcesosUiEstado> get() = _estado

    fun spAnoClick(strAno: String) {
        if(strAno == "") return
        _estado.update { estadoActual ->
            estadoActual.copy(
                isCargando = true
            )
        }
        viewModelScope.launch {
            try {
                val ano = strAno.toInt()
                val datos = usecase.getDatosUseCase(ano)
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        isCargando = false,
                        ano = ano,
                        sabados = datos.sabados,
                        domingos = datos.domingos,
                        nacionales = datos.nacionales,
                        autonomicos = datos.autonomicos,
                        locales = datos.locales,
                        convenio = datos.convenio,
                        msgError = null
                    )
                }
            }
            catch (e: Exception){
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        isCargando = false,
                        msgError = "Eror al obtener los datos: ${e.message}"
                    )
                }
            }
        }
    }
}