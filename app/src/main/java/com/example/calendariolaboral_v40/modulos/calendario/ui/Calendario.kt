package com.example.calendariolaboral_v40.modulos.calendario.ui

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.databinding.ActivityCalendarioBinding
import com.example.calendariolaboral_v40.modulos.calendario.domain.model.Meses
import java.time.LocalDate

class Calendario : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarioBinding

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

    }

    private fun initRv() {

    }

    private fun initListeners() {

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