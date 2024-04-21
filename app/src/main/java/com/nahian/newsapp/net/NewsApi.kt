package com.nahian.newsapp.net

import com.nahian.newsapp.model.AllNewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NewsApi {

    @GET("everything")
    fun getEverythingNews(
        @QueryMap qMap: MutableMap<String, String>
    ) : Call<AllNewsResponse>

    @GET("top-headlines")
    fun getTopHeadlines(
        @QueryMap qMap: MutableMap<String, String>
    ) : Call<AllNewsResponse>
}