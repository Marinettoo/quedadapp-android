package com.quedadapp.app.ui.registro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Meta
import com.quedadapp.app.ui.theme.Paper

@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onRegistroEnviado: () -> Unit,
    onVolverALogin: () -> Unit,
) {
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

        viewModel.error?.let { mensaje ->
            Spacer(Modifier.height(12.dp))
            Text(mensaje, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { viewModel.registrar(onRegistroEnviado) },
            enabled = !viewModel.cargando,
            colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Paper),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (viewModel.cargando) "Enviando…" else "Crear cuenta")
        }

        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onVolverALogin, modifier = Modifier.fillMaxWidth()) {
            Text("Ya tengo cuenta, iniciar sesión", color = Meta)
        }
    }
}
