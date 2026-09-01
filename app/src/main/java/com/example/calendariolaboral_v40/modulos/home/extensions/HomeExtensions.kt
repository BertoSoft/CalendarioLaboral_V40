package com.example.calendariolaboral_v40.modulos.home.extensions

import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.calendariolaboral_v40.databinding.ItemMenuBinding


// Funciones de extension
fun ItemMenuBinding.setTextosyEmojis(
    color: Int,
    emoji: String,
    texto: String
){
    val miContexto = root.context
    miCardView.setCardBackgroundColor(ContextCompat.getColor(miContexto, color))
    tvEmoji.text = emoji
    tvTexto.text = texto
}

