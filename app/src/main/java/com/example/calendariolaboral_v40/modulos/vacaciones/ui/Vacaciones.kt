package com.example.calendariolaboral_v40.modulos.vacaciones.ui

import android.app.DatePickerDialog
import android.app.LocaleConfig
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.core.utils.Utils
import com.example.calendariolaboral_v40.databinding.ActivityVacacionesBinding
import com.example.calendariolaboral_v40.modulos.vacaciones.ui.adapter.VacacionesAdapter
import com.example.calendariolaboral_v40.modulos.vacaciones.ui.viewmodel.VacacionesUiEstado
import com.example.calendariolaboral_v40.modulos.vacaciones.ui.viewmodel.VacacionesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class Vacaciones: AppCompatActivity() {

    @Inject lateinit var utils: Utils
    private lateinit var binding: ActivityVacacionesBinding
    private val viewModel: VacacionesViewModel by viewModels()
    private val miAdaptador by lazy { VacacionesAdapter(utils) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVacacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        initSp()
        initRv()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        with(binding){
            cardFechaInicial.setOnClickListener {
                val fecha = LocalDate.now()
                mostrarCalendario("Fecha inicial del periodo\n", fecha){ ano, mes, dia ->
                    viewModel.tvFechaInicialClick(ano, mes , dia)
                }
            }

            cardFechaFinal.setOnClickListener {
                val fecha = LocalDate.now()
                mostrarCalendario("Fecha final del periodo\n", fecha){ ano, mes, dia ->
                    viewModel.tvFechaFinalClick(ano, mes, dia)
                }
            }

            spAnos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    val strAno = p0?.selectedItem.toString()
                    viewModel.spAnosClick(strAno)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

            btnGuardar.setOnClickListener {
                viewModel.btnGuardarClick()
            }
        }

    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.estado.collect { estado ->
                    dibujaUi(estado)
                }
            }
        }
    }

    fun dibujaUi(estado: VacacionesUiEstado) {
        with(binding){
            // 0.- ProgessBar
            if(estado.isCargando){
                pbCargando.visibility = View.VISIBLE
                rvVacaciones.alpha = 0.5F
            }
            else{
                pbCargando.visibility = View.GONE
                rvVacaciones.alpha = 1.0F
            }

            // 1.- RecyclerView
            miAdaptador.submitList(estado.lista)

            // 2.- Fechas
            var strFechaInicio = ""
            var strFechaFinal = ""
            if(estado.fechaInicio != null){
                strFechaInicio = utils.fromLocalDateToFechaCorta(estado.fechaInicio)
            }
            if(estado.fechaFinal != null){
                strFechaFinal = utils.fromLocalDateToFechaCorta(estado.fechaFinal)
            }
            tvFechaInicio.text = strFechaInicio.ifBlank { "--/--/----" }
            tvFechaFinal.text = strFechaFinal.ifBlank { "--/--/----" }
            cardFechaFinal.isEnabled = estado.isFechaFinActiva

            // 3.- Si la fechaFinal esta habilitada y su valor es "", lanzamos mostrarcalendario
            val fecha = LocalDate.now()
            if(estado.isMostrarCalendario){
                mostrarCalendario( "Fecha final del periodo\n", fecha){ ano, mes, dia ->
                    viewModel.tvFechaFinalClick(ano, mes, dia)
                    viewModel.clearMostrarCalendario()
                }
            }

            // 3. Controlar el estado del botón guardar
            btnGuardar.isEnabled = estado.isGuardarActivo

            // 4. Gestionar los mensajes de error de negocio si existen
            if(estado.msgError != null){
                Toast.makeText(
                    this@Vacaciones,
                    estado.msgError,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearMsgError()
            }


        }
    }

    private fun initRv() {
        with(binding.rvVacaciones){
            layoutManager = LinearLayoutManager(this@Vacaciones)
            adapter = miAdaptador
            setHasFixedSize(true)
        }

        miAdaptador.onItemPulsado  = { vacaciones ->
            viewModel.itemClick(vacaciones)
        }

        miAdaptador.onItemDelete = { vacaciones ->
            viewModel.itemDeleteClick(vacaciones)
        }

    }

    private fun initSp() {
        initSpAno()
    }

    private fun initSpAno() {
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