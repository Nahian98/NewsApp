package com.nahian.newsapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahian.newsapp.R
import com.nahian.newsapp.databinding.ItemNewsBinding
import com.nahian.newsapp.base.ViewExtensions.loadImage
import com.nahian.newsapp.model.Article

class NewsAdapter(
    val context: Context,
    private val articles: MutableList<Article>
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    var onItemClick: ((Article) -> Unit)? = null

    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        Log.d("_Article", "$article")
        holder.binding.tvNewsTitle.text = article.title
        holder.binding.ivImage.loadImage(article.urlToImage, R.drawable.ic_user_avatar)
        val date = article.publishedAt
        val publishedDate = date.split("T")
        holder.binding.tvPublishTime.text = publishedDate[0]
        holder.binding.tvSourceName.text = article.source.name
        holder.binding.root.tag = position

        holder.binding.root.setOnClickListener {
            onItemClick?.invoke(article)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newArticles: MutableList<Article>){
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }

}