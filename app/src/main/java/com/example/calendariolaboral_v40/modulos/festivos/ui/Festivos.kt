package com.example.calendariolaboral_v40.modulos.festivos.ui

import android.app.DatePickerDialog
import android.os.Bundle
import com.example.calendariolaboral_v40.core.ui.extensions.toStringRes
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.databinding.ActivityFestivosBinding
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.TipoFestivo
import com.example.calendariolaboral_v40.modulos.festivos.ui.adapter.FestivosAdapter
import com.example.calendariolaboral_v40.modulos.festivos.ui.viewmodel.FestivosUiEstado
import com.example.calendariolaboral_v40.modulos.festivos.ui.viewmodel.FestivosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class Festivos : AppCompatActivity() {

    private lateinit var binding:  ActivityFestivosBinding
    private val miAdaptador = FestivosAdapter()
    private val viewModel: FestivosViewModel by viewModels()
    @Inject lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFestivosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {
        initSp()
        initRv()
        initObservers()
        //initListeners()
    }

    private fun initRv() {
        with(binding.rvFestivos){
            layoutManager = LinearLayoutManager(this@Festivos)
            adapter = miAdaptador
            setHasFixedSize(true)
        }    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.estado.collect { estado ->
                    dibujaUi(estado)
                }
            }
        }
    }

    fun dibujaUi(estado: FestivosUiEstado) {
        with(binding){
            // 1.- RecyclerView
            miAdaptador.submitList(estado.lista)

            //2.- tv Fecha y btnGuardar
            if(estado.fecha == null){
                tvFecha.text = "Seleccionar Fecha \uD83D\uDCC5"
                setModoEdicion(false)
            }
            else{
                tvFecha.text = utils.fromLocalDateToFechaLarga(estado.fecha)
                setModoEdicion(true)
            }
        }
    }

    private fun initSp() {
        initspAnos()
        initspFestivos()
    }

    private fun initspAnos() {
        val ano = LocalDate.now().year
        val listaAnos = (ano + 1).downTo(2022).map{ it.toString() }
        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.item_sp_anos,
            R.id.tvSp,
            listaAnos
        )
        arrayAdapter.setDropDownViewResource(R.layout.item_sp_anos)
        binding.spAnos.adapter = arrayAdapter
        binding.spAnos.setSelection(1)
    }

    private fun initspFestivos() {
        val listaTipoFestivos = TipoFestivo.entries.map { tipo ->
            getString(tipo.toStringRes())
        }

        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.item_sp,
            R.id.tvSp, listaTipoFestivos
        )
        arrayAdapter.setDropDownViewResource(R.layout.item_sp)
        binding.spFestivos.adapter = arrayAdapter
    }

    private fun setModoEdicion(isActivo: Boolean){
        with(binding){
            cardSpFestivos.isEnabled = isActivo
            spFestivos.isEnabled = isActivo
            btnGuardar.isEnabled = isActivo

            // 🔄 El bloque post espera a que el adaptador termine de dibujar el elemento
            spFestivos.post {
                val viewInterna = spFestivos.selectedView as? android.view.ViewGroup
                val tvInterna = viewInterna?.findViewById<TextView>(R.id.tvSp)
                tvInterna?.isEnabled = isActivo
            }
        }
    }

    private fun mostrarCalendario(
        strTitulo: String,
        fecha: LocalDate,
        fechaSeleccionada: (Int, Int, Int) -> Unit
    ){
        val ano = fecha.year
        val mes = fecha.monthValue
        val dia = fecha.dayOfMonth

        val datePicker = DatePickerDialog(
            this,
            { _, anoSeleccion, mesSeleccion, diaSeleccion ->
                fechaSeleccionada(anoSeleccion, mesSeleccion + 1, diaSeleccion)
            },
            ano,
            mes,
            dia
        )

        datePicker.setOnCancelListener {
            fechaSeleccionada(-1, -1, -1)
        }

        datePicker.setTitle(strTitulo)
        datePicker.show()
    }


}