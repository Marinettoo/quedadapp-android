package com.quedadapp.app.data

import com.quedadapp.app.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Dns
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import java.net.InetAddress

// Solo en depuración: en vez de salir a internet para resolver el
// dominio, lo resuelve directamente a la IP del servidor en la red
// local (BuildConfig.DEBUG_HOST_IP). El dominio en sí no cambia (sigue
// siendo furbo.marinettoo.es en la URL, el SNI y el certificado), así
// que HTTPS sigue validando bien — solo cambia a qué IP se conecta.
private object DnsRedLocal : Dns {
    override fun lookup(hostname: String): List<InetAddress> =
        if (hostname == "furbo.marinettoo.es" && BuildConfig.DEBUG_HOST_IP.isNotBlank()) {
            listOf(InetAddress.getByName(BuildConfig.DEBUG_HOST_IP))
        } else {
            Dns.SYSTEM.lookup(hostname)
        }
}

object ApiClient {
    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient = OkHttpClient.Builder()
        .dns(DnsRedLocal)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            },
        )
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)

    // Las rutas de error de la API devuelven {"error": "..."} con un
    // código distinto de 2xx; Retrofit no las convierte solas, hay que
    // leer el cuerpo de error a mano con este mismo Json.
    fun <T> mensajeDeError(respuesta: Response<T>): String {
        val cuerpo = respuesta.errorBody()?.string()
        val mensaje = cuerpo?.let {
            runCatching { json.decodeFromString<ErrorResponse>(it).error }.getOrNull()
        }
        return mensaje ?: "No se pudo completar la petición (${respuesta.code()})."
    }
}
