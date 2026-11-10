package com.quedadapp.app.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("api/v1/auth/registro")
    suspend fun registro(@Body body: RegistroRequest): Response<Unit>

    @GET("api/v1/me")
    suspend fun me(@Header("Authorization") autorizacion: String): Response<MeResponse>

    @POST("api/v1/auth/logout")
    suspend fun logout(@Header("Authorization") autorizacion: String): Response<Unit>
}
