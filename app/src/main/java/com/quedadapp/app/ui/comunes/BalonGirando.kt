package com.quedadapp.app.ui.comunes

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.quedadapp.app.ui.theme.Ink

// Mismo balón que BallMark.tsx en la web (círculo exterior fijo +
// pentágono central y 5 costuras que giran sobre sí mismas), redibujado
// a mano con Canvas en vez de un vector estático, para poder animarlo.
// Las coordenadas son las mismas que en la web (viewBox 200×200,
// centro en 100,100), solo reescaladas al tamaño real del composable.
@Composable
fun BalonGirando(modifier: Modifier = Modifier, color: Color = Ink) {
    val transicionInfinita = rememberInfiniteTransition(label = "balonInfinito")
    val angulo by transicionInfinita.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(10_000, easing = LinearEasing)),
        label = "anguloBalon",
    )

    Canvas(modifier = modifier) {
        val centro = Offset(size.width / 2f, size.height / 2f)
        val radioExterior = size.minDimension / 2f * 0.92f
        val factor = radioExterior / 92f
        val grosor = (size.minDimension * 0.0125f).coerceAtLeast(1f)

        fun punto(x: Float, y: Float) = Offset(centro.x + (x - 100f) * factor, centro.y + (y - 100f) * factor)

        drawCircle(color = color, radius = radioExterior, center = centro, style = Stroke(width = grosor))

        // Solo este grupo (pentágono + costuras) gira; el círculo de
        // fuera se queda quieto, igual que en la web.
        rotate(degrees = angulo, pivot = centro) {
            val pentagono = Path().apply {
                val v1 = punto(100f, 70f)
                val v2 = punto(128.5f, 90.7f)
                val v3 = punto(117.6f, 124.3f)
                val v4 = punto(82.4f, 124.3f)
                val v5 = punto(71.5f, 90.7f)
                moveTo(v1.x, v1.y)
                lineTo(v2.x, v2.y)
                lineTo(v3.x, v3.y)
                lineTo(v4.x, v4.y)
                lineTo(v5.x, v5.y)
                close()
            }
            drawPath(pentagono, color = color, style = Stroke(width = grosor, join = StrokeJoin.Round))

            val costuras = listOf(
                punto(100f, 70f) to punto(100f, 18f),
                punto(128.5f, 90.7f) to punto(178f, 74.7f),
                punto(117.6f, 124.3f) to punto(148.2f, 166.3f),
                punto(82.4f, 124.3f) to punto(51.8f, 166.3f),
                punto(71.5f, 90.7f) to punto(22f, 74.7f),
            )
            costuras.forEach { (desde, hasta) ->
                drawLine(color = color, start = desde, end = hasta, strokeWidth = grosor, cap = StrokeCap.Round)
            }
        }
    }
}
