package com.quedadapp.app

import android.app.Application
import com.quedadapp.app.data.AuthRepository
import com.quedadapp.app.data.JugadoresRepository
import com.quedadapp.app.data.PagosRepository
import com.quedadapp.app.data.PartidosRepository
import com.quedadapp.app.data.TokenStore

// Sin Hilt/Dagger por ahora: para esta primera versión basta con un
// contenedor manual muy simple. Si el número de dependencias crece
// mucho más con las siguientes pantallas, se puede introducir
// inyección de dependencias de verdad más adelante.
class QuedadappApp : Application() {
    lateinit var authRepository: AuthRepository
        private set
    lateinit var partidosRepository: PartidosRepository
        private set
    lateinit var pagosRepository: PagosRepository
        private set
    lateinit var jugadoresRepository: JugadoresRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val tokenStore = TokenStore(this)
        authRepository = AuthRepository(tokenStore)
        partidosRepository = PartidosRepository(tokenStore)
        pagosRepository = PagosRepository(tokenStore)
        jugadoresRepository = JugadoresRepository(tokenStore)
    }
}
