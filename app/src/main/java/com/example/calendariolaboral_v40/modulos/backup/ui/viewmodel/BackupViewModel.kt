package com.example.calendariolaboral_v40.modulos.backup.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v40.modulos.backup.domain.usecase.BackupUseCase
import com.example.calendariolaboral_v40.modulos.backup.ui.Backup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BackupUiEstado(
    val isExportar: Boolean = false,
    val isImportar: Boolean = false,
    val isCargando: Boolean = false,
    val msgError: String? = null
)

@HiltViewModel
class BackupViewModel@Inject constructor(
    private val backupUseCase: BackupUseCase
): ViewModel() {

    private val _estado = MutableStateFlow<BackupUiEstado>(BackupUiEstado())
    val estado: StateFlow<BackupUiEstado> get() = _estado

    fun setExportar(){
        _estado.update { estadoActual ->
            estadoActual.copy(
                isExportar = true,
            )
        }
    }

    fun setImportar(){
        _estado.update { estadoActual ->
            estadoActual.copy(
                isImportar = true,
            )
        }
    }

    fun clearExportar(){
        _estado.update { estadoActual ->
            estadoActual.copy(
                isExportar = false,
            )
        }
    }

    fun clearImportar(){
        _estado.update { estadoActual ->
            estadoActual.copy(
                isImportar = false,
            )
        }
    }

    fun clearError(){
        _estado.update { estadoActual ->
            estadoActual.copy(
                msgError = null,
            )
        }
    }

    // Logica de copia con subrrutinas
    fun guardarCopia(uri: Uri){
        _estado.update { estadoActual ->
            estadoActual.copy(
                isCargando = true,
            )
        }
        viewModelScope.launch {
            try {
                if(backupUseCase.guardarCopia(uri)){
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            isCargando = false,
                            isExportar = false,
                            isImportar = false,
                            msgError = null
                        )
                    }
                }
                else{
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            isCargando = false,
                            isExportar = false,
                            isImportar = false,
                            msgError = "Se produjo un error al guardar los datos..."
                        )
                    }
                }
            }
            catch (e: Exception){
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        isCargando = false,
                        isExportar = false,
                        isImportar = false,
                        msgError = "se produjo un error: ${e.message}"
                    )
                }
            }
        }
    }

    fun abrirCopia(uri: Uri){
        _estado.update { estadoActual ->
            estadoActual.copy(
                isCargando = true,
            )
        }
        viewModelScope.launch {
            try {
                if(backupUseCase.abrirCopia(uri)){
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            isCargando = false,
                            isExportar = false,
                            isImportar = false,
                            msgError = null
                        )
                    }
                }
                else{
                    _estado.update { estadoActual ->
                        estadoActual.copy(
                            isCargando = false,
                            isExportar = false,
                            isImportar = false,
                            msgError = "Se produjo un error al abrir el archivo de datos..."
                        )
                    }
                }
            }
            catch (e: Exception){
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        isCargando = false,
                        isExportar = false,
                        isImportar = false,
                        msgError = "Se produjo un error: ${e.message}"
                    )
                }
            }
        }
    }


}