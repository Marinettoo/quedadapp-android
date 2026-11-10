package com.quedadapp.app.data

import com.quedadapp.app.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit

object ApiClient {
    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient = OkHttpClient.Builder()
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
