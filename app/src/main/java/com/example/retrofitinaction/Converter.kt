package com.example.retrofitinaction

/**
 * Extension function to easily convert a data class instance to a JSON string.
 */
fun Post.toJson(): String {
    // Gson().toJson(this) in a real app.
    return """
        {"id": $id, "userId": $userId, "title": "$title", "body": "$body"}
    """.trimIndent()
}