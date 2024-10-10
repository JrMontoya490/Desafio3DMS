package com.example.investigacion

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RecursoApiService {
    @GET("recursos")
    suspend fun getRecursos(): List<RecursoModel>

    @POST("recursos")
    suspend fun createRecurso(@Body recurso: RecursoModel): RecursoModel

    @PUT("recursos/{id}")
    suspend fun updateRecurso(@Path("id") id: Long, @Body recurso: RecursoModel)

    @DELETE("recursos/{id}")
    suspend fun deleteRecurso(@Path("id") id: Long)
}
