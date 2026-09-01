package com.example.calendariolaboral_v40.core.ui.extensions

import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import com.example.calendariolaboral_v40.R

fun CardView.animacionClick(onAnimacionEnd: () -> Unit){
    val animacion = AnimationUtils.loadAnimation(context, R.anim.anim_click)
    this.startAnimation(animacion)
    this.postDelayed({ onAnimacionEnd() }, 100)
}
