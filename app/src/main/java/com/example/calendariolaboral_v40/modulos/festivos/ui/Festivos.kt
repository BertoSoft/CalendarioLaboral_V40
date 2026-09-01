package com.example.calendariolaboral_v40.modulos.festivos.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.databinding.ActivityFestivosBinding

class Festivos : AppCompatActivity() {

    private lateinit var binding:  ActivityFestivosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFestivosBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}