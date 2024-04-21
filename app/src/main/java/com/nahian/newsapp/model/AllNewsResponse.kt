package com.nahian.newsapp.model

data class AllNewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)