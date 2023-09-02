package com.hatepoint.natife.data

import com.hatepoint.natife.retrofit.GiphyApiService

class GiphyRepository(private val apiService: GiphyApiService) {
    val apiKey = "CHkeIglUvnyRgSjhRIy3uHkQITWX82tx"

    suspend fun getTrendingGifs(limit: Int, rating: String) =
        apiService.getTrendingGifs(apiKey, limit, rating)
}