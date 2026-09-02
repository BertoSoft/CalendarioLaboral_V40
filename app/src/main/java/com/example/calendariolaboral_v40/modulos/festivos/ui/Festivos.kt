package com.example.calendariolaboral_v40.modulos.festivos.ui

import android.os.Bundle
import com.example.calendariolaboral_v40.core.ui.extensions.toStringRes
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.databinding.ActivityFestivosBinding
import com.example.calendariolaboral_v40.modulos.festivos.domain.model.TipoFestivo
import java.time.LocalDate

class Festivos : AppCompatActivity() {

    private lateinit var binding:  ActivityFestivosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFestivosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {
        initSp()
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







    }
}