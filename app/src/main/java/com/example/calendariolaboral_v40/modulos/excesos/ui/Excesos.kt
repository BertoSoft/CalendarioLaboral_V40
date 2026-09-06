package com.example.calendariolaboral_v40.modulos.excesos.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.core.ui.extensions.toDias
import com.example.calendariolaboral_v40.core.ui.extensions.toHoras
import com.example.calendariolaboral_v40.databinding.ActivityExcesosBinding
import com.example.calendariolaboral_v40.modulos.excesos.ui.viewmodel.ExcesosUiEstado
import com.example.calendariolaboral_v40.modulos.excesos.ui.viewmodel.ExcesosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class Excesos : AppCompatActivity() {
    private lateinit var binding: ActivityExcesosBinding
    private val viewModel: ExcesosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcesosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {
        initSp()
        initObserves()
        initListeners()
    }

    private fun initObserves() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.estado.collect { estado ->
                    dibujaUi(estado)
                }
            }
        }
    }

    fun dibujaUi(estado: ExcesosUiEstado) = with(binding){
        // 1.- Sabados Domingos y festivos
        tvValorSabados.text = estado.sabados.toDias()
        tvValorDomingos.text = estado.domingos.toDias()
        tvValorNacionales.text = estado.nacionales.toDias()
        tvValorAutonomicos.text = estado.autonomicos.toDias()
        tvValorLocales.text = estado.locales.toDias()
        tvValorConvenio.text = estado.convenio.toDias()

        // 2.- segundo bloque
        var strTexto = "(${estado.diasAno}-${estado.sabados}-${estado.domingos}-${estado.nacionales}-${estado.autonomicos}-${estado.locales}-${estado.convenio})x8h. = ${estado.horasTotales.toHoras()}"
        tvValorHoras.text = strTexto

        strTexto = "(22 Días x 8 h.) = 176 Horas."
        tvValorVacacionesExceso.text = strTexto


        strTexto = "${estado.horasTotales} h. - 176 h. = ${estado.horasTrabajo.toHoras()}"
        tvValorTotales.text = strTexto

        strTexto = "${estado.horasTrabajo} h. - 1752 h. = ${estado.horasSobrantes.toHoras()}"
        tvValorTotales1.text = strTexto

        tvDiasExcesoFinal.text = "Días de exceso: ${estado.diasSobrantes.toDias()}"
    }

    private fun initListeners() {
        with(binding){
            spAnos.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    val strAno = spAnos.selectedItem.toString()
                    viewModel.spAnoClick(strAno)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }
    }

    private fun initSp() {
        var ano = LocalDate.now().year
        val listaAnos = ((ano + 1)downTo 2022).map{ it.toString() }
        val miAdapter = ArrayAdapter(
            this,
            R.layout.item_sp_anos,
            R.id.tvSp,
            listaAnos
        )
        miAdapter.setDropDownViewResource(R.layout.item_sp_anos)
        with(binding){
            spAnos.adapter = miAdapter
            if(spAnos.selectedItemPosition != 1){
                spAnos.setSelection(1)
            }
        }
    }
}