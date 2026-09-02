package com.example.calendariolaboral_v40.modulos.festivos.domain.model

import java.time.LocalDate

data class DatosFestivos (
    val id: Int,
    val fecha: LocalDate,
    val tipoFestivo: TipoFestivo
)

enum class TipoFestivo{
    NACIONAL,
    AUTONOMICO,
    LOCAL,
    EXCESO_JORNADA,
    CONVENIO
}