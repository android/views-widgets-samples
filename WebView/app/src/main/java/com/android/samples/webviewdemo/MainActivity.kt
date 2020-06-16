/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.samples.webviewdemo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.core.content.ContextCompat.startActivity
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import com.android.samples.webviewdemo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    //Creating the custom WebView Client Class
    private class MyWebViewClient(private val assetLoader: WebViewAssetLoader) :
        WebViewClientCompat() {
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.getUrl());
        }
    }

    /** Instantiate the interface and set the context  */
    class WebAppInterface(private val mContext: Context) {
        /** Send a message from the web page  */
        @JavascriptInterface
        fun sendMessage(message: String) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(mContext, shareIntent, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure asset loader with custom domain
        val assetLoader = WebViewAssetLoader.Builder()
            .setDomain("gcoleman799.github.io")
            .addPathHandler("/Asset-Loader/", WebViewAssetLoader.AssetsPathHandler(this))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(this))
            .build();

        //Set clients
        binding.webview.webViewClient = MyWebViewClient(assetLoader)

        //Set Title
        title = "WebView Weather"

        //Enable Javascript
        binding.webview.settings.javaScriptEnabled = true

        //Connect to Javascript Interface
        binding.webview.addJavascriptInterface(WebAppInterface(this), "Weather")

        //Load the content
        binding.webview.loadUrl("https://gcoleman799.github.io/Asset-Loader/index.html")
    }
}