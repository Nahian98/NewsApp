package com.nahian.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nahian.newsapp.model.AllNewsResponse
import com.nahian.newsapp.net.NewsRepository

class MainActivityViewModel {
    private lateinit var newsRepository: NewsRepository
    private lateinit var mutableLiveData: MutableLiveData<AllNewsResponse>
    private lateinit var mutableLiveDataSearch: MutableLiveData<AllNewsResponse>

    private val apiKey = "cbc079cad59c41a89882ce7e31a2b277"

    fun init() {
//        if (mutableLiveData != null){
//            return
//        }

        newsRepository = NewsRepository.getInstance()
        mutableLiveData = newsRepository.getNews("us", apiKey)
    }

    fun search(keyword: String){
//        if (mutableLiveData != null){
//            return
//        }
        newsRepository = NewsRepository.getInstance()
        mutableLiveDataSearch = newsRepository.getNewsBySearch(keyword, apiKey)

    }

    fun getNewsRepository() : LiveData<AllNewsResponse>? {
        return mutableLiveData
    }

    fun getNewsRepositorySearch(): MutableLiveData<AllNewsResponse> {
        return mutableLiveDataSearch
    }
}