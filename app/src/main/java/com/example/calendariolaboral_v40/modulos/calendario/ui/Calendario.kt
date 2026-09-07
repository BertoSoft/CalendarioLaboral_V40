package com.example.calendariolaboral_v40.modulos.calendario.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.databinding.ActivityCalendarioBinding
import com.example.calendariolaboral_v40.modulos.calendario.domain.model.Meses
import com.example.calendariolaboral_v40.modulos.calendario.ui.adapter.CalendarioAdapter
import com.example.calendariolaboral_v40.modulos.calendario.ui.viewmodel.CalendarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class Calendario : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarioBinding
    private val viewModel: CalendarioViewModel by viewModels()
    private val miAdapter by lazy { CalendarioAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        initSp()
        initListeners()
        initRv()
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.estado.collect { estado ->

                    // 1.- isCArgando
                    with(binding){
                        if(estado.isCargando){
                            pbCargando.visibility = View.VISIBLE
                            rvCalendarioDias.alpha = 0.5F
                        }
                        else{
                            pbCargando.visibility = View.GONE
                            rvCalendarioDias.alpha = 1.0F
                        }
                    }

                    // 2.- RecyclerView
                    if(estado.listaMes != null){
                        miAdapter.submitList(estado.listaMes)
                    }
                }
            }
        }
    }

    private fun initRv() {
        with(binding.rvCalendarioDias){
            layoutManager = GridLayoutManager(this@Calendario, 7)
            adapter = miAdapter
            setHasFixedSize(true)
        }
    }

    private fun initListeners() {

        binding.spAnioCalendario.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                val strAno = p0?.selectedItem.toString()
                viewModel.spAnoClick(strAno)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        binding.spMesCalendario.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                val strMes = p0?.selectedItem.toString()
                viewModel.spMesClick(strMes)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun initSp() {
        initSpAnos()
        initSpMeses()
    }

    private fun initSpMeses() {
        val lista = Meses.entries.map { it.toString() }
        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.item_sp_anos,
            R.id.tvSp, lista
        )
        arrayAdapter.setDropDownViewResource(R.layout.item_sp_anos)
        binding.spMesCalendario.adapter = arrayAdapter
        val indiceMes = LocalDate.now().monthValue - 1
        binding.spMesCalendario.setSelection(indiceMes)
    }

    private fun initSpAnos() {
        val ano = LocalDate.now().year
        val listaAnos = ((ano + 1)downTo 2022).map { it.toString() }
        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.item_sp_anos,
            R.id.tvSp,
            listaAnos
        )
        arrayAdapter.setDropDownViewResource(R.layout.item_sp_anos)
        binding.spAnioCalendario.adapter = arrayAdapter
        binding.spAnioCalendario.setSelection(1)
    }
}