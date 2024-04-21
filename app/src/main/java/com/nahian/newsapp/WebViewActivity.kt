package com.nahian.newsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nahian.newsapp.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private var url: String? = null
    private var title = "Title"
    private val TAG = WebViewActivity::class.java.name
    private var cookieManager: CookieManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentExtras()
        initComponent()
        initListener()
    }

    private fun getIntentExtras() {
        url = intent.getStringExtra("url")
    }

    private fun initComponent() {
        binding.toolbar.tvTitle.text = "WebView"
        setUpWebView()
    }

    private fun initListener() {
        binding.toolbar.ivToolbarIcon.setOnClickListener {
            onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled, JavascriptInterface")
    private fun setUpWebView() {
        binding.webView.setLayerType(
            View.LAYER_TYPE_HARDWARE, null
        )

        // Set the necessary settings value
        val settings = binding.webView.settings
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.setSupportMultipleWindows(false)
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false
        settings.defaultTextEncodingName = "utf-8"
        settings.loadsImagesAutomatically = true
        cookieManager = CookieManager.getInstance()
        cookieManager?.setAcceptCookie(true)
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        cookieManager?.setAcceptThirdPartyCookies(binding.webView, true)

        // Set the WebView client
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedHttpError(
                view: WebView, request: WebResourceRequest, errorResponse: WebResourceResponse
            ) {
                super.onReceivedHttpError(view, request, errorResponse)

                // Log the error

            }

            override fun onReceivedSslError(
                view: WebView, handler: SslErrorHandler, error: SslError
            ) {
                var message = "SSL Certificate error."
                when (error.primaryError) {
                    SslError.SSL_UNTRUSTED -> message = "The certificate authority is not trusted."
                    SslError.SSL_EXPIRED -> message = "The certificate has expired."
                    SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
                    SslError.SSL_NOTYETVALID -> message = "The certificate is not yet valid."
                }
                message += " Do you want to continue anyway?"
                Log.e(TAG, "onReceivedSslError() $message")
            }

            override fun onReceivedError(
                view: WebView, errorCode: Int, description: String, failingUrl: String
            ) {

                // Log the error
                Log.e(TAG, "onReceivedError() $errorCode")
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // If the url field contains a web url, show the progress.
                // Else, if the url field other thing than web url (for example, a resource path),
                // hide the url
                Log.v(TAG, "onPageStarted() start url = $url")
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE


                // If url match has not been found yet, check if matches, and do work accordingly
                //handleUrl(url, view)

            }

            override fun onPageFinished(view: WebView, url: String) {
                // Hide the progress
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE
            }
        }

        // Set the WebChrome client
        binding.webView.webChromeClient = object : WebChromeClient() {
            // This method is fired in case of opening new window
            override fun onCreateWindow(
                view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                val result = view.hitTestResult
                val data = result.extra
//                Logger.d(TAG , " [onCreateWindow()] DATA: " + data)
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = view
                resultMsg.sendToTarget()
                Log.d(
                    TAG,
                    "onCreateWindow()" + "NEW WINDOW CREATED. URL = " + view.url + ", ORIGINAL URL = " + view.originalUrl
                )

                return true
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.d(
                    TAG,
                    "onProgressChanged()" + "page loading progress updated. progress = " + newProgress + ", url = "
                )
            }
        }


        url?.let {
            binding.webView.loadUrl(
                it
            )
        }
        Log.v(
            TAG, "url: $url"
        )
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            binding.webView.clearCache(true)
            binding.webView.clearHistory()
            binding.webView.clearSslPreferences()
            cookieManager?.removeAllCookies(null)
            cookieManager?.flush()
            WebStorage.getInstance().deleteAllData()
            if (isTaskRoot) {
                startActivity(Intent(this@WebViewActivity, MainActivity::class.java))
                finish()
            } else {
                super.onBackPressed()
            }


        }
    }
}