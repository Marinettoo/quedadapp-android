package com.quedadapp.app.ui.registro

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Meta
import com.quedadapp.app.ui.theme.Paper

@Composable
fun RegistroEnviadoScreen(onVolverALogin: () -> Unit) {
    // El círculo entra con un rebote elástico y el check se dibuja
    // trazo a trazo justo después, en vez de aparecer los dos de golpe.
    val escalaCirculo = remember { Animatable(0f) }
    val progresoCheck = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        escalaCirculo.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
        progresoCheck.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .scale(escalaCirculo.value)
                .border(2.dp, Ink, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(44.dp)) {
                val ancho = size.width
                val alto = size.height
                val inicio = Offset(ancho * 0.15f, alto * 0.55f)
                val medio = Offset(ancho * 0.42f, alto * 0.78f)
                val fin = Offset(ancho * 0.88f, alto * 0.25f)

                val trazoCompleto = Path().apply {
                    moveTo(inicio.x, inicio.y)
                    lineTo(medio.x, medio.y)
                    lineTo(fin.x, fin.y)
                }

                // Solo se dibuja la parte del trazo que toca según el
                // progreso de la animación (0 → nada, 1 → check entero).
                val medidor = PathMeasure().apply { setPath(trazoCompleto, false) }
                val trazoVisible = Path()
                medidor.getSegment(0f, medidor.length * progresoCheck.value, trazoVisible, true)

                drawPath(
                    path = trazoVisible,
                    color = Ink,
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            "Solicitud enviada",
            style = MaterialTheme.typography.titleLarge,
            color = Ink,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Un administrador tiene que aprobar tu cuenta antes de que puedas entrar.",
            style = MaterialTheme.typography.bodyMedium,
            color = Meta,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onVolverALogin,
            colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Paper),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Volver al inicio de sesión")
        }
    }
}
