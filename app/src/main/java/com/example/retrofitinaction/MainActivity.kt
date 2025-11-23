package com.example.retrofitinaction
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.retrofitinaction.ui.theme.RetrofitInActionTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitInActionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    main()
                }
            }
        }
    }
}

/**
 * ====================================================================
 * Simulated Application (Main/Testing)
 * ====================================================================
 * Demonstrates how a ViewModel or main function would use the Repository.
 */
fun main() = runBlocking {
    val repository: IPostRepository = PostRepository(RetrofitClient.apiService)

    println("--- Executing Mock API Calls via Repository ---")

    // --- Test 1: GET Post (Success) ---
    val postId = 42
    println("\n[1] Fetching Post $postId...")
    when (val result = repository.fetchPost(postId)) {
        is Result.Success -> println("SUCCESS: Fetched Post Title: ${result.data.title}")
        is Result.Error -> println("ERROR: ${result.message}")
        Result.Loading -> {}
    }

    // --- Test 2: POST Post (Success, 201 Created) ---
    val newPost = Post(userId = 1, title = "Mocked Draft", body = "Repository Test Content")
    println("\n[2] Creating New Post...")
    when (val result = repository.createNewPost(newPost)) {
        is Result.Success -> println("SUCCESS: Created Post with Mock ID: ${result.data.id} (Status 201)")
        is Result.Error -> println("ERROR: ${result.message}")
        Result.Loading -> {}
    }

    // --- Test 3: DELETE Post (Success, 204 No Content) ---
    val deleteId = 99
    println("\n[3] Deleting Post $deleteId...")
    when (val result = repository.deletePost(deleteId)) {
        is Result.Success -> println("SUCCESS: Post $deleteId deleted. (Status 204)")
        is Result.Error -> println("ERROR: ${result.message}")
        Result.Loading -> {}
    }

    // --- Test 4: Simulate a FAILED call (e.g., 404 handled by MockInterceptor fallback) ---
    // If we call a path not explicitly mocked, the MockInterceptor will return a 404
    // (This requires adding a mock for an unhandled path, but using a non-standard call
    // in the real API will also cause an internal error which safeApiCall handles).
    // Let's assume a hypothetical `fetchNonExistentPost` was added to the service
    // that the mock returns a 404 for. For now, rely on the safeApiCall to demonstrate error handling.
    // If the mock was configured to return a 404 for post 0:
    // val failedResponse = apiService.getPost(0) // Assuming mock interceptor returns 404 for ID 0
    // println("\n[4] Fetching Post 0 (Simulating 404)...")
    // ...

    println("\n--- Repository execution complete ---")
}
