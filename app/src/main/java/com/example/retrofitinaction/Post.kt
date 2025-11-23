package com.example.retrofitinaction

/**
 * Data Model for a Post
 */
data class Post(
    val id: Int? = null,
    val userId: Int,
    val title: String,
    val body: String
)