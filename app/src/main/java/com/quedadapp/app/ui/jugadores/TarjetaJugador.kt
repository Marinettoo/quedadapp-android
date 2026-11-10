package com.quedadapp.app.ui.jugadores

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.quedadapp.app.BuildConfig
import com.quedadapp.app.data.Jugador
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Lime
import kotlinx.coroutines.delay
import okhttp3.Headers
import kotlin.math.roundToInt

private val COLOR_BRONCE = Color(0xFFCD8B55)
private val COLOR_PLATA = Color(0xFFC7CCD1)
private val COLOR_ORO = Color(0xFFF5C64B)
private val COLOR_MVP_FONDO = Color(0xFF0A0A0A)
private val COLOR_MVP_TEXTO = Color(0xFFF2C94C)

private val ETIQUETAS_PIERNA = mapOf("DIESTRO" to "Diestro", "ZURDO" to "Zurdo", "AMBIDIESTRO" to "Ambidiestro")
private const val RETRASO_ENTRE_CHIPS = 110

private fun nivelDe(media: Double?): String? = when {
    media == null -> null
    media >= 4.5 -> "oro"
    media >= 2.9 -> "plata"
    else -> "bronce"
}

// Misma ficha tipo cromo deportivo que TarjetaJugador.tsx en la web:
// fondo según nivel (bronce/plata/oro) o negro+dorado si es el MVP
// actual, nombre en cursiva grande, chips de estadísticas que cuentan
// al aparecer, foto recortada asomando por la esquina, y una
// decoración de fondo sutil (círculo punteado, línea, punto que late)
// que gira muy despacio.
@Composable
fun TarjetaJugador(jugador: Jugador, token: String?, modifier: Modifier = Modifier) {
    val nivel = nivelDe(jugador.media)
    val fondo = when {
        jugador.esMvp -> COLOR_MVP_FONDO
        nivel == "oro" -> COLOR_ORO
        nivel == "plata" -> COLOR_PLATA
        nivel == "bronce" -> COLOR_BRONCE
        else -> Lime
    }
    val colorTexto = if (jugador.esMvp) COLOR_MVP_TEXTO else Ink
    val colorDecor = if (jugador.esMvp) Color.White else Color.Black
    val chipFondo = if (jugador.esMvp) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.85f)
    val chipTexto = if (jugador.esMvp) COLOR_MVP_TEXTO else Color.White
    val tieneBrillo = jugador.esMvp || nivel != null

    Box(
        modifier = modifier
            .width(320.dp)
            .height(410.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(fondo),
    ) {
        DecoracionFondo(color = colorDecor, modifier = Modifier.fillMaxSize())
        if (tieneBrillo) {
            BrilloDiagonal(modifier = Modifier.fillMaxSize())
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .border(1.5.dp, colorDecor.copy(alpha = 0.5f), RoundedCornerShape(26.dp)),
        )

        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                jugador.nombreUsuario,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 32.sp,
                lineHeight = 32.sp,
                letterSpacing = (-0.8).sp,
                color = colorTexto,
            )
            Spacer(Modifier.height(16.dp))

            var indiceChip = 0
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ChipEstadistica(
                    valor = jugador.media?.let { contadorAnimado(it.toFloat(), 1, (indiceChip++) * RETRASO_ENTRE_CHIPS) } ?: "—",
                    etiqueta = if (jugador.cuenta == 0) "Sin valoraciones" else "★ (${jugador.cuenta} ${if (jugador.cuenta == 1) "voto" else "votos"})",
                    fondo = chipFondo,
                    texto = chipTexto,
                )
                ChipEstadistica(
                    valor = contadorAnimado(jugador.partidosJugados.toFloat(), 0, (indiceChip++) * RETRASO_ENTRE_CHIPS),
                    etiqueta = "Partidos jugados",
                    fondo = chipFondo,
                    texto = chipTexto,
                )
                ChipEstadistica(
                    valor = contadorAnimado(jugador.golesMarcados.toFloat(), 0, (indiceChip++) * RETRASO_ENTRE_CHIPS),
                    etiqueta = "Goles marcados",
                    fondo = chipFondo,
                    texto = chipTexto,
                )
                jugador.edad?.let { edad ->
                    ChipEstadistica(
                        valor = contadorAnimado(edad.toFloat(), 0, (indiceChip++) * RETRASO_ENTRE_CHIPS),
                        etiqueta = "años",
                        fondo = chipFondo,
                        texto = chipTexto,
                    )
                }
                jugador.piernaBuena?.let { pierna ->
                    ChipEstadistica(
                        valor = ETIQUETAS_PIERNA[pierna] ?: pierna,
                        etiqueta = "Pierna buena",
                        fondo = chipFondo,
                        texto = chipTexto,
                    )
                }
            }
        }

        FotoJugador(jugador = jugador, token = token, modifier = Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
private fun ChipEstadistica(valor: String, etiqueta: String, fondo: Color, texto: Color) {
    Column(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(valor, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = texto)
        Text(etiqueta, fontSize = 11.sp, color = texto.copy(alpha = 0.72f))
    }
}

// Cuenta rápido desde 0 hasta su valor real al aparecer en pantalla,
// con un pequeño retraso entre chips (igual que ContadorAnimado.tsx).
@Composable
private fun contadorAnimado(valor: Float, decimales: Int, retrasoMs: Int): String {
    val animado = remember { Animatable(0f) }
    LaunchedEffect(valor) {
        delay(retrasoMs.toLong())
        animado.animateTo(valor, animationSpec = tween(900, easing = FastOutSlowInEasing))
    }
    return if (decimales == 0) animado.value.roundToInt().toString() else "%.${decimales}f".format(animado.value)
}

@Composable
private fun DecoracionFondo(color: Color, modifier: Modifier = Modifier) {
    val infinita = rememberInfiniteTransition(label = "decorInfinita")
    val anguloCirculo by infinita.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(50_000, easing = LinearEasing)),
        label = "anguloCirculo",
    )
    val anguloEsquina by infinita.animateFloat(
        initialValue = 0f,
        targetValue = -360f,
        animationSpec = infiniteRepeatable(tween(35_000, easing = LinearEasing)),
        label = "anguloEsquina",
    )
    val pulso by infinita.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "pulso",
    )

    Canvas(modifier = modifier) {
        val centroCirculo = Offset(size.width * 0.58f, -70.dp.toPx())
        rotate(anguloCirculo, pivot = centroCirculo) {
            drawCircle(
                color = color.copy(alpha = 0.1f),
                radius = 130.dp.toPx(),
                center = centroCirculo,
                style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 7f))),
            )
        }

        val yLinea = 60.dp.toPx()
        drawLine(
            color = color.copy(alpha = 0.1f),
            start = Offset(0f, yLinea),
            end = Offset(size.width, yLinea),
            strokeWidth = 2.dp.toPx(),
        )
        drawCircle(
            color = color.copy(alpha = 0.16f),
            radius = 3.dp.toPx() * pulso,
            center = Offset(size.width * 0.58f, yLinea),
        )

        val centroEsquina = Offset(-30.dp.toPx(), size.height + 30.dp.toPx())
        rotate(anguloEsquina, pivot = centroEsquina) {
            drawCircle(
                color = color.copy(alpha = 0.1f),
                radius = 45.dp.toPx(),
                center = centroEsquina,
                style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 7f))),
            )
        }
    }
}

// Barrido de luz en diagonal, como el reflejo de un cromo metálico:
// una barra traslúcida girada que atraviesa la ficha una y otra vez.
@Composable
private fun BrilloDiagonal(modifier: Modifier = Modifier) {
    val infinita = rememberInfiniteTransition(label = "brilloInfinita")
    val progreso by infinita.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3800
                0f at 0
                0f at 570
                1f at 2090
                1f at 3800
            },
        ),
        label = "progresoBrillo",
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .graphicsLayer { alpha = 0.99f },
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 700.dp, height = 90.dp)
                .graphicsLayer {
                    rotationZ = -25f
                    translationX = (progreso - 0.5f) * 1100.dp.toPx()
                }
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, Color.White.copy(alpha = 0.5f), Color.Transparent),
                    ),
                ),
        )
    }
}

@Composable
private fun FotoJugador(jugador: Jugador, token: String?, modifier: Modifier = Modifier) {
    if (jugador.tieneFoto && token != null) {
        val contexto = LocalContext.current
        val url = "${BuildConfig.API_BASE_URL}fotos/${jugador.id}?v=${jugador.fotoActualizadaEn}"
        val peticion = remember(url, token) {
            ImageRequest.Builder(contexto)
                .data(url)
                .headers(Headers.Builder().add("Authorization", "Bearer $token").build())
                .crossfade(true)
                .build()
        }
        AsyncImage(
            model = peticion,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier
                .height(210.dp)
                .graphicsLayer {
                    translationX = (jugador.fotoOffsetX / 100.0).toFloat() * size.width
                    translationY = (jugador.fotoOffsetY / 100.0).toFloat() * size.height
                    scaleX = jugador.fotoEscala.toFloat()
                    scaleY = jugador.fotoEscala.toFloat()
                    transformOrigin = TransformOrigin(1f, 1f)
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                // Máscara: la base de la foto se funde en el fondo de la
                // ficha en vez de cortarse en seco (igual que el
                // mask-image de la web).
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to Color.Black,
                            0.86f to Color.Black,
                            1f to Color.Transparent,
                        ),
                        blendMode = BlendMode.DstIn,
                    )
                },
        )
    } else {
        Box(
            modifier = modifier
                .padding(end = 24.dp, bottom = 20.dp)
                .size(90.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                jugador.nombreUsuario.take(1).uppercase(),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.5f),
            )
        }
    }
}
