package com.example.calendariolaboral_v40.modulos.festivos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.TipoFestivo
import com.example.calendariolaboral_v40.modulos.festivos.domain.usecase.FestivosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
class FestivosViewModel @Inject constructor(
    private val useCase: FestivosUseCase
): ViewModel() {

    private val _estado = MutableStateFlow<FestivosUiEstado>(FestivosUiEstado())
    val estado: StateFlow<FestivosUiEstado> get() = _estado

    fun spAnoClick(strAno: String){
        val ano = strAno.toIntOrNull() ?: return

        _estado.update { estadoACtual ->
            estadoACtual.copy(
                isCargando = true
            )
        }

        viewModelScope.launch {
            try {
                val lista = useCase.getAllFestivosUseCase(strAno)
                _estado.update { estadoACtual ->
                    estadoACtual.copy(
                        isCargando = false,
                        msgError = null,
                        fecha = null,
                        lista = lista
                    )
                }
            } catch (e: Exception) {
                _estado.update { estadoACtual ->
                    estadoACtual.copy(
                        isCargando = false,
                        fecha =  null,
                        msgError = "Error al obtener la lista de Festivos: ${e.message}",
                        lista = null
                    )
                }
            }
        }
    }

    fun spFestivosClick(indice: Int){
        val tipoFestivo = TipoFestivo.entries.getOrNull(indice) ?: TipoFestivo.NACIONAL
        _estado.update { estadoActual ->
            estadoActual.copy(
                tipoFestivo = tipoFestivo,
                msgError = null
            )
        }
    }

    fun tvFechaClick(ano: Int, mes: Int, dia: Int) {
        if(ano < 0) return
        val fecha = LocalDate.of(ano, mes, dia)
        if(fecha != null){
            _estado.update { estadoActual ->
                estadoActual.copy(
                    fecha = fecha,
                    msgError = null
                )
            }
        }
    }

    fun btnGuardarClick() {
        _estado.update { estadoActual ->
            estadoActual.copy(isCargando = true)
        }
        viewModelScope.launch {
            try {
                val fecha = _estado.value.fecha ?: return@launch
                val tipoFestivo = _estado.value.tipoFestivo ?: return@launch
                val id = useCase.existeFestivoUseCase(DatosFestivos(-1, fecha, tipoFestivo))
                val dato = DatosFestivos(id, fecha, tipoFestivo)
                if(useCase.setFestivoUseCase(dato)){
                    val strAno = fecha.year.toString()
                    val lista = useCase.getAllFestivosUseCase(strAno)
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            lista = lista,
                            isCargando = false,
                            msgError = null
                            )
                    }

                }
                else{
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            isCargando = false,
                            msgError = "Se produjo un error al guardar los datos..."
                        )
                    }
                }
            }
            catch (e: Exception){
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        isCargando = false,
                        msgError = "Se produjo un error: ${e.message}"
                    )
                }
            }
        }
    }
}