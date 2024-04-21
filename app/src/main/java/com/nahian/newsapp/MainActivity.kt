package com.nahian.newsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahian.newsapp.adapter.NewsAdapter
import com.nahian.newsapp.databinding.ActivityMainBinding
import com.nahian.newsapp.viewmodel.MainActivityViewModel
import com.nahian.newsapp.model.Article

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val articleArrayList = ArrayList<Article>()
    private var newsAdapter: NewsAdapter? = null
    private lateinit var newsViewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        newsViewModel = MainActivityViewModel()
        loadDataFromNews()
        initComponent()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        // Update the data when the activity resumes
        loadDataFromNews()
    }

    private fun initComponent() {
        newsAdapter = NewsAdapter(this@MainActivity, arrayListOf())
        binding.rvTopHeadlines.adapter = newsAdapter
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvTopHeadlines.layoutManager = linearLayoutManager
    }

    private fun initListener() {

        newsAdapter?.onItemClick = {
            startActivity(Intent(this@MainActivity, WebViewActivity::class.java).apply {
                putExtra("url", it.url)
            })
        }

        binding.srlNews.setOnRefreshListener {
            loadDataFromNews()
        }

        binding.svNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // When the user submits the query, pass it to the ViewModel
                Log.d("_Search", query)
                searchNews(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle query text change if needed
                return false
            }
        })


    }

    private fun searchNews(query: String) {
        binding.srlNews.isRefreshing = true
        newsViewModel.search(query)
        newsViewModel.getNewsRepositorySearch().observe(this@MainActivity, Observer{ newsResponse ->
            if (newsResponse != null){
                val newArticles = newsResponse.articles
                Log.d("_Search1", query)
                articleArrayList.clear()
                articleArrayList.addAll(newArticles)
                newsAdapter?.setData(articleArrayList)
                binding.srlNews.isRefreshing = false
            } else {
                loadDataFromNews()
                Log.d("_Search2", query)
            }

        })
    }


    private fun loadDataFromNews() {
        binding.srlNews.isRefreshing = true
        newsViewModel.init()
        newsViewModel.getNewsRepository()?.observe(this@MainActivity, Observer { newsResponse ->
            if(newsResponse != null){
                val newArticles = newsResponse.articles
                articleArrayList.clear()
                articleArrayList.addAll(newArticles)
                newsAdapter?.setData(articleArrayList)
                binding.srlNews.isRefreshing = false
            }
        })
    }

    private fun onLoadingSwipeRefresh() {
        binding.srlNews.post { loadDataFromNews() }
    }

}