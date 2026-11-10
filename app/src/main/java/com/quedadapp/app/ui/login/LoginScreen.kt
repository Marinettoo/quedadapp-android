package com.quedadapp.app.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.quedadapp.app.data.Usuario
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Lime
import com.quedadapp.app.ui.theme.Meta
import com.quedadapp.app.ui.theme.Paper

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginExitoso: (Usuario) -> Unit,
    onIrARegistro: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Bloque de marca (lima), como el "login cinematográfico" de la
        // web: lo expresivo va arriba, el formulario abajo es lo
        // funcional. Versión simplificada por ahora, sin la ilustración.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .background(Lime),
            contentAlignment = Alignment.BottomStart,
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
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

            viewModel.error?.let { mensaje ->
                Spacer(Modifier.height(12.dp))
                Text(mensaje, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { viewModel.iniciarSesion(onLoginExitoso) },
                enabled = !viewModel.cargando,
                colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Paper),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (viewModel.cargando) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Paper)
                } else {
                    Text("Entrar")
                }
            }

            Spacer(Modifier.height(12.dp))
            TextButton(onClick = onIrARegistro, modifier = Modifier.fillMaxWidth()) {
                Text("¿No tienes cuenta? Regístrate", color = Meta)
            }
        }
    }
}
