package com.example.calendariolaboral_v40.modulos.festivos.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.example.calendariolaboral_v40.core.ui.extensions.toStringRes
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.core.utils.Utils
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

    @Inject lateinit var utils: Utils
    private lateinit var binding:  ActivityFestivosBinding
    private val miAdaptador by lazy { FestivosAdapter(utils) }
    private val viewModel: FestivosViewModel by viewModels()

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
        initListeners()
    }

    private fun initListeners() {
        with(binding){
            spAnos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    spView: AdapterView<*>?,
                    p1: View?,
                    indice: Int,
                    p3: Long
                ) {
                    // Esto hace que si el rv esta cargando no hacemos nada aqui
                    if(viewModel.estado.value.isCargando) return

                    val strAno = spView?.getItemAtPosition(indice)?.toString() ?: LocalDate.now().year.toString()
                    viewModel.spAnoClick(strAno)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

            spFestivos.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    viewModel.spFestivosClick(p2)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

            tvFecha.setOnClickListener {
                val fecha = LocalDate.now()
                mostrarCalendario(
                    "Elige una fecha para el día festivo",
                    fecha,
                    {ano, mes, dia -> viewModel.tvFechaClick(ano, mes, dia)}
                )
            }

            btnGuardar.setOnClickListener {
                viewModel.btnGuardarClick()
            }
        }
    }

    private fun initRv() {
        with(binding.rvFestivos){
            layoutManager = LinearLayoutManager(this@Festivos)
            adapter = miAdaptador
            setHasFixedSize(true)
        }

        miAdaptador.onItemPulsado = { festivo ->
            viewModel.itemClick(festivo)
        }

        miAdaptador.onItemDeletePulsado = { festivo ->
            viewModel.itemDeleteClick(festivo)
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

    fun dibujaUi(estado: FestivosUiEstado) {
        with(binding){
            //0 progressbar
            if(estado.isCargando){
                pbCargando.visibility = View.VISIBLE
                rvFestivos.alpha = 0.5F
            }
            else{
                pbCargando.visibility = View.GONE
                rvFestivos.alpha =  1.0F
            }

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

            // 3.- Spinner Festivos (Estructura de ifs corregida)
            val indice = TipoFestivo.entries.find { it == estado.tipoFestivo }?.ordinal
            if(cardSpFestivos.isEnabled && indice != null){
                // Si estamos editando, sincronizamos el Spinner con el índice correcto
                if(spFestivos.selectedItemPosition != indice){
                    spFestivos.setSelection(indice)
                }
            } else {
                // El reset a 0 solo ocurre cuando NO hay un festivo seleccionado para edición
                if(spFestivos.selectedItemPosition != 0){
                    spFestivos.setSelection(0)
                }
            }
        }
    }

    private fun initSp() {
        initSpAnos()
        initspFestivos()
    }

    private fun initSpAnos() {
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