package com.quedadapp.app.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.quedadapp.app.data.Usuario
import com.quedadapp.app.ui.comunes.BalonGirando
import com.quedadapp.app.ui.comunes.BotonPrincipal
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Lime
import com.quedadapp.app.ui.theme.Meta

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginExitoso: (Usuario) -> Unit,
    onIrARegistro: () -> Unit,
) {
    // El titular del bloque lima entra con un pequeño deslizamiento +
    // desvanecido al abrir la pantalla, en vez de aparecer de golpe —
    // el equivalente animado al "login cinematográfico" de la web.
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val alphaTitular by animateFloatAsState(if (visible) 1f else 0f, tween(550), label = "alphaTitularLogin")
    val desplazamientoTitular by animateDpAsState(if (visible) 0.dp else 20.dp, tween(550), label = "offsetTitularLogin")

    // El mensaje de error se queda visible durante la animación de
    // salida aunque el ViewModel ya lo haya puesto a null (si no, el
    // texto desaparecería de golpe a mitad de la animación).
    var ultimoError by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(viewModel.error) {
        if (viewModel.error != null) ultimoError = viewModel.error
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .background(Lime),
            contentAlignment = Alignment.BottomStart,
        ) {
            BalonGirando(
                color = Ink,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 40.dp, y = 40.dp)
                    .size(180.dp),
            )

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .offset(y = desplazamientoTitular)
                    .alpha(alphaTitular),
            ) {
                Text("Quedadapp", style = MaterialTheme.typography.labelLarge, color = Ink)
                Spacer(Modifier.height(8.dp))
                Text(
                    "La app de los sábado fúrbo",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Ink,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Entrar", style = MaterialTheme.typography.titleLarge, color = Ink)
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = viewModel.nombreUsuario,
                onValueChange = viewModel::onNombreUsuarioChange,
                label = { Text("Nombre de usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
            )

            AnimatedVisibility(
                visible = viewModel.error != null,
                enter = fadeIn(tween(200)) + expandVertically(tween(200)),
                exit = fadeOut(tween(150)) + shrinkVertically(tween(150)),
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        ultimoError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            BotonPrincipal(
                texto = "Entrar",
                cargando = viewModel.cargando,
                onClick = { viewModel.iniciarSesion(onLoginExitoso) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(12.dp))
            TextButton(onClick = onIrARegistro, modifier = Modifier.fillMaxWidth()) {
                Text("¿No tienes cuenta? Regístrate", color = Meta)
            }
        }
    }
}
