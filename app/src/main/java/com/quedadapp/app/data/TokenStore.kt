package com.quedadapp.app.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

// El token de sesión es, a efectos prácticos, una contraseña: se guarda
// cifrado en disco (EncryptedSharedPreferences, no SharedPreferences a
// secas), nunca en texto plano ni en logs fuera del modo debug.
class TokenStore(context: Context) {
    private val prefs: SharedPreferences = run {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "sesion_segura",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    fun guardarToken(token: String) {
        prefs.edit().putString(CLAVE_TOKEN, token).apply()
    }

    fun leerToken(): String? = prefs.getString(CLAVE_TOKEN, null)

    fun borrarToken() {
        prefs.edit().remove(CLAVE_TOKEN).apply()
    }

    companion object {
        private const val CLAVE_TOKEN = "token"
    }
}
