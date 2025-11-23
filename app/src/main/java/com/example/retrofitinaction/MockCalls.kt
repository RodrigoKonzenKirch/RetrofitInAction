package com.example.retrofitinaction

/**
 * ====================================================================
 * Example Execution (Simulated)
 * ====================================================================
 * In actual Android code (e.g., a ViewModel or a CoroutineScope):
 * NOTE: This is just a conceptual function for demonstration.
 */
suspend fun executeMockCalls() {
    val api = RetrofitClient.apiService

    // 1. GET Request
    val getResponse = api.getPost(1)
    println("GET Status: ${getResponse.code()}")
    println("GET Body: ${getResponse.body()?.title}")

    // 2. POST Request
    val newPost = Post(userId = 1, title = "My New Post", body = "Content to send")
    val postResponse = api.createPost(newPost)
    println("POST Status: ${postResponse.code()}")
    println("POST Body ID: ${postResponse.body()?.id}") // Should be 101

    // 3. PUT Request
    val updatedPost = Post(id = 5, userId = 1, title = "Revised Title", body = "Updated content")
    val putResponse = api.updatePost(5, updatedPost)
    println("PUT Status: ${putResponse.code()}")
    println("PUT Body Title: ${putResponse.body()?.title}") // Should be "Updated Title Mock"

    // 4. DELETE Request
    val deleteResponse = api.deletePost(10)
    println("DELETE Status: ${deleteResponse.code()}")
    println("DELETE Success: ${deleteResponse.isSuccessful}") // Should be true (204 No Content)
}