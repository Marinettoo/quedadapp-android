package com.quedadapp.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quedadapp.app.data.AuthRepository
import com.quedadapp.app.data.PartidosRepository
import com.quedadapp.app.data.Usuario
import com.quedadapp.app.ui.comunes.ViewModelFactory
import com.quedadapp.app.ui.home.HomeScreen
import com.quedadapp.app.ui.home.HomeViewModel
import com.quedadapp.app.ui.login.LoginScreen
import com.quedadapp.app.ui.login.LoginViewModel
import com.quedadapp.app.ui.partidos.PartidosViewModel
import com.quedadapp.app.ui.registro.RegistroEnviadoScreen
import com.quedadapp.app.ui.registro.RegistroScreen
import com.quedadapp.app.ui.registro.RegistroViewModel
import com.quedadapp.app.ui.theme.Ink

// Sin Navigation-Compose todavía: con cuatro pantallas en fila (login,
// registro, registro enviado, inicio) basta un estado simple. Cuando
// lleguen las pantallas de partidos/equipos/pagos con su propia
// navegación en profundidad, este es el sitio donde introducirlo.
private sealed interface PantallaRaiz {
    data object Cargando : PantallaRaiz
    data object Login : PantallaRaiz
    data object Registro : PantallaRaiz
    data object RegistroEnviado : PantallaRaiz
    data class Inicio(val usuario: Usuario) : PantallaRaiz
}

@Composable
fun AppRoot(authRepository: AuthRepository, partidosRepository: PartidosRepository) {
    var pantalla by remember { mutableStateOf<PantallaRaiz>(PantallaRaiz.Cargando) }

    // Al abrir la app: si hay un token guardado y sigue siendo válido,
    // se entra directo a Inicio sin pasar por el login.
    LaunchedEffect(Unit) {
        val usuario = authRepository.sesionActual()
        pantalla = if (usuario != null) PantallaRaiz.Inicio(usuario) else PantallaRaiz.Login
    }

    val factory = remember { ViewModelFactory(authRepository, partidosRepository) }

    // Un deslizamiento + desvanecido entre pantallas en vez de un corte
    // seco: la que entra viene desde la derecha, la que sale se desliza
    // un poco a la izquierda mientras se desvanece.
    AnimatedContent(
        targetState = pantalla,
        transitionSpec = {
            (slideInHorizontally(animationSpec = tween(320)) { ancho -> ancho / 4 } + fadeIn(tween(320)))
                .togetherWith(slideOutHorizontally(animationSpec = tween(220)) { ancho -> -ancho / 8 } + fadeOut(tween(180)))
        },
        label = "pantallaRaiz",
    ) { pantallaActual ->
        when (pantallaActual) {
            PantallaRaiz.Cargando -> CargandoScreen()

            PantallaRaiz.Login -> {
                val viewModel: LoginViewModel = viewModel(factory = factory)
                LoginScreen(
                    viewModel = viewModel,
                    onLoginExitoso = { pantalla = PantallaRaiz.Inicio(it) },
                    onIrARegistro = { pantalla = PantallaRaiz.Registro },
                )
            }

            PantallaRaiz.Registro -> {
                val viewModel: RegistroViewModel = viewModel(factory = factory)
                RegistroScreen(
                    viewModel = viewModel,
                    onRegistroEnviado = { pantalla = PantallaRaiz.RegistroEnviado },
                    onVolverALogin = { pantalla = PantallaRaiz.Login },
                )
            }

            PantallaRaiz.RegistroEnviado -> RegistroEnviadoScreen(onVolverALogin = { pantalla = PantallaRaiz.Login })

            is PantallaRaiz.Inicio -> {
                val viewModel: HomeViewModel = viewModel(factory = factory)
                val partidosViewModel: PartidosViewModel = viewModel(factory = factory)
                HomeScreen(
                    usuario = pantallaActual.usuario,
                    viewModel = viewModel,
                    partidosViewModel = partidosViewModel,
                    onSesionCerrada = { pantalla = PantallaRaiz.Login },
                )
            }
        }
    }
}

@Composable
private fun CargandoScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Ink)
    }
}
