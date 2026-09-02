package com.example.calendariolaboral_v40.modulos.home.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.databinding.ActivityHomeBinding
import com.example.calendariolaboral_v40.modulos.home.extensions.setTextosyEmojis
import com.example.calendariolaboral_v40.core.ui.extensions.animacionClick
import com.example.calendariolaboral_v40.modulos.festivos.ui.Festivos
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {
        initTarjetas()
        initListeners()
    }

    private fun initListeners() {
        with(binding){
            cardFestivos.miCardView.setOnClickListener {
                cardFestivos.miCardView.animacionClick{
                    val intent = Intent(this@HomeActivity, Festivos::class.java)
                    startActivity(intent)
                }
            }
            cardVacaciones.miCardView.setOnClickListener {
                cardVacaciones.miCardView.animacionClick{
                    Toast.makeText(this@HomeActivity, "Abriendo Vacaciones...", Toast.LENGTH_SHORT).show()
                }
            }
            cardBackup.miCardView.setOnClickListener {
                cardBackup.miCardView.animacionClick{
                    Toast.makeText(this@HomeActivity, "Abriendo Backup...", Toast.LENGTH_SHORT).show()
                }
            }
            cardExcesoJornadas.miCardView.setOnClickListener {
                cardExcesoJornadas.miCardView.animacionClick{
                    Toast.makeText(this@HomeActivity, "Abriendo Exceso de Jornadas...", Toast.LENGTH_SHORT).show()
                }
            }
            cardCalendarioLaboral.miCardView.setOnClickListener {
                cardCalendarioLaboral.miCardView.animacionClick{
                    Toast.makeText(this@HomeActivity, "Abriendo Calendario Laboral...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initTarjetas(){
        with(binding){
            cardFestivos.setTextosyEmojis(R.color.festivos,"\uD83C\uDF89", "Días \nFestivos")
            cardVacaciones.setTextosyEmojis(R.color.vacaciones,"\uD83C\uDF34", "Mis \nVacaciones")
            cardBackup.setTextosyEmojis(R.color.backup,"\uD83D\uDCBE", "Copia de \nSeguridad")
            cardExcesoJornadas.setTextosyEmojis(R.color.exceso,"⏱\uFE0F", "Exceso de \nJornadas")
            cardCalendarioLaboral.setTextosyEmojis(R.color.calendario,"\uD83D\uDCC5", "Calendario \nLaboral")
        }
    }
}