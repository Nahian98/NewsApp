package com.nahian.newsapp.base

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object ViewExtensions {

    fun ImageView.loadImage(uri: String?, placeHolder: Int) {
        val options = RequestOptions().error(placeHolder)

        Glide.with(this.context)
            .setDefaultRequestOptions(options)
            .load(uri)
            .into(this)
    }
}