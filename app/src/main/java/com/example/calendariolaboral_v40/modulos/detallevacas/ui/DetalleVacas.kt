package com.example.calendariolaboral_v40.modulos.detallevacas.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.core.ui.extensions.toDias
import com.example.calendariolaboral_v40.core.utils.Utils
import com.example.calendariolaboral_v40.databinding.ActivityDetalleVacasBinding
import com.example.calendariolaboral_v40.modulos.detallevacas.ui.adapter.DetalleVacasAdapter
import com.example.calendariolaboral_v40.modulos.detallevacas.ui.viewmodel.DetalleVacasUiEstado
import com.example.calendariolaboral_v40.modulos.detallevacas.ui.viewmodel.DetalleVacasViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class DetalleVacas : AppCompatActivity() {

    @Inject lateinit var utils: Utils
    private lateinit var binding: ActivityDetalleVacasBinding
    private val viewModel: DetalleVacasViewModel by viewModels()
    private val miAdaptador by lazy { DetalleVacasAdapter(utils) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleVacasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {
        initSpAnos()
        initRv()
        initObserbers()
        initListeners()
    }

    private fun initListeners() {

        binding.spAnos.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                val strAno = binding.spAnos.selectedItem.toString()
                viewModel.spAnosClick(strAno)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }

    private fun initObserbers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.estado.collect { estado ->
                    dibujaUi(estado)
                }
            }
        }
    }

    fun dibujaUi(estado: DetalleVacasUiEstado) {
        with(binding){
            // 1.- isCArgando
            if(estado.isCargando){
                pbCargando.visibility = View.VISIBLE
                rvVacaciones.alpha = 0.5F
            }
            else{
                pbCargando.visibility = View.GONE
                rvVacaciones.alpha = 1.0F
            }

            //2.- Textos
            val strDiasAtrasado = estado.vacasAtrasadas.toDias()
            val strDiasDisfrutados = "Vacaciones disfrutadas: " + estado.vacasDisfrutadas.toDias()
            val strDiasPendientes = "Vacaciones pendientes: " + estado.vacasPendientes.toDias()
            tvAtrasadas.text = strDiasAtrasado
            tvDisfrutadas.text = strDiasDisfrutados
            tvPendientes.text = strDiasPendientes

            // 3.- Rv
            miAdaptador.submitList(estado.lista)
        }
    }

    private fun initRv() {
        with(binding.rvVacaciones){
            layoutManager = LinearLayoutManager(this@DetalleVacas)
            adapter = miAdaptador
            setHasFixedSize(true)
        }

    }

    private fun initSpAnos() {
        val ano = LocalDate.now().year
        val listaAnos = ((ano + 1)downTo 2022).map { it.toString() }
        val arrrayAdapter = ArrayAdapter(
            this,
            R.layout.item_sp_anos,
            R.id.tvSp,
            listaAnos
        )
        arrrayAdapter.setDropDownViewResource(R.layout.item_sp_anos)
        binding.spAnos.adapter = arrrayAdapter
        binding.spAnos.setSelection(1)
    }
}