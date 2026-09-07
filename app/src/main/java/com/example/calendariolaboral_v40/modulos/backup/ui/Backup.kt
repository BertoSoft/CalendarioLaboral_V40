package com.example.calendariolaboral_v40.modulos.backup.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v40.databinding.ActivityBackupBinding
import com.example.calendariolaboral_v40.modulos.backup.ui.viewmodel.BackupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Backup : AppCompatActivity() {

    private lateinit var binding: ActivityBackupBinding
    private val viewModel: BackupViewModel by viewModels()

    private val dialogoGuardar = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            result.data?.data?.let { uri -> viewModel.guardarCopia(uri) }
        }
    }

    private val dialogoAbrir = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            result.data?.data?.let { uri -> viewModel.abrirCopia(uri) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        initObservers()
    }

    private fun initObservers() {

    }

    private fun initListeners() = with(binding){
        btnGuardar.setOnClickListener {
            viewModel.setExportar()
        }
        btnAbrir.setOnClickListener {
            viewModel.setImportar()
        }
    }

}