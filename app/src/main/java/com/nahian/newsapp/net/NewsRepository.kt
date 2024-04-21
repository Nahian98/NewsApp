package com.nahian.newsapp.net

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.nahian.newsapp.model.AllNewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NewsRepository {
    private lateinit var newRepository: NewsRepository
    private val retrofit = RetrofitService.getInstance()
    private val newsApi: NewsApi = retrofit.create(NewsApi::class.java)

    fun getInstance(): NewsRepository {
        newRepository = NewsRepository
        return newRepository
    }

    fun getNewsBySearch(keyword: String, apiKey: String): MutableLiveData<AllNewsResponse>{
        val newsDataSearch = MutableLiveData<AllNewsResponse>()

        val qMap = mutableMapOf(
            "q" to keyword,
            "apiKey" to apiKey
        )
        newsApi.getEverythingNews(qMap).enqueue(object : Callback<AllNewsResponse>{
            override fun onResponse(
                call: Call<AllNewsResponse>,
                response: Response<AllNewsResponse>
            ) {

                newsDataSearch.value = response.body()

                Log.d("_SearchedNews", "${GsonBuilder().setPrettyPrinting().create().toJson(newsDataSearch.value)}")

            }

            override fun onFailure(call: Call<AllNewsResponse>, t: Throwable) {
                newsDataSearch.value = null
            }

        })
        return newsDataSearch
    }

    fun getNews(country: String, apiKey: String): MutableLiveData<AllNewsResponse>{
        val newsData = MutableLiveData<AllNewsResponse>()

        val qMap = mutableMapOf(
            "country" to country,
            "apiKey" to apiKey
        )
        newsApi.getTopHeadlines(qMap).enqueue(object : Callback<AllNewsResponse>{
            override fun onResponse(
                call: Call<AllNewsResponse>,
                response: Response<AllNewsResponse>
            ) {

                newsData.value = response.body()

                Log.d("_TopHeadlines", "${GsonBuilder().setPrettyPrinting().create().toJson(newsData.value)}")

            }

            override fun onFailure(call: Call<AllNewsResponse>, t: Throwable) {
                newsData.value = null
            }

        })
        return newsData
    }



}