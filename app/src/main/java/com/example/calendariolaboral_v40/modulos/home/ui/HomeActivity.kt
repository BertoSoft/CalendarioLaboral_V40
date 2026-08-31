package com.example.calendariolaboral_v40.modulos.home.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}