package com.quedadapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.quedadapp.app.ui.AppRoot
import com.quedadapp.app.ui.theme.Paper
import com.quedadapp.app.ui.theme.QuedadappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as QuedadappApp

        setContent {
            QuedadappTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Paper) {
                    AppRoot(app.authRepository, app.partidosRepository)
                }
            }
        }
    }
}
