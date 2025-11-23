package com.example.retrofitinaction

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * ====================================================================
 * Retrofit Service Interface
 * ====================================================================
 * This interface defines all the methods we want to mock and test.
 * The implementation never runs against a real server.
 */
interface ApiService {
    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): retrofit2.Response<Post>

    @POST("posts")
    suspend fun createPost(@Body post: Post): retrofit2.Response<Post>

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: Post): retrofit2.Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): retrofit2.Response<Unit>
}