package com.example.retrofitinaction

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

/**
 * ====================================================================
 * The Custom Mock Interceptor (The Core)
 * ====================================================================
 * This interceptor intercepts network calls and returns hardcoded responses
 * without ever hitting the actual network.
 */
class MockInterceptor : Interceptor {
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath
        val method = request.method

        println("Intercepting request: $method $path")

        // Use a when block to define different mock responses based on path and method
        return when {
            // --- GET Mock ---
            method == "GET" && path.matches("/posts/\\d+".toRegex()) -> {
                val postId = path.substringAfterLast("/").toIntOrNull()
                val mockPost = Post(
                    id = postId,
                    userId = 1,
                    title = "Mocked GET Post $postId",
                    body = "This post content was generated locally by the MockInterceptor."
                )
                buildMockResponse(chain, 200, mockPost.toJson())
            }

            // --- POST Mock (Creating a resource) ---
            method == "POST" && path == "/posts" -> {
                // Return status 201 (Created) and the submitted data with a new ID
                val requestBodyString = request.body?.toString() ?: ""
                val mockResponseJson = """
                    {"id": 101, "userId": 1, "title": "New Post Success", "body": "Mocked POST successful"}
                """.trimIndent()
                buildMockResponse(chain, 201, mockResponseJson)
            }

            // --- PUT Mock (Updating a resource) ---
            method == "PUT" && path.matches("/posts/\\d+".toRegex()) -> {
                // Return the updated data (or a boilerplate success message)
                val mockResponseJson = """
                    {"id": 5, "userId": 1, "title": "Updated Title Mock", "body": "Mocked PUT successful"}
                """.trimIndent()
                buildMockResponse(chain, 200, mockResponseJson)
            }

            // --- DELETE Mock (Deleting a resource) ---
            method == "DELETE" && path.matches("/posts/\\d+".toRegex()) -> {
                // Return status 204 (No Content) as DELETE often has no response body
                buildMockResponse(chain, 204, "")
            }

            // --- Default Fallback ---
            else -> {
                println("No mock rule found. Returning 404.")
                buildMockResponse(chain, 404, """{"error": "Not Found"}""")
            }
        }
    }

    /**
     * Helper function to build a standard OkHttp Response object.
     */
    private fun buildMockResponse(chain: Interceptor.Chain, code: Int, bodyContent: String): Response {
        return Response.Builder()
            .code(code)
            .protocol(Protocol.HTTP_1_1)
            .request(chain.request())
            .message("Mock Response")
            .body(bodyContent.toResponseBody(jsonMediaType))
            .addHeader("content-type", "application/json")
            .build()
    }
}
