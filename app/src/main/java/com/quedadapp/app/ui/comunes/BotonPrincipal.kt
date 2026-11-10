package com.quedadapp.app.ui.comunes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Paper

// Botón primario compartido por login/registro: un poco de "rebote"
// elástico al pulsarlo y el contenido (texto ↔ spinner) cambia con un
// crossfade en vez de sustituirse de golpe. Reutilizado en vez de
// repetir esta lógica en cada pantalla.
@Composable
fun BotonPrincipal(
    texto: String,
    cargando: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pulsado by interactionSource.collectIsPressedAsState()
    val escala by animateFloatAsState(
        targetValue = if (pulsado) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "escalaBotonPrincipal",
    )

    Button(
        onClick = onClick,
        enabled = habilitado && !cargando,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Paper),
        modifier = modifier.scale(escala),
    ) {
        AnimatedContent(targetState = cargando, label = "contenidoBotonPrincipal") { estaCargando ->
            if (estaCargando) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Paper, strokeWidth = 2.dp)
            } else {
                Text(texto)
            }
        }
    }
}
