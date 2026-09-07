package com.example.calendariolaboral_v40.modulos.home.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.databinding.ActivityHomeBinding
import com.example.calendariolaboral_v40.modulos.home.extensions.setTextosyEmojis
import com.example.calendariolaboral_v40.core.ui.extensions.animacionClick
import com.example.calendariolaboral_v40.modulos.backup.ui.Backup
import com.example.calendariolaboral_v40.modulos.calendario.ui.Calendario
import com.example.calendariolaboral_v40.modulos.excesos.ui.Excesos
import com.example.calendariolaboral_v40.modulos.festivos.ui.Festivos
import com.example.calendariolaboral_v40.modulos.vacaciones.ui.Vacaciones
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
                    val intent = Intent(this@HomeActivity, Vacaciones::class.java)
                    startActivity(intent)
                }
            }
            cardBackup.miCardView.setOnClickListener {
                cardBackup.miCardView.animacionClick{
                    val intent = Intent(this@HomeActivity, Backup::class.java)
                    startActivity(intent)
                }
            }
            cardExcesoJornadas.miCardView.setOnClickListener {
                cardExcesoJornadas.miCardView.animacionClick{
                    val intent = Intent(this@HomeActivity, Excesos::class.java)
                    startActivity(intent)                  }
            }
            cardCalendarioLaboral.miCardView.setOnClickListener {
                cardCalendarioLaboral.miCardView.animacionClick{
                    val intent = Intent(this@HomeActivity, Calendario::class.java)
                    startActivity(intent)
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
            cardCalendarioLaboral.setTextosyEmojis(R.color.calendario,"\uD83D\uDCC5", "Calendario Laboral")
        }
    }
}