package com.example.chatapplication.network


import com.example.chatapplication.model.Response
import com.example.chatapplication.model.UserRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

private const val base_url = "https://api.openai.com/v1/images/"


private val retrofitBuilder = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(base_url).build()

interface DataService{
    @POST("generations")
    suspend fun getImage(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorisation: String = "Bearer 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",
        @Body userRequest: UserRequest
    ): Response
}

object ImageApi{
    val retrofitService: DataService by lazy{
        retrofitBuilder.create(DataService::class.java)
    }
}
