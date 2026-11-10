package com.quedadapp.app.ui.comunes

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Mismo formato y zona horaria que formatearFecha() en la web
// (Europe/Madrid, es-ES): "jue, 12 nov, 13:41".
private val formateador = DateTimeFormatter
    .ofPattern("EEE, d MMM, HH:mm", Locale("es", "ES"))
    .withZone(ZoneId.of("Europe/Madrid"))

fun formatearFechaIso(iso: String): String = formateador.format(Instant.parse(iso))
