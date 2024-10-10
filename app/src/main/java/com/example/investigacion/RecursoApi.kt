package com.example.investigacion

import retrofit2.Call
import retrofit2.http.*

interface RecursoApi {
    @GET("recursos")
    fun getRecursos(): Call<List<Recurso>>

    @GET("recursos/{id}")
    fun getRecurso(@Path("id") id: Int): Call<Recurso>

    @POST("recursos")
    fun createRecurso(@Body recurso: Recurso): Call<Recurso>

    @PUT("recursos/{id}")
    fun updateRecurso(@Path("id") id: Int, @Body recurso: Recurso): Call<Recurso>

    @DELETE("recursos/{id}")
    fun deleteRecurso(@Path("id") id: Int): Call<Void>
}

//por el momento esta cosa no sirve de mucho pero aca se tiene idea de las peticiones que deben hacerse