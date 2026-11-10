package com.quedadapp.app

import android.app.Application
import com.quedadapp.app.data.AuthRepository
import com.quedadapp.app.data.TokenStore

// Sin Hilt/Dagger por ahora: para esta primera versión (login +
// registro + sesión) basta con un contenedor manual muy simple. Si el
// número de dependencias crece con las siguientes pantallas, se puede
// introducir inyección de dependencias de verdad más adelante.
class QuedadappApp : Application() {
    lateinit var authRepository: AuthRepository
        private set

    override fun onCreate() {
        super.onCreate()
        authRepository = AuthRepository(TokenStore(this))
    }
}
