package com.example.retrofitinaction

import retrofit2.Response
import java.io.IOException

/**
 * Custom sealed class to encapsulate success, error, and loading states.
 * This is crucial for separating the Repository logic from the UI/ViewModel.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception, val message: String? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * ====================================================================
 * Repository Interface
 * ====================================================================
 * Defines the contract for all data operations.
 */
interface IPostRepository {
    suspend fun fetchPost(id: Int): Result<Post>
    suspend fun createNewPost(post: Post): Result<Post>
    suspend fun deletePost(id: Int): Result<Unit> // Unit signifies no body needed for successful deletion
}

/**
 * ====================================================================
 * Repository Implementation
 * ====================================================================
 * Contains the logic for fetching data and handling API responses.
 */
class PostRepository(private val apiService: ApiService) : IPostRepository {

    /**
     * Generic safe API call wrapper.
     * Handles exceptions (like IOExceptions from the MockInterceptor) and non-2xx status codes.
     */
    private suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                // If the body is null (like for a 204 No Content), we safely cast Unit or throw
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else if (response.code() == 204) {
                    // Handle 204 No Content, which Retrofit handles as a successful call with a null body
                    @Suppress("UNCHECKED_CAST")
                    Result.Success(Unit as T)
                } else {
                    // This handles successful calls where we expected a body but got none
                    Result.Error(Exception("Empty body received for success response"), "No content")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Result.Error(IOException("HTTP Error ${response.code()}"), errorMessage)
            }
        } catch (e: Exception) {
            Result.Error(e, "Network or unexpected error: ${e.localizedMessage}")
        }
    }

    override suspend fun fetchPost(id: Int): Result<Post> {
        return safeApiCall { apiService.getPost(id) }
    }

    override suspend fun createNewPost(post: Post): Result<Post> {
        // Our MockInterceptor returns 201 (Created) for POST
        return safeApiCall { apiService.createPost(post) }
    }

    override suspend fun deletePost(id: Int): Result<Unit> {
        // Our MockInterceptor returns 204 (No Content) for DELETE
        return safeApiCall { apiService.deletePost(id) }
    }
}