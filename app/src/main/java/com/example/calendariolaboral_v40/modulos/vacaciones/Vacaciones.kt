package com.example.calendariolaboral_v40.modulos.vacaciones

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.core.utils.Utils
import com.example.calendariolaboral_v40.databinding.ActivityVacacionesBinding
import com.example.calendariolaboral_v40.modulos.vacaciones.viewmodel.VacacionesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class Vacaciones: AppCompatActivity() {

    @Inject lateinit var utils: Utils
    private lateinit var binding: ActivityVacacionesBinding
    private val viewModel: VacacionesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVacacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        initSp()
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
}