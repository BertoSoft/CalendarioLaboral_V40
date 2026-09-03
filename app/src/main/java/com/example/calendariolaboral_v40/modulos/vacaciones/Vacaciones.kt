package com.example.calendariolaboral_v40.modulos.vacaciones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v40.databinding.ActivityVacacionesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Vacaciones: AppCompatActivity() {

    private lateinit var binding: ActivityVacacionesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVacacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}