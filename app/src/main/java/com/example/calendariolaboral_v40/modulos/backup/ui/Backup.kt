package com.example.calendariolaboral_v40.modulos.backup.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calendariolaboral_v40.databinding.ActivityBackupBinding
import com.example.calendariolaboral_v40.modulos.backup.ui.viewmodel.BackupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.estado.collect { estado ->
                    //Boton Exportar
                    if(estado.isExportar){
                        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "application/x-sqlite3"
                            putExtra(Intent.EXTRA_TITLE, "copia_Calendario.db")
                        }
                        dialogoGuardar.launch(intent)
                        viewModel.clearExportar()
                    }
                    // Boton Importar
                    if(estado.isImportar){
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "*/*"
                        }
                        dialogoAbrir.launch(intent)
                        viewModel.clearImportar()
                    }

                    // Mensaje de error
                    if(estado.msgError != null){
                        Toast.makeText(this@Backup, estado.msgError, Toast.LENGTH_SHORT).show()
                        viewModel.clearError()
                    }
                }
            }
        }
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