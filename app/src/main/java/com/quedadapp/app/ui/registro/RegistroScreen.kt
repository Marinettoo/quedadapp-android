package com.quedadapp.app.ui.registro

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.quedadapp.app.ui.comunes.BotonPrincipal
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Meta

@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onRegistroEnviado: () -> Unit,
    onVolverALogin: () -> Unit,
) {
    // Igual que en Login: se guarda el último mensaje para que no
    // desaparezca de golpe a mitad de la animación de salida.
    var ultimoError by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(viewModel.error) {
        if (viewModel.error != null) ultimoError = viewModel.error
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.titleLarge, color = Ink)
        Spacer(Modifier.height(4.dp))
        Text(
            "Tu cuenta quedará pendiente hasta que un administrador la apruebe.",
            style = MaterialTheme.typography.bodyMedium,
            color = Meta,
        )
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
            value = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Correo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = viewModel.password2,
            onValueChange = viewModel::onPassword2Change,
            label = { Text("Repite la contraseña") },
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
            texto = "Crear cuenta",
            cargando = viewModel.cargando,
            onClick = { viewModel.registrar(onRegistroEnviado) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onVolverALogin, modifier = Modifier.fillMaxWidth()) {
            Text("Ya tengo cuenta, iniciar sesión", color = Meta)
        }
    }
}
