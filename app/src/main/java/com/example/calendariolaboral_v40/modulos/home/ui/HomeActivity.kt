package com.example.calendariolaboral_v40.modulos.home.ui

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.calendariolaboral_v40.R
import com.example.calendariolaboral_v40.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {
        initMenu()
        initListeners()
    }

    private fun initListeners() {
        with(binding){
            cardFestivos.miCardView.setOnClickListener {
                val animClick = AnimationUtils.loadAnimation(cardFestivos.miCardView.context, R.anim.anim_click)
                cardFestivos.miCardView.apply {
                    startAnimation(animClick)
                    postDelayed({
                        Toast.makeText(cardFestivos.miCardView.context, "Abriendo Vacaciones...", Toast.LENGTH_SHORT).show()
                    }, 100)
                }
            }
        }
    }

    private fun initMenu() {
        // Colores de fondo y textos
        with(binding){
            cardFestivos.apply {
                miCardView.setCardBackgroundColor(getColor(R.color.festivos))
                tvEmoji.text = "\uD83C\uDF89"
                tvTexto.text ="Días \nFestivos "
            }

            cardVacaciones.apply {
                miCardView.setCardBackgroundColor(getColor(R.color.vacaciones))
                tvEmoji.text = "\uD83C\uDF34"
                tvTexto.text ="Mis \nVacaciones"
            }

            cardBackup.apply {
                miCardView.setCardBackgroundColor(getColor(R.color.backup))
                tvEmoji.text = "\uD83D\uDCBE"
                tvTexto.text ="Copia de  \nSeguridad"
            }

            cardExcesoJornadas.apply {
                miCardView.setCardBackgroundColor(getColor(R.color.exceso))
                tvEmoji.text = "⏱\uFE0F"
                tvTexto.text ="Exceso de \nJornadas"
            }

            cardCalendarioLaboral.apply {
                miCardView.setCardBackgroundColor(getColor(R.color.calendario))
                tvEmoji.text = "\uD83D\uDCBE"
                tvTexto.text ="Calendario \nLaboral"
            }
        }
    }
}