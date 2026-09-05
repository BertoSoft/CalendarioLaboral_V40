package com.example.calendariolaboral_v40.modulos.detallevacas.ui.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class DetalleVacasUiEstado(
    val vacasAtrasadas: Int = 0,
    val vacasDisfrutadas: Int = 0,
    val vacasPendientes: Int = 0,
    val isCargando: Boolean = false,
    val strError: String? = null
)

@HiltViewModel
class DetalleVacasViewModel@Inject constructor(): ViewModel(){

}